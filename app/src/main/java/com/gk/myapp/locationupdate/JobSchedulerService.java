package com.gk.myapp.locationupdate;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;

/**
 * Created by Goyal's on 19-12-2017.
 */
public class JobSchedulerService extends JobService {

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            jobFinished((JobParameters) msg.obj, false);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                scheduleRefresh();
            if (checkServiceRunning()) {
                P mPreferences = new P(getApplicationContext());
                if (!mPreferences.getBooleanPref("not_assign")) {
//                    startService(new Intent(getApplicationContext(), LocationUpdate.class));
                }
            } else {
//                startService(new Intent(getApplicationContext(), LocationUpdate.class));
            }
            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("newConfig", "newConfig");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e("rebind", "rebind");
    }

    private void scheduleRefresh() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName jobService = new ComponentName(getPackageName(), JobSchedulerService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(1, jobService).setMinimumLatency(C.LOCATION_UPDATE_TIME).build();
        jobScheduler.schedule(jobInfo);
    }

}
