package com.gk.myapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.activities.ApplyLeaveActivity;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.MyLeaveDetailsActivity;
import com.gk.myapp.activities.StaffLeaveDetailsActivity;
import com.gk.myapp.adapters.LeaveAdapter;
import com.gk.myapp.adapters.StaffLeaveAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.LeaveDetailModel;
import com.gk.myapp.model.LeaveListModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 18-12-2016.
 */
public class LeaveFragment extends BaseFragment implements View.OnClickListener, StaffLeaveAdapter.AcceptRejectLeave, LeaveAdapter.CancelLeaveListener {
    public static boolean update = false;
    private final int MY_LEAVE = 1;
    private final int STAFF_LEAVE = 2;
    private int selected = MY_LEAVE;

    private ListView lv;
    private ArrayList<LeaveDetailModel> mList, staffLeave;
    private LeaveAdapter mAdp;
    private BaseActivityTabs mActivity;

    private Button btnMyLeaves, btnStaffLeaves;

    private StaffLeaveAdapter staffLeaveAdapter;
    private LinearLayout linTopBtns;

    @Override
    protected int setView() {
        return R.layout.fragment_leave;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivityTabs) getActivity();

        getIds(view);
    }

    private void getIds(View v) {
        linTopBtns = (LinearLayout) v.findViewById(R.id.lin_top_btns);
        lv = (ListView) v.findViewById(R.id.list_view);
        mList = new ArrayList<>();
        staffLeave = new ArrayList<>();
        mAdp = new LeaveAdapter(getActivity(), mList,this);
        staffLeaveAdapter = new StaffLeaveAdapter(getActivity(), mList, this);
        lv.setAdapter(mAdp);

        getMyLeaves();

        mActivity.setRightButton(true, R.mipmap.ic_add, new BaseActivityTabs.HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                Intent mIntent = new Intent(mActivity, ApplyLeaveActivity.class);
                startActivity(mIntent);
            }
        });

        btnMyLeaves = (Button) v.findViewById(R.id.btn_my_leaves);
        btnStaffLeaves = (Button) v.findViewById(R.id.btn_staff_leaves);

        btnMyLeaves.setOnClickListener(this);
        btnStaffLeaves.setOnClickListener(this);

        if (getPreferences().getStringPref(P.USER_TYPE).equals(C.SO)) {
            linTopBtns.setVisibility(View.GONE);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected == MY_LEAVE) {
                    Intent mIntent = new Intent(getActivity(), MyLeaveDetailsActivity.class);
                    mIntent.putExtra("leaveId", mList.get(position).getId());
                    startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(getActivity(), StaffLeaveDetailsActivity.class);
                    mIntent.putExtra("leaveId", staffLeave.get(position).getId());
                    startActivity(mIntent);
                }
            }
        });

    }

    private void getMyLeaves() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LeaveListModel> call = apiService.getMyLeaves(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<LeaveListModel>() {
                @Override
                public void onResponse(Call<LeaveListModel> call, Response<LeaveListModel> response) {
                    LeaveListModel data = response.body();
                    U.hideProgress();
                    selected = MY_LEAVE;
                    mList.clear();
                    btnMyLeaves.setBackgroundResource(R.drawable.btn_notif_rounded_left_selected);
                    btnStaffLeaves.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        mList.addAll(data.getData());

                    } else {
                        U.toast(data.getMessage());
                    }
                    mAdp.notifyData(mList);
                    lv.setAdapter(mAdp);
                }

                @Override
                public void onFailure(Call<LeaveListModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }


    private void cancelLeave(String leaveId) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.cancelLeave(getPreferences().getStringPref(P.USER_ID), leaveId);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
//                    U.hideProgress();

                    U.toast(data.getMessage());
                    getMyLeaves();
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void getStaffLeaves() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LeaveListModel> call = apiService.getStaffLeave(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<LeaveListModel>() {
                @Override
                public void onResponse(Call<LeaveListModel> call, Response<LeaveListModel> response) {
                    LeaveListModel data = response.body();
                    U.hideProgress();
                    selected = STAFF_LEAVE;
                    staffLeave.clear();
                    btnStaffLeaves.setBackgroundResource(R.drawable.btn_notif_rounded_right_selected);
                    btnMyLeaves.setBackgroundResource(R.drawable.btn_notif_rounded_left_unselected);
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        staffLeave.addAll(data.getData());
                    } else {
                        U.toast(data.getMessage());
                    }
                    staffLeaveAdapter.notifyList(staffLeave);
                    lv.setAdapter(staffLeaveAdapter);


                }

                @Override
                public void onFailure(Call<LeaveListModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void rejectLeave(String leaveId) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.changeLeaveStatus(getPreferences().getStringPref(P.USER_ID), leaveId, "" + C.CANCEL);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        getStaffLeaves();
                    }
                    U.toast(data.getMessage());

                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void acceptLeave(String leaveId) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.changeLeaveStatus(getPreferences().getStringPref(P.USER_ID), leaveId, "" + C.APPROVE);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        getStaffLeaves();
                    }
                    U.toast(data.getMessage());

                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (update && selected == MY_LEAVE) {
            update = false;
            getMyLeaves();
        } else if (update && selected == STAFF_LEAVE) {
            update = false;
            getStaffLeaves();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_leaves:
                getMyLeaves();
                break;

            case R.id.btn_staff_leaves:
                getStaffLeaves();
                break;

        }
    }

    @Override
    public void onAcceptLeave(String leaveId) {
        acceptLeave(leaveId);
    }

    @Override
    public void onRejectLeave(String leaveId) {
        rejectLeave(leaveId);
    }

    @Override
    public void onCancelListener(LeaveDetailModel obj) {
        cancelLeave(obj.getId());
    }
}
