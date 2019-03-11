package com.gk.myapp.broardcast_receivers;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.gk.myapp.locationupdate.JobSchedulerService;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;

/**
 * Created by Gk on 12-08-2017.
 */
public class PowerOnOffBroadcastReceiver extends BroadcastReceiver {
    private P mPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Mobile switched On",Toast.LENGTH_LONG).show();
        Log.e("poweronbroad","Device switched On");
        mPreferences=new P(context);
        if(mPreferences.getBooleanPref(P.JOB_STARTED)){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
                ComponentName jobService = new ComponentName(context.getPackageName(), JobSchedulerService.class.getName());
//            JobInfo jobInfo = new JobInfo.Builder(1, jobService).setPeriodic(1000,1000).build();
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
                if (checkServiceRunning(context)) {

                } else {
//                    context.startService(new Intent(context, LocationUpdate.class));
                }
            }
        }
//            context.startService(new Intent(context, MyService.class));
    }

    public boolean checkServiceRunning(Context con) {
        ActivityManager manager = (ActivityManager) con.getSystemService(con.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.gk.myapp.locationupdate.LocationUpdate"
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
