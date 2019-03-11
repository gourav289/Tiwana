package com.gk.myapp.activities.cons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.activities.so.AddPaymentActivity;
import com.gk.myapp.adapters.PaymentHistoryAdapter;
import com.gk.myapp.adapters.PaymentHistoryCustAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.PaymentHistoryModel;
import com.gk.myapp.model.PaymentHistoryResponse;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 12-09-2017.
 */
public class PaymentHistoryCustActivity extends BaseActivity {
    private ListView lv;
    private ArrayList<PaymentHistoryModel> mList;
    private PaymentHistoryCustAdapter mAdp;
    private Context mContext;

    public static boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getIds();
        mList = new ArrayList<>();
        getPaymentHistory();

    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.payment_history_title);
    }

    @Override
    protected int setView() {
        return R.layout.activity_payment_history;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (update) {
            update = false;
            getPaymentHistory();
        }
    }

    private void getIds() {
        lv = (ListView) findViewById(R.id.list_view);

    }

    private void getPaymentHistory() {
        if (U.isConnectedToInternet(mContext)) {
            U.showProgress(mContext);
            mList.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<PaymentHistoryResponse> call = apiService.paymentHistory(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<PaymentHistoryResponse>() {
                @Override
                public void onResponse(Call<PaymentHistoryResponse> call, Response<PaymentHistoryResponse> response) {
                    PaymentHistoryResponse data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        mList.addAll(data.getData());
                    } else {
                        U.toast(data.getMessage());
                    }

                    mAdp = new PaymentHistoryCustAdapter(mContext, mList);
                    lv.setAdapter(mAdp);
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<PaymentHistoryResponse> call, Throwable t) {
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }
}
