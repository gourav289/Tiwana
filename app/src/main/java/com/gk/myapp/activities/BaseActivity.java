package com.gk.myapp.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gk.myapp.R;
import com.gk.myapp.app.AppController;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

/**
 * Created by Gk on 11-12-2016.
 */
public abstract class BaseActivity extends Activity {

    private FrameLayout frmContainer;
    private ImageButton ibtnLeft, ibtnRight;
    private TextView txtTitle;
    private P mPreferences;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext=this;
        getIds();
    }

    private void getIds() {
        ibtnLeft = (ImageButton) findViewById(R.id.ibtn_back);
        ibtnRight = (ImageButton) findViewById(R.id.ibtn_right);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        frmContainer = (FrameLayout) findViewById(R.id.container);

        String headerTitle = setHeaderTitle();
        if (headerTitle != null)
            txtTitle.setText(setHeaderTitle());
        View view = getLayoutInflater().inflate(setView(), null);
        frmContainer.addView(view);

//        setLeftButton(true, R.mipmap.ic_back, new HeaderButtonClick() {
//            @Override
//            public void onHeaderButtonClicked(int id) {
//                finish();
//            }
//        });
        setRightButton(false, 0, null);

        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected abstract String setHeaderTitle();

    protected abstract int setView();

    protected void setLeftButton(boolean visibility, int icon, final HeaderButtonClick mListener) {
        if (visibility) {
            ibtnLeft.setVisibility(View.VISIBLE);
        } else {
            ibtnLeft.setVisibility(View.INVISIBLE);
//        }

            ibtnLeft.setImageResource(icon);

            ibtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onHeaderButtonClicked(v.getId());
                }
            });
        }
    }


    protected void setRightButton(boolean visibility, int icon, final HeaderButtonClick mListener) {
        if (visibility) {
            ibtnRight.setVisibility(View.VISIBLE);
        } else {
            ibtnRight.setVisibility(View.INVISIBLE);
        }

        ibtnRight.setImageResource(icon);

        ibtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onHeaderButtonClicked(v.getId());
            }
        });
    }

    protected void changeHeaderTitle(String title) {
        if (title != null)
            txtTitle.setText(title);
    }

    public interface HeaderButtonClick {
        public void onHeaderButtonClicked(int id);
    }

    protected Context getContext(){
        return this;
    }

    protected P getPreferences(){
        if(mPreferences==null)
            mPreferences=new P();
        return mPreferences;
    }

    protected Context getCont(){
        return mContext;
    }

    protected void showToast(String message){
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void showProgress(){
        U.showProgress(getCont());
    }

    protected void hideProgress(){
        U.hideProgress();
    }
}

