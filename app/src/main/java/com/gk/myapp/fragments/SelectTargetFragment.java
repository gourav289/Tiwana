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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.OrderHistoryActivity;
import com.gk.myapp.activities.SelectAsmActivity;
import com.gk.myapp.activities.TargetDetailsActivity;
import com.gk.myapp.activities.so.AddPaymentActivity;
import com.gk.myapp.activities.so.ProductListActivity;
import com.gk.myapp.adapters.MonthListAdapter;
import com.gk.myapp.adapters.TargetListAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.locationupdate.JobSchedulerService;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.DealerTargetList;
import com.gk.myapp.model.YearTargetResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 12-10-2017.
 */
public class SelectTargetFragment extends BaseFragment {
//    private TextView txtTitleFirst, txtTitleSecond, txtStatusFirst, txtStatusSecond;
//    private LinearLayout linFirst, linSecond;
    private int year,currentYear;
    private BaseActivityTabs mActivity;

    private ListView lv;
    private List<DealerTargetList.MonthList> mList;
    private MonthListAdapter mAdp;

    @Override
    protected int setView() {
        return R.layout.fragment_select_target;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        if (getPreferences().getBooleanPref(P.JOB_STARTED)){
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
//            getActivity().startService(new Intent(getActivity(), MyService.class));

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        year=currentYear-1;
        getIds(view);
        setListeners();

        getYearTarget(year);

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
        mActivity = (BaseActivityTabs) getActivity();
//        txtTitleFirst = (TextView) v.findViewById(R.id.txt_first_half_title);
//        txtTitleSecond = (TextView) v.findViewById(R.id.txt_second_half_title);
//        txtStatusFirst = (TextView) v.findViewById(R.id.txt_first_half_status);
//        txtStatusSecond = (TextView) v.findViewById(R.id.txt_second_half_status);
//        linFirst = (LinearLayout) v.findViewById(R.id.lin_first);
//        linSecond = (LinearLayout) v.findViewById(R.id.lin_second);

        mList = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.list_view);
        if(getPreferences().getStringPref(P.USER_TYPE).equals(C.SO)) {

            mActivity.setLeftText("Order\nHistory", new BaseActivityTabs.HeaderButtonClick() {
                @Override
                public void onHeaderButtonClicked(int id) {
                    Intent mIntent = new Intent(mActivity, OrderHistoryActivity.class);
                    startActivity(mIntent);
                }
            });

            mActivity.setRightText("Create\nOrder", new BaseActivityTabs.HeaderButtonClick() {
                @Override
                public void onHeaderButtonClicked(int id) {
                    Intent mIntent = new Intent(getActivity(), ProductListActivity.class);
                    startActivity(mIntent);
                }
            });
        }
    }

    private void setListeners() {
//        linFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNextActivity(getPreferences().getStringPref(P.USER_TYPE),"1",txtTitleFirst.getText().toString());
//            }
//        });
//
//        linSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNextActivity(getPreferences().getStringPref(P.USER_TYPE),"2",txtTitleSecond.getText().toString());
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openNextActivity(getPreferences().getStringPref(P.USER_TYPE),mList.get(i).getMonth(),mList.get(i).getMonthDetail(),mList.get(i).getYear());
            }
        });
    }

    private void getYearTarget(final int yr) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DealerTargetList> call = apiService.getYearlyTarget(getPreferences().getStringPref(P.USER_ID), getPreferences().getStringPref(P.USER_TYPE), ""+yr);
            call.enqueue(new Callback<DealerTargetList>() {
                @Override
                public void onResponse(Call<DealerTargetList> call, Response<DealerTargetList> response) {
                    DealerTargetList data = response.body();
//                    if (data.getSuccess().equals("1")) {
////                        txtTitleFirst.setText(data.getData().getFirstHalf().getMonth());
////                        txtStatusFirst.setText(data.getData().getFirstHalf().getTargetStatus());
////                        txtTitleSecond.setText(data.getData().getSecondHalf().getMonth());
////                        txtStatusSecond.setText(data.getData().getSecondHalf().getTargetStatus());
//                    } else {
//                        showToast(data.getMessage());
//                    }

                    if (data.getSuccess().equals("1")) {
                        mList.addAll(data.getData());
                    } else {
                        showToast(data.getMessage());
                    }

                    if(yr!=currentYear){
                        getYearTarget(currentYear);
                    }else {
                        mAdp = new MonthListAdapter(getActivity(), mList);
                        lv.setAdapter(mAdp);
                        U.hideProgress();
                    }


                }

                @Override
                public void onFailure(Call<DealerTargetList> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void openNextActivity(String userType,String half,String title,String year){
        if (userType.equals(C.GM)) {
            Intent mIntent = new Intent(getActivity(), SelectAsmActivity.class);
            mIntent.putExtra("title",title);
            mIntent.putExtra("year", ""+year);
            mIntent.putExtra("half", half);
            startActivity(mIntent);
        }else if(userType.equals(C.ASM)){
            Intent mIntent = new Intent(getActivity(), SelectSoActivity.class);
            mIntent.putExtra("title",title);
            mIntent.putExtra("year", ""+year);
            mIntent.putExtra("half", half);
            mIntent.putExtra("from",C.ASM);
            startActivity(mIntent);
        }else{
            Intent mIntent = new Intent(getActivity(), TargetDetailsActivity.class);
            mIntent.putExtra("title",title);
            mIntent.putExtra("year", ""+year);
            mIntent.putExtra("half", half);
            mIntent.putExtra("so_id",getPreferences().getStringPref(P.USER_ID));
            startActivity(mIntent);
        }
    }
}
