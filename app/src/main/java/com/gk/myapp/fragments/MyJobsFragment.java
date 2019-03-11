package com.gk.myapp.fragments;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.activities.AddAllowancesActivity;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.CreateMessageActivity;
import com.gk.myapp.activities.DummyActivity;
import com.gk.myapp.activities.LoginActivity;
import com.gk.myapp.activities.PaymentHistoryActivity;
import com.gk.myapp.activities.so.AddPaymentActivity;
import com.gk.myapp.activities.so.ProductListActivity;
import com.gk.myapp.alarmservice.Alarm;
import com.gk.myapp.distance_db.DatabaseHandler;
import com.gk.myapp.distance_db.DistanceModel;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.locationupdate.GPSTracker;
import com.gk.myapp.locationupdate.JobSchedulerService;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.UserDetails;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.MarshMallowPermission;
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
 * Created by Gk on 29-08-2017.
 */
public class MyJobsFragment extends BaseFragment implements View.OnClickListener {
    private BaseActivityTabs mActivity;
    private Button btnStart, btnStop;
    //    private Button btnClear, btnReport;
    private ImageView ivProfile;
    private TextView txtName, txtLocation, txtState;
    private String locationService = "";

    private P mP;

    private Button btnUpdateLocation;

//    private final int START=1;
//    private final int STOP=2;
//    private final int PAUSE=3;
//    private final int RESUME=4;
//    private int from=0;


    @Override
    protected int setView() {
        return R.layout.activity_my_job;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivityTabs) getActivity();

        mP = new P(getCont());

        Toast.makeText(getActivity(), "userid:  " + mP.getStringPref(P.USER_ID), Toast.LENGTH_SHORT).show();
        getIds(view);
        setListeners();

        changeButtonText();

    }

    private void getIds(View view) {
        btnStart = (Button) view.findViewById(R.id.btn_start);
        btnStop = (Button) view.findViewById(R.id.btn_stop);
        txtState = (TextView) view.findViewById(R.id.txt_state);

        btnUpdateLocation = (Button) view.findViewById(R.id.btn_update_location);

//        btnClear = (Button) view.findViewById(R.id.btn_clear);
//        btnReport = (Button) view.findViewById(R.id.btn_show);
//        if (mP.getBooleanPref(P.JOB_STARTED)) {
//            btnStart.setText(getString(R.string.btn_job_started));
//            btnStop.setText(getString(R.string.stop_job));
//        } else {
//            btnStart.setText(getString(R.string.start_job));
//            btnStop.setText(getString(R.string.btn_job_stopped));
//        }


        if (getPreferences().getStringPref(P.USER_TYPE).equals(C.SO)) {
            mActivity.setRightText("Add\nPayment", new BaseActivityTabs.HeaderButtonClick() {
                @Override
                public void onHeaderButtonClicked(int id) {
                    Intent mIntent = new Intent(mActivity, AddPaymentActivity.class);
                    startActivity(mIntent);
                }
            });
        }

        txtName = (TextView) view.findViewById(R.id.txt_name);
        txtLocation = (TextView) view.findViewById(R.id.txt_location);
        ivProfile = (ImageView) view.findViewById(R.id.iv_profile_pic);

        txtName.setText(getPreferences().getStringPref(P.EMP_CODE));
        txtLocation.setText(getPreferences().getStringPref(P.DESIGNATION));
        txtState.setText(getPreferences().getStringPref(P.STATE));

        if (getPreferences().getStringPref(P.IMAGE) != null && !getPreferences().getStringPref(P.IMAGE).equals("")) {
            Glide.with(this)
                    .load(C.IMAGE_BASE_URL + getPreferences().getStringPref(P.IMAGE))
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_profile_pic)
                    .crossFade()
                    .into(ivProfile);
        }

        getProfileWebService();
    }


    private void setListeners() {
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
//        btnClear.setOnClickListener(this);
//        btnReport.setOnClickListener(this);

        btnUpdateLocation.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
//                if (mP.getBooleanPref(P.JOB_STARTED)) {
//                    U.toast(getString(R.string.job_started));
//                } else if (!isGPSEnabled()) {
//                    U.toast(getCont(), getString(R.string.on_gps));
//                }
//                else {
//                    if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
//                        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
//                        if (marshMallowPermission.checkPermissionForLocation()) {
//                            webserviceStartJob();
//                        } else {
//                            marshMallowPermission.requestPermissionForLocation();
//                        }
//                    } else {
//                        webserviceStartJob();
//                    }
//                }


                if (!isGPSEnabled()) {
                    U.toast(getCont(), getString(R.string.on_gps));
                } else {
                    if (locationService.equalsIgnoreCase("off")) {
                        showToast(getString(R.string.location_off));
                    } else {
                        if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_STARTED) {
                            sendLocation(C.JOB_STARTED);
                        } else if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_STOPPED) {
                            startJob(C.JOB_STOPPED);
                        } else {
                            startJob(C.JOB_PAUSED);
                        }
                    }
                }


                break;

            case R.id.btn_stop:
//                if (!mP.getBooleanPref(P.JOB_STARTED))
//                    U.toast(getString(R.string.job_stoped));
//                else {
//                    webserviceStopJob();
//                }


                if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_STOPPED)
                    U.toast(getString(R.string.job_stoped));
                else {
                    sendLocation(C.JOB_STOPPED);
                }
                break;
//            case R.id.btn_clear:
//                DatabaseHandler db = new DatabaseHandler(getActivity());
//                db.deletePerRecords();
//                break;
//            case R.id.btn_show:
//                Intent in = new Intent(getActivity(), DummyActivity.class);
//                startActivity(in);
//                break;

            case R.id.btn_update_location:
                if (mP.getIntPreference(P.JOB_STATUS) != C.JOB_STOPPED) {
                    locationLock();
                } else {
                    U.toast(getString(R.string.job_stoped));
                }
                break;

        }
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

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

    private void startJob(int from) {
        if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
            if (marshMallowPermission.checkPermissionForLocation()) {
                webserviceStartJob(from);
            } else {
                marshMallowPermission.requestPermissionForLocation();
            }
        } else {
            webserviceStartJob(from);
        }
    }


    private void webserviceStartJob(final int from) {
        if (U.isConnectedToInternet(getContext())) {
            U.showProgress(getContext());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.startJob(getPreferences().getStringPref(P.EMP_CODE), "1", "1", "Start", "1");
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
//                        U.toast(data.getMessage());
                        Log.e("inside", "Job started");
//                        mP.setBooleanPref(P.JOB_STARTED, true);
//                        btnStart.setText(getString(R.string.btn_job_started));
//                        btnStop.setText(getString(R.string.stop_job));

                        mP.setIntPref(P.JOB_STATUS, C.JOB_STARTED);

//                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                            JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(getActivity().JOB_SCHEDULER_SERVICE);
//                            ComponentName jobService = new ComponentName(getActivity().getPackageName(), JobSchedulerService.class.getName());
//                            JobInfo jobInfo;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                jobInfo = new JobInfo.Builder(1, jobService)
//                                        .setMinimumLatency(C.LOCATION_UPDATE_TIME).build();
//                            } else {
//                                jobInfo = new JobInfo.Builder(1, jobService)
//                                        .setPeriodic(C.LOCATION_UPDATE_TIME).build();
//                            }
//                            jobScheduler.schedule(jobInfo);
//                        } else {
//                            if (checkServiceRunning()) {
//
//                            } else {
//                                getActivity().startService(new Intent(getActivity(), LocationUpdate.class));
//                            }
//                        }
                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        DistanceModel obj = new DistanceModel();
                        obj.setLat(0);
                        obj.setLng(0);
                        obj.setAddress("");
                        obj.setDistance("Started");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sf.format(System.currentTimeMillis());
                        obj.setDate(date);
                        obj.setResponse(data.getMessage());
                        db.addData(obj);
                        Alarm mAlarm = new Alarm();
                        mAlarm.startAlarm(getActivity());

                        changeButtonText();
//                        getActivity().startService(new Intent(getActivity(), MyService.class));
                    } else {
                        U.toast(data.getMessage());
                    }
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void webserviceStopJob(final int from) {
        if (U.isConnectedToInternet(getContext())) {
//            U.showProgress(getContext());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.startJob(getPreferences().getStringPref(P.EMP_CODE), "1", "1", "Stop", "2");
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
//                        U.toast(data.getMessage());
                        Log.e("inside", "Job started");
//                        mP.setBooleanPref(P.JOB_STARTED, false);
//                        btnStart.setText(getString(R.string.start_job));
//                        btnStop.setText(getString(R.string.btn_job_stopped));


//                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                            JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                            jobScheduler.cancelAll();
//                        }
//                        LocationUpdate.con.stopcall();
//                        getActivity().stopService(new Intent(getActivity(), LocationUpdate.class));


                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        DistanceModel obj = new DistanceModel();
                        obj.setLat(0);
                        obj.setLng(0);
                        obj.setAddress("");
                        obj.setDistance("Stopped");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sf.format(System.currentTimeMillis());
                        obj.setDate(date);
                        db.addData(obj);
                        Alarm mAlarm = new Alarm();
                        mAlarm.stopAlarm(getActivity());
                        if (from == C.JOB_STARTED) {
                            mP.setIntPref(P.JOB_STATUS, C.JOB_PAUSED);
                        } else {
                            mP.setIntPref(P.JOB_STATUS, C.JOB_STOPPED);
                            Intent allowActivity = new Intent(getActivity(), AddAllowancesActivity.class);
                            startActivity(allowActivity);
                        }
                    } else {
                        U.toast(data.getMessage());
                    }

                    changeButtonText();
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager)
                getCont().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.gk.myapp.locationupdate.LocationUpdate"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void changeButtonText() {
        if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_STARTED) {
            btnStart.setText("PAUSE JOB");
            btnStop.setText("STOP JOB");
        } else if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_PAUSED) {
            btnStart.setText("RESUME JOB");
            btnStop.setText("STOP JOB");
        } else {
            btnStart.setText("START JOB");
            btnStop.setText("JOB STOPPED");
        }
    }


    private void sendLocation(final int from) {
        if (U.isConnectedToInternet(getContext())) {
            U.showProgress(getCont());
            final DatabaseHandler db = new DatabaseHandler(getActivity());
            P mPreferences = new P(getActivity());
            String imeiNumber = mPreferences.getStringPref(P.IMEI_NUMBER);
            try {
                if (db.getCount() > 0) {
                    JSONArray jArray = new JSONArray();
                    for (DistanceModel disModel : db.getCompleteData()) {
                        JSONObject obj = new JSONObject();
                        obj.put("lat", "" + disModel.getLat());
                        obj.put("long", "" + disModel.getLng());
                        obj.put("location", disModel.getAddress());
                        obj.put("mobile_information", imeiNumber);
                        obj.put("mobile_data_time", disModel.getDate());
                        jArray.put(obj);
                    }

                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<CommonSuccessModel> call = apiService.updateLocationMultipleLat(mPreferences.getStringPref(P.USER_ID), "" + jArray.toString());
                    call.enqueue(new Callback<CommonSuccessModel>() {
                        @Override
                        public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                            CommonSuccessModel data = response.body();
                            if (data.getSuccess().equalsIgnoreCase("1")) {
                                db.deleteRecords();
//                                db.deletePerRecords();
                                webserviceStopJob(from);
                            } else {
                                U.hideProgress();
                                U.toast(getActivity(), data.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                            U.hideProgress();
                            U.toast("Error from webservice");
                        }
                    });

                } else {
                    webserviceStopJob(from);
                }
            } catch (Exception e) {
                U.hideProgress();
                e.printStackTrace();
                U.toast("Some error occurred, try again");
            }
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }


    private void getProfileWebService() {
        if (U.isConnectedToInternet(getContext())) {
            U.showProgress(getContext());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<UserDetails> call = apiService.getCompleteProfile(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                    UserDetails data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        setData(data.getUserData());
                    } else {
                        U.toast(data.getMessage());
                    }
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }


    private void setData(UserDetails.UserData data) {
        txtName.setText(data.getName() + "-" + data.getEmpCode());
        if (data.getRole().equals(C.GM)) {
            txtLocation.setText("General Manager");
        } else if (data.getRole().equals(C.ASM)) {
            txtLocation.setText("Area Sales Manager");
        } else {
            txtLocation.setText("Sales Officer");
        }

        txtState.setText(data.getState());

        if (getPreferences().getStringPref(P.IMAGE) != null && !getPreferences().getStringPref(P.IMAGE).equals("")) {
            Glide.with(this)
                    .load(C.IMAGE_BASE_URL + getPreferences().getStringPref(P.IMAGE))
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_profile_pic)
                    .crossFade()
                    .into(ivProfile);
        }
        locationService = data.getLocation();
    }


    private void locationLock() {
        if (U.isConnectedToInternet(getContext())) {
            U.showProgress(getCont());
            GPSTracker gpsTracker = new GPSTracker(getActivity());
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            String address = getAddress(lat, lng);
            P mPreferences = new P(getActivity());
            String imeiNumber = mPreferences.getStringPref(P.IMEI_NUMBER);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.locationLock(mPreferences.getStringPref(P.USER_ID), "" + lat, "" + lng, address, imeiNumber);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    U.toast(getActivity(), data.getMessage());
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    U.hideProgress();
                    U.toast("Error from webservice");
                }
            });

        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

}
