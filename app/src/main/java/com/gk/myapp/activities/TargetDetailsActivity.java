package com.gk.myapp.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.adapters.ProductDetailAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.model.SelectAsmModel;
import com.gk.myapp.model.SelectAsmResponse;
import com.gk.myapp.model.YearTargetResponse;
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
public class TargetDetailsActivity extends BaseActivity {
    private ListView lv;
    private ArrayList<ProductDetailModel> mList;
    private ProductDetailAdapter madp;

    private TextView txtTarget, txtAchieved, txtPending, txtHeading/*,txtName,txtLocation*/;
    private String year, half, title, soId, from;

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
        half = getIntent().getStringExtra("half");
        title = getIntent().getStringExtra("title");
        soId = getIntent().getStringExtra("so_id");
//        txtName= (TextView) findViewById(R.id.txt_name);
//        txtLocation= (TextView) findViewById(R.id.txt_location);
        txtAchieved = (TextView) findViewById(R.id.txt_achieved_desc);
        txtPending = (TextView) findViewById(R.id.txt_pending_desc);
        txtTarget = (TextView) findViewById(R.id.txt_total_desc);
        txtHeading = (TextView) findViewById(R.id.txt_heading);
        txtHeading.setText(title);

        lv = (ListView) findViewById(R.id.list_view);
        mList = new ArrayList<>();
        madp = new ProductDetailAdapter(this, mList);
        lv.setAdapter(madp);
    }

    private void getProductDetails() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<SelectAsmResponse> call = apiService.getTargetSo(getPreferences().getStringPref(P.USER_ID), half, year, soId);
            call.enqueue(new Callback<SelectAsmResponse>() {
                @Override
                public void onResponse(Call<SelectAsmResponse> call, Response<SelectAsmResponse> response) {
                    SelectAsmResponse data = response.body();
                    if (data.getStatus().equals("1")) {
                        txtTarget.setText(data.getData().getTarget()+ C.KG);
                        txtPending.setText(data.getData().getPending()+ C.KG);
                        txtAchieved.setText(data.getData().getAchieved()+ C.KG);
                        mList.addAll(data.getData().getProductList());
                        if(data.getData().getSoList().size()>0) {
//                            txtName.setText(data.getData().getSoList().get(0).getAsmName());
//                            txtLocation.setText(data.getData().getSoList().get(0).getLocation());
                        }
                    } else {
                        showToast(data.getMessage());
                    }

                    madp.notifyDataSetChanged();
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
