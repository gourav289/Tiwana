package com.gk.myapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gk.myapp.R;
import com.gk.myapp.fragments.LeaveFragment;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 17-09-2017.
 */
public class AddAllowancesActivity extends BaseActivity {

    private EditText edDailyAllowances, edOtherAllowances;
    private Button btnSubmit;
    private String dailyAllow, otherAllow;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.add_allowance);
    }

    @Override
    protected int setView() {
        return R.layout.activity_add_allowances;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIds();
        setListeners();
    }

    private void getIds() {
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        edDailyAllowances = (EditText) findViewById(R.id.ed_daily_allowance);
        edOtherAllowances = (EditText) findViewById(R.id.ed_other_allowance);
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields())
                    addAllow();
            }
        });
    }

    private boolean checkFields() {
        dailyAllow = edDailyAllowances.getText().toString().trim();
        otherAllow = edOtherAllowances.getText().toString().trim();

        if (dailyAllow.equals("")) {
            showToast(getString(R.string.add_daily_allow));
            return false;
        }
        return true;
    }

    private void addAllow() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.saveAllowances(getPreferences().getStringPref(P.USER_ID), dailyAllow, otherAllow);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    finish();
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

}
