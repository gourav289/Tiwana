package com.gk.myapp.fragments;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.activities.AddAllowancesActivity;
import com.gk.myapp.activities.ApplyLeaveActivity;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.LoginActivity;
import com.gk.myapp.adapters.MessageAdapter;
import com.gk.myapp.adapters.NotificationAdapter;
import com.gk.myapp.alarmservice.Alarm;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.interfaces.YesNoDialogConfirmation;
import com.gk.myapp.locationupdate.LocationUpdate;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.MessageListModel;
import com.gk.myapp.model.MessageModel;
import com.gk.myapp.model.NotificationModel;
import com.gk.myapp.model.NotificationResponseModel;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 18-12-2016.
 */
public class NotificationMainFragment extends BaseFragment implements View.OnClickListener, YesNoDialogConfirmation {
    private final int SALES = 1;
    private final int FINANCE = 2;
    private int selected = SALES;
    private Button btnSales, btnHuman, btnFinance;

    private ListView lv;
    private ArrayList<NotificationModel> mListSales;
    private NotificationAdapter mAdp;

    private BaseActivityTabs mActivity;




    @Override
    protected int setView() {
        return R.layout.fragment_notifications;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivityTabs) getActivity();
        getIds(view);
        setListeners();
        getNotification();
    }

    private void getIds(View v) {
        btnSales = (Button) v.findViewById(R.id.btn_sales);
        btnFinance = (Button) v.findViewById(R.id.btn_finance);
        btnHuman = (Button) v.findViewById(R.id.btn_human_resource);
        lv = (ListView) v.findViewById(R.id.list_view);
        mListSales = new ArrayList<>();

        mActivity.setRightButton(true, R.mipmap.ic_logout, new BaseActivityTabs.HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                U.yesNoDialog(getCont(), NotificationMainFragment.this, "", getString(R.string.logot_msg), 0, "logout");
            }
        });

        mAdp = new NotificationAdapter(getActivity(), mListSales);
        lv.setAdapter(mAdp);
    }

    private void setListeners() {
        btnSales.setOnClickListener(this);
        btnHuman.setOnClickListener(this);
        btnFinance.setOnClickListener(this);
    }

    @Override
    public void onYesClicked(int pos, String type) {
        if (type.equals("logout")) {
            if (getPreferences().getBooleanPref(P.JOB_STARTED))
                webserviceStopJob();
            else {
                getPreferences().clearLoginData();
                startActivity(new Intent(mActivity, LoginActivity.class));
                mActivity.finish();
            }
        }
    }


    private void getNotification() {
        if (U.isConnectedToInternet(getActivity())) {
            U.showProgress(getActivity());
            mListSales.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<NotificationResponseModel> call = apiService.getNotification(getPreferences().getStringPref(P.USER_ID), "" + selected);
            call.enqueue(new Callback<NotificationResponseModel>() {
                @Override
                public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                    NotificationResponseModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        mListSales.addAll(data.getData());
                    } else {
                        U.toast(data.getMessage());
                    }
                    mAdp.notifyList(mListSales);
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<NotificationResponseModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                    mAdp.notifyList(mListSales);
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sales:
                selected=SALES;
                getNotification();
                btnSales.setBackgroundResource(R.drawable.btn_notif_rounded_left_selected);
                btnHuman.setBackgroundResource(R.drawable.btn_notif_rect_unselected);
                btnFinance.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);

                break;

            case R.id.btn_human_resource:
                btnSales.setBackgroundResource(R.drawable.btn_notif_rounded_left_unselected);
                btnHuman.setBackgroundResource(R.drawable.btn_notif_rect_selected);
                btnFinance.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);

                break;

            case R.id.btn_finance:
                selected=FINANCE;
                getNotification();
                btnSales.setBackgroundResource(R.drawable.btn_notif_rounded_left_unselected);
                btnHuman.setBackgroundResource(R.drawable.btn_notif_rect_unselected);
                btnFinance.setBackgroundResource(R.drawable.btn_notif_rounded_right_selected);

                break;
        }
    }


    private void webserviceStopJob() {
        if (U.isConnectedToInternet(getContext())) {
            U.showProgress(getContext());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.startJob(getPreferences().getStringPref(P.EMP_CODE), "1", "1", "Stop", "2");
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        U.toast(data.getMessage());
                        Log.e("inside", "Job started");
                        getPreferences().setBooleanPref(P.JOB_STARTED, false);

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                            JobScheduler jobScheduler = (JobScheduler)getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                            jobScheduler.cancelAll();
                        }
//                        LocationUpdate.con.stopcall();
//                        getActivity().stopService(new Intent(getActivity(), LocationUpdate.class));

                        Alarm mAlarm=new Alarm();
                        mAlarm.stopAlarm(getActivity());
                        getPreferences().clearLoginData();
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        mActivity.finish();
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

}
