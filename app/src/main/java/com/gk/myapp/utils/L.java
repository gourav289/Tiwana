package com.gk.myapp.utils;

import android.util.Log;

/**
 * Created by abc on 5/10/2016.
 */
public class L {
    public static void e(String tag,String message){
        Log.e(tag, message);
    }
    public static void i(String tag,String message){
        Log.i(tag,message);
    }
    public static void d(String tag,String message){
        Log.d(tag,message);
    }
}
