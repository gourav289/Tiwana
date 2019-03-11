package com.gk.myapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.adapters.SelectAsmAdapter;
import com.gk.myapp.model.SelectAsmModel;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class DistributeTargetActivity extends BaseActivity {
    private ListView lv;
    private ArrayList<SelectAsmModel> mList;
    private SelectAsmAdapter madp;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.distribute_target);
    }

    @Override
    protected int setView() {
        return R.layout.activity_distribute_target;
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
        madp = new SelectAsmAdapter(this, mList);
        lv.setAdapter(madp);
    }

    private void setListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(DistributeTargetActivity.this, CreateTargetActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
