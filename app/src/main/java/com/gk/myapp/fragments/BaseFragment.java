package com.gk.myapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gk.myapp.utils.P;

/**
 * Created by Gk on 11-12-2016.
 */
public abstract class BaseFragment extends Fragment {

    private P mPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setView(),container,false);
    }

    protected P getPreferences(){
        if(mPreferences==null)
            mPreferences=new P();
        return mPreferences;
    }
    protected abstract int setView();

    protected Context getCont(){
        return getActivity();
    }

    protected void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
