package com.gk.myapp.alarmservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.gk.myapp.distance_db.DatabaseHandler;
import com.gk.myapp.distance_db.DistanceModel;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.locationupdate.GPSTracker;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 07-01-2018.
 */
public class Alarm extends BroadcastReceiver {

    private int ALARM_ID = 111111;
    private Context mContext;
    private P mPreferences;

    @Override
    public void onReceive(final Context context, Intent intent) {

        mContext = context;
        Toast.makeText(context, "Alarm..Call", Toast.LENGTH_SHORT).show();
        mPreferences = new P(mContext);
        GPSTracker gpsTracker = new GPSTracker(context);
        final double lat = gpsTracker.getLatitude();
        final double lng = gpsTracker.getLongitude();

//        final double lat=Double.parseDouble(mPreferences.getStringPref("lati"));
//        final double lng=Double.parseDouble(mPreferences.getStringPref("longi"));

//        mContext.startService(new Intent(mContext, LocationUpdate.class));

        addLocation(lat, lng, "No update");
        sendLocation(lat, lng);
    }

    public void startAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), ALARM_ID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                , C.LOCATION_UPDATE_TIME, pendingIntent);
    }


    public void stopAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), ALARM_ID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void sendLocation(final double lat, final double lng) {
        final DatabaseHandler db = new DatabaseHandler(mContext);
        final P mPreferences = new P(mContext);
        String imeiNumber = mPreferences.getStringPref(P.IMEI_NUMBER);
        Log.e("inside service", "lat:  " + lat + " ,long: " + lng + " , " + "" + ", " + imeiNumber);

        try {
            if (db.getCount() >= 10) {
                final JSONArray jArray = new JSONArray();
                for (DistanceModel disModel : db.getCompleteData()) {
                    JSONObject obj = new JSONObject();
                    obj.put("lat", "" + disModel.getLat());
                    obj.put("long", "" + disModel.getLng());
                    obj.put("location", disModel.getAddress());
                    obj.put("mobile_information", imeiNumber);
                    obj.put("mobile_data_time", disModel.getDate());
                    jArray.put(obj);
                }

                if (!mPreferences.getBooleanPref(P.LOCATION_UPDATING)) {
                    mPreferences.setBooleanPref(P.LOCATION_UPDATING, true);
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<CommonSuccessModel> call = apiService.updateLocationMultipleLat(mPreferences.getStringPref(P.USER_ID), "" + jArray.toString());
                    call.enqueue(new Callback<CommonSuccessModel>() {
                        @Override
                        public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                            CommonSuccessModel data = response.body();
                            addPermanentLocation(jArray.toString(), data.getMessage() + "   Return:  " + data.getSuccess());
                            if (data.getSuccess().equalsIgnoreCase("1")) {
                                db.deleteRecords();
                            } else
                                U.toast(mContext, data.getMessage());
                            mPreferences.setBooleanPref(P.LOCATION_UPDATING, false);
//                        addLocation(lat,lng,data.getMessage());
                        }

                        @Override
                        public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                            U.toast("Error from webservice");
//                        addLocation(lat, lng, "" + t);
                            addPermanentLocation(jArray.toString(), t.toString());
                            mPreferences.setBooleanPref(P.LOCATION_UPDATING, false);
                        }
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<CommonSuccessModel> call = apiService.updateLocation(mPreferences.getStringPref(P.USER_ID), "" + lat, "" + lng, address, imeiNumber);
//        call.enqueue(new Callback<CommonSuccessModel>() {
//            @Override
//            public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
//                CommonSuccessModel data = response.body();
//                Log.e("response", "location");
////                U.toast(/*data.getMessage()*/"Posted"+"\nlat:  "+lat+"\nlong: "+lng+"\nAddress : "+address);
//            }
//
//            @Override
//            public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
//                // Log error here since request failed
//                U.toast("Error");
//            }
//        });
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                address = addresses.get(0).getAddressLine(i) + ", ";
            }

            if (address.length() > 0) {
                address = address.substring(0, address.length() - 2);
            } else
                address = addresses.get(0).getAddressLine(0);

            address = address.replace("Unnamed Road", "");

            if (address.trim().equals(""))
                address = "NA";

            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            String comAdd = address + " " + city + " " + state + " " + country;
            comAdd = comAdd.replaceAll("null", "");
            return comAdd;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void addLocation(final double lat, final double lng, final String response) {
        HandlerThread handlerThread = new HandlerThread("database_helper");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                DatabaseHandler db = new DatabaseHandler(mContext);
                DistanceModel obj = new DistanceModel();
                obj.setLat(lat);
                obj.setLng(lng);
                obj.setAddress(getAddress(lat, lng));
                obj.setResponse(response);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sf.format(System.currentTimeMillis());
                obj.setDate(date);
                db.addData(obj);
            }
        });


    }

    private void addPermanentLocation(final String data, final String response) {
        HandlerThread handlerThread = new HandlerThread("database_helper");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                DatabaseHandler db = new DatabaseHandler(mContext);
                DistanceModel obj = new DistanceModel();
                obj.setAddress(data);
                obj.setResponse(response);
                db.addPermanentData(obj);
            }
        });


    }


}
