package com.gk.myapp.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.adapters.OrderHistoryAdapter;
import com.gk.myapp.adapters.OrderHistoryDetailsAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.OrderHistoryResponse;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 04-02-2018.
 */
public class OrderHistoryDetailsActivity extends BaseActivity {

    private ListView lv;
    private OrderHistoryDetailsAdapter mAdp;
    private List<OrderHistoryResponse.OrderDetails> mList;


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
        mList= (List<OrderHistoryResponse.OrderDetails>) getIntent().getSerializableExtra("list");
        getIds();
        setListeners();
    }

    private void getIds() {
        lv = (ListView) findViewById(R.id.list_view);
        mAdp=new OrderHistoryDetailsAdapter(this,mList);
        lv.setAdapter(mAdp);
    }

    private void setListeners() {

    }


}
