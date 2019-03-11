package com.gk.myapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.gk.myapp.R;
import com.gk.myapp.adapters.CrearteTargetProductAdapter;
import com.gk.myapp.adapters.OrderHistoryAdapter;
import com.gk.myapp.adapters.SpinnerAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.OrderHistoryResponse;
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.model.SelectAsmResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 04-02-2018.
 */
public class OrderHistoryActivity extends BaseActivity implements OrderHistoryAdapter.CancelOrderListener {

    private ListView lv;
    private OrderHistoryAdapter mAdp;
    private List<OrderHistoryResponse.OrderHistoryList> mList;
    private int page = 1;
    private int totalRecords=0;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.order_history);
    }

    @Override
    protected int setView() {
        return R.layout.activity_order_history;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIds();
        setListeners();
    }

    private void getIds() {
        lv = (ListView) findViewById(R.id.list_view);
        mList = new ArrayList<>();
        mAdp = new OrderHistoryAdapter(getCont(), mList, OrderHistoryActivity.this);
        lv.setAdapter(mAdp);
        getOrderList();
    }

    private void setListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent(OrderHistoryActivity.this, OrderHistoryDetailsActivity.class);
                mIntent.putExtra("list", (Serializable) mList.get(i).getOrderDetail());
                startActivity(mIntent);
            }
        });
    }


    private void getOrderList() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            if (page == 1)
                mList.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<OrderHistoryResponse> call = apiService.getOrderHistory(getPreferences().getStringPref(P.USER_ID), "" + page);
            call.enqueue(new Callback<OrderHistoryResponse>() {
                @Override
                public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                    OrderHistoryResponse data = response.body();
                    totalRecords=Integer.parseInt(data.getTotalRecords());
                    if (data.getStatus().equals("1")) {
                        mList.addAll(data.getData());
                    } else {
                        showToast(data.getMessage());
                    }
                    mAdp.notifyDataChanged(totalRecords,page,mList);
                    page++;
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void cancelOrderWs(String orderId) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            mList.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.cancelOrder(getPreferences().getStringPref(P.USER_ID), orderId);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equals("1")) {
                        page=1;
                        getOrderList();
                    } else {
                        U.hideProgress();
                    }
                    showToast(data.getMessage());
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
    public void onCancelOrder(String orderId) {
        cancelOrderWs(orderId);
    }

    @Override
    public void onLoadMore() {
        getOrderList();
    }
}
