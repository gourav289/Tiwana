package com.gk.myapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gk.myapp.R;
import com.gk.myapp.utils.U;

/**
 * Created by Gk on 11-12-2016.
 */
public class DummySecActivity extends BaseActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIds();
    }


    private void getIds() {
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected String setHeaderTitle() {
        return "My sec Act";
    }

    @Override
    protected int setView() {
        return R.layout.dummy2;
    }

    @Override
    protected void setRightButton(boolean visibility, int icon, HeaderButtonClick mListener) {
        super.setRightButton(true, R.mipmap.ic_attachment, new HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                U.toast("My toast show");
            }
        });
    }
}
