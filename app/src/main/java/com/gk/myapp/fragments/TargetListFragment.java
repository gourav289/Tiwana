package com.gk.myapp.fragments;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.CreateTargetActivity;
import com.gk.myapp.activities.DistributeTargetActivity;
import com.gk.myapp.activities.SelectAsmActivity;
import com.gk.myapp.activities.so.AddPaymentActivity;
import com.gk.myapp.adapters.TargetListAdapter;
import com.gk.myapp.locationupdate.JobSchedulerService;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.model.TargetModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;

import java.util.ArrayList;

/**
 * Created by Gk on 17-12-2016.
 */
public class TargetListFragment extends BaseFragment implements BaseActivityTabs.HeaderButtonClick {

    private ListView lv;
    private TargetListAdapter mTargetListAdapter;
    private ArrayList<TargetModel> mList;
    private BaseActivityTabs mActivity;

    private P mP;

    @Override
    protected int setView() {
        return R.layout.fragment_target_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivityTabs) getActivity();
        mList = new ArrayList<>();
        mP = new P();
//        if(mP.getBooleanPref(P.JOB_STARTED)){
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(getActivity().JOB_SCHEDULER_SERVICE);
//                ComponentName jobService = new ComponentName(getActivity().getPackageName(), JobSchedulerService.class.getName());
//                JobInfo jobInfo;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    jobInfo = new JobInfo.Builder(1, jobService)
//                            .setMinimumLatency(C.LOCATION_UPDATE_TIME).build();
//                } else {
//                    jobInfo = new JobInfo.Builder(1, jobService)
//                            .setPeriodic(C.LOCATION_UPDATE_TIME).build();
//                }
//                jobScheduler.schedule(jobInfo);
//            } else {
//                if (checkServiceRunning()) {
//
//                } else {
////                    getActivity().startService(new Intent(getActivity(), LocationUpdate.class));
//                }
//            }
//        }
////            getActivity().startService(new Intent(getActivity(), MyService.class));

        getIds(view);
        setListeners();

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

    private void getIds(View v) {
        lv = (ListView) v.findViewById(R.id.list_view);
        mTargetListAdapter = new TargetListAdapter(mActivity, mList);
        lv.setAdapter(mTargetListAdapter);
        mActivity.setRightButton(true, R.mipmap.ic_add, this);
    }

    private void setListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(mActivity, SelectAsmActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onHeaderButtonClicked(int id) {
        Intent mIntent = new Intent(mActivity, AddPaymentActivity.class);
        startActivity(mIntent);
    }
}
