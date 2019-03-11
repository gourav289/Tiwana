package com.gk.myapp.locationupdate;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.gk.myapp.distance_db.DatabaseHandler;
import com.gk.myapp.distance_db.DistanceModel;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.wdullaer.materialdatetimepicker.Utils;

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
 * Created by dev on 18/12/17.
 */

public class LocationUpdate extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;
    private static final long INTERVAL = C.LOCATION_UPDATE_TIME;
    private static final long FASTEST_INTERVAL =  C.LOCATION_UPDATE_TIME;
    LocationRequest mLocationRequest;
    Context mContext;
    private P mPreferences;
    public static LocationUpdate con;

    private SettingsClient mSettingsClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private DatabaseHandler db;

//    Location location1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        con = LocationUpdate.this;
        mPreferences = new P(getApplicationContext());
        createLocationRequest();
        return START_STICKY;
    }

    protected void createLocationRequest() {
        mPreferences = new P(getApplicationContext());
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);// use to check the location is enable or not
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mPreferences.setBooleanPref("not_assign", false);
            return;
        }
        mPreferences.setBooleanPref("not_assign", true);
        createLocationCallback();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
//                if (location1 == null) {
//                    location1 = new Location("locationA");
//                    location1.setLatitude(locationResult.getLocations().get(0).getLatitude());
//                    location1.setLongitude(locationResult.getLocations().get(0).getLongitude());
//                }
//                Location location2 = new Location("locationA");
//                location2.setLatitude(locationResult.getLocations().get(0).getLatitude());
//                location2.setLongitude(locationResult.getLocations().get(0).getLongitude());
//                double distance = location1.distanceTo(location2);
                con = LocationUpdate.this;
                Log.e("Longitude", "location getLongitude " + locationResult.getLocations().get(0).getLongitude());
                Log.e("Latitude", "location getLatitude " + locationResult.getLocations().get(0).getLatitude());
                if (locationResult.getLocations().get(0).getLatitude() <= 0 || locationResult.getLocations().get(0).getLongitude() <= 0) {

                }/* else if (distance < 50) {

                } */ else {
//                    location1 = new Location("locationA");
//                    location1.setLatitude(locationResult.getLocations().get(0).getLatitude());
//                    location1.setLongitude(locationResult.getLocations().get(0).getLongitude());
                    mPreferences.setStringPref("lati", "" + locationResult.getLocations().get(0).getLatitude());
                    mPreferences.setStringPref("longi", "" + locationResult.getLocations().get(0).getLongitude());
                    addLocation(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude(),"No update");
                    sendLocation(locationResult.getLocations().get(0).getLatitude(), locationResult.getLocations().get(0).getLongitude());
                }
            }
        };
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void sendLocation(final double lat, final double lng) {
        db=new DatabaseHandler(this);
        String imeiNumber = mPreferences.getStringPref(P.IMEI_NUMBER);

        try {
            if (db.getCount() >= 10) {
                JSONArray jArray = new JSONArray();
                for (DistanceModel disModel : db.getCompleteData()) {
                    JSONObject obj = new JSONObject();
                    obj.put("lat", "" + disModel.getLat());
                    obj.put("long",""+disModel.getLng());
                    obj.put("location",disModel.getAddress());
                    obj.put("mobile_information",imeiNumber);
                    obj.put("mobile_data_time",disModel.getDate());
                    jArray.put(obj);
                }

                addPerData(jArray.toString());
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonSuccessModel> call = apiService.updateLocationMultipleLat(mPreferences.getStringPref(P.USER_ID), "" + jArray.toString());
                call.enqueue(new Callback<CommonSuccessModel>() {
                    @Override
                    public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                        CommonSuccessModel data = response.body();
                        if(data.getSuccess().equalsIgnoreCase("1")) {
                            db.deleteRecords();
                        }
                        else
                            U.toast(mContext, data.getMessage());

                        addPerData(data.getMessage());

                    }

                    @Override
                    public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                        U.toast("Error from webservice");
//                        addLocation(lat, lng, "" + t);
                        addPerData(""+t);
                    }
                });

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

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

    public void stopcall() {

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        stopSelf();
    }

    private void addLocation(final double lat, final double lng,final String response) {
        HandlerThread handlerThread = new HandlerThread("database_helper");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
//                addLocation(lat,lng);

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

    private void addPerData(final String data){
        HandlerThread handlerThread = new HandlerThread("database_helper");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
//                addLocation(lat,lng);

                DatabaseHandler db = new DatabaseHandler(mContext);
                DistanceModel obj = new DistanceModel();
                obj.setAddress(data);
                db.addPermanentData(obj);
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
