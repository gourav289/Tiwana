package com.gk.myapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.adapters.SelectAsmAdapter;
import com.gk.myapp.fragments.SelectSoActivity;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
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
public class SelectAsmActivity extends BaseActivity {
    private ListView lv;
    private ArrayList<SelectAsmModel> mList;
    private SelectAsmAdapter madp;

    private TextView txtTarget, txtAchieved, txtPending,txtHeading;

    private String year, half,title;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.select_asm);
    }

    @Override
    protected int setView() {
        return R.layout.activity_select_asm;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        year = getIntent().getStringExtra("year");
        half = getIntent().getStringExtra("half");
        title=getIntent().getStringExtra("title");
        getIds();
        setListeners();
        getAsm();
    }

    private void getIds() {
        lv = (ListView) findViewById(R.id.list_view);
        txtAchieved = (TextView) findViewById(R.id.txt_achieved_desc);
        txtPending = (TextView) findViewById(R.id.txt_pending_desc);
        txtTarget = (TextView) findViewById(R.id.txt_total_desc);
        txtHeading= (TextView) findViewById(R.id.txt_heading);
        txtHeading.setText(title);
        mList = new ArrayList<>();
        madp = new SelectAsmAdapter(this, mList);
        lv.setAdapter(madp);
    }

    private void setListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(SelectAsmActivity.this, SelectSoActivity.class);
                mIntent.putExtra("title",txtHeading.getText().toString());
                mIntent.putExtra("year", year);
                mIntent.putExtra("half", half);
                mIntent.putExtra("asm_id",mList.get(position).getId());
                startActivity(mIntent);
            }
        });
    }


    private void getAsm() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            mList.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<SelectAsmResponse> call = apiService.getHalfYearlyTarget(getPreferences().getStringPref(P.USER_ID), half, year);
            call.enqueue(new Callback<SelectAsmResponse>() {
                @Override
                public void onResponse(Call<SelectAsmResponse> call, Response<SelectAsmResponse> response) {
                    SelectAsmResponse data = response.body();
                    if (data.getStatus().equals("1")) {
                        txtTarget.setText(data.getData().getTarget()+ C.KG);
                        txtPending.setText(data.getData().getPending()+ C.KG);
                        txtAchieved.setText(data.getData().getAchieved()+ C.KG);
                        mList.addAll(data.getData().getAsmList());
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
