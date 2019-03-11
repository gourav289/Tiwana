package com.gk.myapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.LeaveDetailModel;
import com.gk.myapp.model.LeaveDetailsDataModel;
import com.gk.myapp.utils.U;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 12-08-2017.
 */
public class MyLeaveDetailsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout linBottomBtn;
    private TextView txtFromDate, txtToDate, txtType, txtDays, txtReason;

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
        String leaveId = getIntent().getStringExtra("leaveId");
        getLeaveDetails(leaveId);
    }

    private void getIds() {
        linBottomBtn = (LinearLayout) findViewById(R.id.lin_bottom_btns);
        linBottomBtn.setVisibility(View.GONE);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtDays = (TextView) findViewById(R.id.txt_days);
        txtFromDate = (TextView) findViewById(R.id.txt_from);
        txtToDate = (TextView) findViewById(R.id.txt_to);
        txtReason = (TextView) findViewById(R.id.txt_reason);
    }

    private void setListeners() {

    }

    private void getLeaveDetails(String leaveId) {
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
                        LeaveDetailModel obj = data.getData();
                        txtDays.setText(obj.getTotalDay());
                        txtType.setText(obj.getLeaveType());
                        txtFromDate.setText(obj.getStartDate());
                        txtToDate.setText(obj.getEndDate());
                        txtReason.setText(obj.getReason());
                    } else {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
