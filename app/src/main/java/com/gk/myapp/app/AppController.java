package com.gk.myapp.app;

/**
 * Created by 123 on 12/1/2015.
 */

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.gk.myapp.utils.MarshMallowPermission;
import com.gk.myapp.utils.P;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();


    private static AppController mInstance;
    private static int screenW=0,screenH=0;

    private static String language=null;

    private P mPrefrence;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mPrefrence=new P();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private static Context toastContext;

    public Context getContext(){
        if(toastContext==null)
            toastContext=this;
        return toastContext;
    }


    public int getScreenWidth(){
            if(screenW==0) {
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                final DisplayMetrics displayMetrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(displayMetrics);
                screenW= displayMetrics.widthPixels;
            }
            return screenW;
        }

    public int getScreenHeight(){
        if(screenH==0) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            screenH = displayMetrics.heightPixels;
        }
        return screenH;
    }

}