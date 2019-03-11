package com.gk.myapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import com.gk.myapp.R;
import com.gk.myapp.adapters.CrearteTargetProductAdapter;
import com.gk.myapp.adapters.SpinnerAdapter;
import com.gk.myapp.model.ProductDetailModel;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class CreateTargetActivity extends BaseActivity {

    private ListView lv;
    private CrearteTargetProductAdapter mAdp;
    private ArrayList<ProductDetailModel> mList;

    private Spinner spnYear, spnMonth;
    private String[] montharr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private ArrayList<String> mListMonth, mListYear;

    private SpinnerAdapter yearAdp, monthAdp;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.create_target);
    }

    @Override
    protected int setView() {
        return R.layout.fragment_create_target;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListYear = new ArrayList<>();
        mListMonth = new ArrayList<>();
        for (int i = 0; i < montharr.length; i++) {
            mListMonth.add(montharr[i]);
        }
        for (int i = 1900; i < 2100; i++) {
            mListYear.add("" + i);
        }

        mListMonth.add(0, "Month");
        mListYear.add(0, "Year");
        monthAdp = new SpinnerAdapter(this, mListMonth);
        yearAdp = new SpinnerAdapter(this, mListYear);
        getIds();
        setListeners();
    }

    private void getIds() {
        lv = (ListView) findViewById(R.id.list_view);
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductDetailModel obj = new ProductDetailModel();
            obj.setQuantityKg("Quantity in KG");
            obj.setQuantityNo("Quantity in Number");
            mList.add(obj);
        }

        mAdp = new CrearteTargetProductAdapter(this, mList);
        View v = getLayoutInflater().inflate(R.layout.create_target_footer, null);
        spnMonth = (Spinner) v.findViewById(R.id.spn_month);
        spnYear = (Spinner) v.findViewById(R.id.spn_year);
        spnMonth.setAdapter(monthAdp);
        spnYear.setAdapter(yearAdp);
        lv.addFooterView(v);
        lv.setAdapter(mAdp);

        setRightButton(true, R.mipmap.ic_history, new HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                Intent mIntent = new Intent(CreateTargetActivity.this, PaymentHistoryActivity.class);

                startActivity(mIntent);
            }
        });
    }

    private void setListeners() {

    }
}
