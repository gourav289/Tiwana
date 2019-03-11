package com.gk.myapp.activities;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.interfaces.YesNoDialogConfirmation;
import com.gk.myapp.locationupdate.JobSchedulerService;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.UserDetails;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.MarshMallowPermission;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 22-02-2017.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener, BaseActivity.HeaderButtonClick, YesNoDialogConfirmation {
    private Button btnStart, btnStop;

    private ImageView ivProfilePic;
    private TextView txtName, txtEmpCode, txtAreaCode, txtAge, txtMobile, txtAddress, txtState, txtCity, txtEmail;
    private P mP;
    private boolean fromLogout = false;

    private Context mContext;
    @Override
    protected String setHeaderTitle() {
        return getString(R.string.profile);
    }

    @Override
    protected int setView() {
        return R.layout.activity_profile_other_emp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mP = new P();
//        if(mP.getBooleanPref(P.JOB_STARTED))
//            callService();
//            startService(new Intent(ProfileActivity.this, MyService.class));
        mContext=this;
        setRightButton(true, R.mipmap.ic_logout, this);
        getIds();
        setListeners();
        getProfileWebService();

        changeButtonText();
    }


    private void getIds() {
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        ivProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtEmpCode = (TextView) findViewById(R.id.txt_emp_code);
        txtAreaCode = (TextView) findViewById(R.id.txt_area_code);
        txtAge = (TextView) findViewById(R.id.txt_age);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        txtAddress = (TextView) findViewById(R.id.txt_address);
        txtState = (TextView) findViewById(R.id.txt_state);
        txtCity = (TextView) findViewById(R.id.txt_city);
        txtEmail = (TextView) findViewById(R.id.txt_email);

//        if (mP.getBooleanPref(P.JOB_STARTED)) {
//            btnStart.setText(getString(R.string.btn_job_started));
//            btnStop.setText(getString(R.string.stop_job));
//        }else{
//            btnStart.setText(getString(R.string.start_job));
//            btnStop.setText(getString(R.string.btn_job_stopped));
//        }
    }


    private void setListeners() {
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    private void setData(UserDetails.UserData data) {
        if (data.getImage() != null && !data.getImage().equals("")) {
            Glide
                    .with(this)
                    .load(C.IMAGE_BASE_URL + data.getImage())
                    .centerCrop()
                    .placeholder(R.mipmap.dummy_profile_pic)
                    .crossFade()
                    .into(ivProfilePic);
        }
        txtName.setText(data.getName());
        txtCity.setText(data.getCity());
        txtState.setText(data.getState());
        txtAddress.setText(data.getAddress());
        txtAge.setText(data.getAge());
        txtAreaCode.setText(data.getAreaCode());
        txtEmpCode.setText(data.getEmpCode());
        txtMobile.setText(data.getMobile());
        txtEmail.setText(data.getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
//

//                if(!isGPSEnabled()){
//                    U.toast(mContext,getString(R.string.on_gps));
//                }else {
//                    if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_STARTED) {
//                        webserviceStopJob(C.JOB_STARTED);
//                    } else if (mP.getIntPreference(P.JOB_STATUS) == C.JOB_STOPPED) {
//                        startJob(C.JOB_STOPPED);
//                    } else {
//                        startJob(C.JOB_PAUSED);
//                    }
//                }

                break;

            case R.id.btn_stop:

//                if (mP.getIntPreference(P.JOB_STATUS)==C.JOB_STOPPED)
//                    U.toast(getString(R.string.job_stoped));
//                else {
//                    webserviceStopJob(C.JOB_STOPPED);
//                }
                break;

        }
    }

    private void startJob(int from){
        if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
            MarshMallowPermission marshMallowPermission = new MarshMallowPermission(ProfileActivity.this);
            if (marshMallowPermission.checkPermissionForLocation()) {
                webserviceStartJob(from);
            } else {
                marshMallowPermission.requestPermissionForLocation();
            }
        } else {
            webserviceStartJob(from);
        }
    }

    private void webserviceStartJob(int from) {
        if(U.isConnectedToInternet(getContext())) {
            U.showProgress(getContext());
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            String date[] = (sf.format(cal.getTime())).split(" ");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.startJob(getPreferences().getStringPref(P.EMP_CODE), "1", "1", "Start","1");
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        U.toast(data.getMessage());
                        Log.e("inside", "Job started");
//                    Alarm alarm = new Alarm();
//                    alarm.SetAlarm(ProfileActivity.this, System.currentTimeMillis());
//                        mP.setBooleanPref(P.JOB_STARTED, true);
//                        btnStart.setText(getString(R.string.btn_job_started));
//                        btnStop.setText(getString(R.string.stop_job));

                        mP.setIntPref(P.JOB_STATUS,C.JOB_STARTED);
                        callService();
//                        startService(new Intent(ProfileActivity.this, MyService.class));
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
        }else{
            U.toast(getString(R.string.no_internet));
        }
    }

    private void webserviceStopJob(final int from) {
        if(U.isConnectedToInternet(getContext())) {
            U.showProgress(getContext());
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            String date[] = (sf.format(cal.getTime())).split(" ");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.startJob(getPreferences().getStringPref(P.EMP_CODE), "1", "1", "Stop", "2");
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        U.toast(data.getMessage());
                        Log.e("inside", "Job started");
//                    Alarm alarm = new Alarm();
//                    alarm.CancelAlarm(ProfileActivity.this);
//                        mP.setBooleanPref(P.JOB_STARTED, false);
//                        btnStart.setText(getString(R.string.start_job));
//                        btnStop.setText(getString(R.string.btn_job_stopped));

                        if(from==C.JOB_STARTED) {
                            mP.setIntPref(P.JOB_STATUS,C.JOB_PAUSED);
                        }else {
                            mP.setIntPref(P.JOB_STATUS,C.JOB_STOPPED);
                            Intent allowActivity = new Intent(ProfileActivity.this, AddAllowancesActivity.class);
                            startActivity(allowActivity);
                        }

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
                            jobScheduler.cancelAll();
                        }
                        LocationUpdate.con.stopcall();
                        stopService(new Intent(getApplicationContext(), LocationUpdate.class));
//                        stopService(new Intent(ProfileActivity.this, MyService.class));
                        if (fromLogout) {
                            mP.clearLoginData();
                            Intent mIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(mIntent);
                            finish();
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
        }else{
            U.toast(getString(R.string.no_internet));
        }
    }

    private void getProfileWebService() {
        if(U.isConnectedToInternet(getContext())) {
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
        }else{
            U.toast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                webserviceStartJob();
            }
        }
    }

    @Override
    public void onHeaderButtonClicked(int id) {
        U.yesNoDialog(getContext(), ProfileActivity.this, "", getString(R.string.logot_msg), 0, "logout");
    }

    @Override
    public void onBackPressed() {
        U.yesNoDialog(getContext(), ProfileActivity.this, "", getString(R.string.exit_msg), 0, "exit");
    }

    @Override
    public void onYesClicked(int pos, String type) {
        if (type.equals("exit"))
            finish();
        else if (type.equals("logout")){
            if(mP.getBooleanPref(P.JOB_STARTED)) {
                fromLogout=true;
                webserviceStopJob(C.JOB_STARTED);
            }else{
                mP.clearLoginData();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        }
    }

    public boolean isGPSEnabled (){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    void callService(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName jobService = new ComponentName(getPackageName(), JobSchedulerService.class.getName());
            JobInfo jobInfo;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                jobInfo = new JobInfo.Builder(1, jobService)
                        .setMinimumLatency(C.LOCATION_UPDATE_TIME).build();
            } else {
                jobInfo = new JobInfo.Builder(1, jobService)
                        .setPeriodic(C.LOCATION_UPDATE_TIME).build();
            }
            jobScheduler.schedule(jobInfo);
        } else {
            if (checkServiceRunning()) {

            } else {
                startService(new Intent(getApplicationContext(), LocationUpdate.class));
            }
        }
    }

    public boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.gk.myapp.locationupdate.LocationUpdate"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void changeButtonText(){
        if(mP.getIntPreference(P.JOB_STATUS)==C.JOB_STARTED){
            btnStart.setText("PAUSE JOB");
            btnStop.setText("STOP JOB");
        }else if(mP.getIntPreference(P.JOB_STATUS)==C.JOB_PAUSED){
            btnStart.setText("RESUME JOB");
            btnStop.setText("STOP JOB");
        }else{
            btnStart.setText("START JOB");
            btnStop.setText("JOB STOPPED");
        }
    }

}
