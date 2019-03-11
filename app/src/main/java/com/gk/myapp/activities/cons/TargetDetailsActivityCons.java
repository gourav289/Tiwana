package com.gk.myapp.activities.cons;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.adapters.ProductDetailAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.model.SelectAsmResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 19-01-2018.
 */
public class TargetDetailsActivityCons extends BaseActivity {
    private ListView lv;
    private ArrayList<ProductDetailModel> mList;
    private ProductDetailAdapter madp;

    private TextView txtTarget, txtAchieved, txtPending, txtHeading/*,txtName,txtLocation*/;
    private String year, month, title, soId, from;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.target_details);
    }

    @Override
    protected int setView() {
        return R.layout.activity_target_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIds();

        getProductDetails();
    }

    private void getIds() {
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        title = getIntent().getStringExtra("title");
        soId = getIntent().getStringExtra("so_id");
        txtAchieved = (TextView) findViewById(R.id.txt_achieved_desc);
        txtPending = (TextView) findViewById(R.id.txt_pending_desc);
        txtTarget = (TextView) findViewById(R.id.txt_total_desc);
        txtHeading = (TextView) findViewById(R.id.txt_heading);
        txtHeading.setText(title);

        lv = (ListView) findViewById(R.id.list_view);
        lv.setVisibility(View.GONE);
    }

    private void getProductDetails() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<SelectAsmResponse> call = apiService.getMonthlyData(getPreferences().getStringPref(P.USER_ID), month, year);
            call.enqueue(new Callback<SelectAsmResponse>() {
                @Override
                public void onResponse(Call<SelectAsmResponse> call, Response<SelectAsmResponse> response) {
                    SelectAsmResponse data = response.body();
                    if (data.getStatus().equals("1")) {
                        txtTarget.setText(data.getData().getTarget()+ C.KG);
                        txtPending.setText(data.getData().getPending()+ C.KG);
                        txtAchieved.setText(data.getData().getAchieved() + C.KG);

                    } else {
                        showToast(data.getMessage());
                    }

                    U.hideProgress();

                }

                @Override
                public void onFailure(Call<SelectAsmResponse> call, Throwable t) {
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
