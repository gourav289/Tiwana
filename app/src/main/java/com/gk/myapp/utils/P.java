package com.gk.myapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gk.myapp.app.AppController;

public class P {
    public static final String EMP_CODE = "EMP_CODE";
    public static final String IMAGE = "IMAGE";
    public static final String NAME = "NAME";
    public static final String STATE_CODE = "STATE_CODE";
    public static final String STATE = "STATE";
    public static final String DESIGNATION = "DESIGNATION";
    SharedPreferences loginPref, permanentPref;
    // Login
    private final String LOGIN_PREF = "login_pref";
    private final String IS_LOGIN = "login";
    private final String LOGIN_DATA = "login_data";
    private final String PERMANENT_PREF = "PERMANENT_PREF";

    public static final String USER_TYPE = "user_type";

    //variables
    public static final String USER_ID = "USER_ID";
    public static final String EMAIL = "EMAIL";
    public static final String PROFILE_PIC = "PROFILE_PIC";
    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    private final String IS_REMEMBER = "IS_REMEMBER";
    public static final String JOB_STARTED = "JOB_STARTED";

    public static final String IMEI_NUMBER = "imei_number";
    public static final String JOB_STATUS = "JOB_STATUS";
    public static final String LOCATION_UPDATING = "LOCATION_UPDATING";

    public P() {
        loginPref = AppController.getInstance().getContext().getSharedPreferences(LOGIN_PREF,
                Context.MODE_PRIVATE);
        permanentPref = AppController.getInstance().getContext().getSharedPreferences(PERMANENT_PREF,
                Context.MODE_PRIVATE);
    }

    public P(Context mContext) {
        loginPref = mContext.getSharedPreferences(LOGIN_PREF,
                Context.MODE_PRIVATE);
        permanentPref = mContext.getSharedPreferences(PERMANENT_PREF,
                Context.MODE_PRIVATE);
    }

    //Is Login
    public void setLogin(boolean isLogin) {
        Editor editor = loginPref.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }

    public boolean getLogin() {
        return loginPref.getBoolean(IS_LOGIN, false);
    }


    //Login data
    public void setLoginData(String data) {
        Editor editor = loginPref.edit();
        editor.putString(LOGIN_DATA, data);
        editor.commit();
    }

    public String getLoginData() {
        return loginPref.getString(LOGIN_DATA, "");
    }

    //user id
    public void setStringPref(String key, String data) {
        Editor editor = permanentPref.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public String getStringPref(String type) {
        return permanentPref.getString(type, "");
    }

    public void setBooleanPref(String key, boolean data) {
        Editor editor = loginPref.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public boolean getBooleanPref(String type) {
        return loginPref.getBoolean(type, false);
    }


    public void setIsRemember(boolean data) {
        Editor editor = permanentPref.edit();
        editor.putBoolean(IS_REMEMBER, data);
        editor.commit();
    }

    public boolean isRemember() {
        return permanentPref.getBoolean(IS_REMEMBER, false);
    }

    public int getIntPreference(String type) {
        return loginPref.getInt(type, C.JOB_STOPPED);
    }

    public void setIntPref(String key, int data) {
        Editor editor = loginPref.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    //clear login data
    public void clearLoginData() {
        Editor editor = loginPref.edit();
        editor.clear();
        editor.commit();
    }

}
