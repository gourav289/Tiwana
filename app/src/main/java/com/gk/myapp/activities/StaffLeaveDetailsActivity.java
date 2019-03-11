package com.gk.myapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.fragments.LeaveFragment;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.LeaveDetailModel;
import com.gk.myapp.model.LeaveDetailsDataModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 12-08-2017.
 */
public class StaffLeaveDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtFromDate,txtToDate,txtType,txtDays,txtReason;
    private TextView txtAccept,txtReject;
    private String leaveId;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.leave_details);
    }

    @Override
    protected int setView() {
        return R.layout.activity_leave_details;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIds();
        setListeners();
        leaveId=getIntent().getStringExtra("leaveId");
        getLeaveDetails();
    }

    private void getIds() {
        txtType= (TextView) findViewById(R.id.txt_type);
        txtDays= (TextView) findViewById(R.id.txt_days);
        txtFromDate= (TextView) findViewById(R.id.txt_from);
        txtToDate= (TextView) findViewById(R.id.txt_to);
        txtReason= (TextView) findViewById(R.id.txt_reason);

        txtAccept= (TextView) findViewById(R.id.txt_approve);
        txtReject= (TextView) findViewById(R.id.txt_reject);
    }

    private void setListeners() {
        txtAccept.setOnClickListener(this);
        txtReject.setOnClickListener(this);
    }

    private void getLeaveDetails() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LeaveDetailsDataModel> call = apiService.getLeaveDetails(leaveId);
            call.enqueue(new Callback<LeaveDetailsDataModel>() {
                @Override
                public void onResponse(Call<LeaveDetailsDataModel> call, Response<LeaveDetailsDataModel> response) {
                    LeaveDetailsDataModel data = response.body();
                    U.hideProgress();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        LeaveDetailModel obj=data.getData();
                        txtDays.setText(obj.getTotalDay());
                        txtType.setText(obj.getLeaveType());
                        txtFromDate.setText(obj.getStartDate());
                        txtToDate.setText(obj.getEndDate());
                        txtReason.setText(obj.getReason());
                        if(obj.getStatus().equals(C.PENDING)){
                            txtAccept.setVisibility(View.VISIBLE);
                            txtReject.setVisibility(View.VISIBLE);
                        }else{
                            txtAccept.setVisibility(View.GONE);
                            txtReject.setVisibility(View.GONE);
                        }
                    }else {
                        U.toast(data.getMessage());
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<LeaveDetailsDataModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void rejectLeave() {
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
                        LeaveFragment.update=true;
                        finish();
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

    private void acceptLeave() {
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
                        LeaveFragment.update=true;
                        finish();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_approve:
                acceptLeave();
                break;

            case R.id.txt_reject:
                rejectLeave();
                break;
        }
    }
}
