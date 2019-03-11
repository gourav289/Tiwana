package com.gk.myapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.activities.cons.EnquiryActivity;
import com.gk.myapp.activities.cons.SignUpActivity;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.UserDetails;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.MarshMallowPermission;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 17-12-2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private final String TAG = "login";
    private Context mContext;
    private Button btnLogin;
    private String empCode, password;
    private EditText edUserName, edPassword;
    private CheckBox chkRemember;
    private CheckBox chkDealer;
    private boolean isRemember = false;
    private P mP;

    private TextView txtSignUp;
    private TextView txtEnquiry;

    private String imei = "";

    private String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mP = new P();
        if (mP.getLogin() && mP.getStringPref(P.USER_TYPE).equalsIgnoreCase(C.OTHER_USER)) {
            Intent mIntent = new Intent(this, ProfileActivity.class);
            startActivity(mIntent);
            finish();
        } else if (mP.getLogin() && (mP.getStringPref(P.USER_TYPE).equalsIgnoreCase(C.GM) || mP.getStringPref(P.USER_TYPE).equals(C.ASM) || mP.getStringPref(P.USER_TYPE).equals(C.SO))) {
            Intent mIntent = new Intent(this, BaseActivityTabs.class);
            startActivity(mIntent);
            finish();
        } else if (mP.getLogin() && mP.getStringPref(P.USER_TYPE).equalsIgnoreCase(C.CONSUMER)) {
            Intent mIntent = new Intent(this, BaseActivityTabsCons.class);
            startActivity(mIntent);
            finish();
        }

        setContentView(R.layout.activity_login);
        mContext = this;
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        imei = telephonyManager.getDeviceId();

        getIds();
        setListeners();
    }

    private void getIds() {

        btnLogin = (Button) findViewById(R.id.btn_login);
        edUserName = (EditText) findViewById(R.id.ed_username);
        edPassword = (EditText) findViewById(R.id.ed_password);
        chkRemember = (CheckBox) findViewById(R.id.chk_remember);
        chkRemember.setChecked(mP.isRemember());
        if (chkRemember.isChecked()) {
            edUserName.setText(mP.getStringPref(P.EMP_CODE));
            edPassword.setText(mP.getStringPref(P.PASSWORD));
            isRemember = true;
        }

        txtSignUp = (TextView) findViewById(R.id.txt_signup);
        txtEnquiry = (TextView) findViewById(R.id.txt_enquiry);

        chkDealer = (CheckBox) findViewById(R.id.chk_dealer);
    }

    private void setListeners() {
        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtEnquiry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
//                Intent mIntent = new Intent(LoginActivity.this, BaseActivityTabs.class);
//                startActivity(mIntent);
                if ((android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)) {
                    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(LoginActivity.this);
                    if (marshMallowPermission.checkPermissionForImeiNumber()) {
                        try {
                            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            mP.setStringPref(P.IMEI_NUMBER, telephonyManager.getDeviceId());
                            empCode = edUserName.getText().toString().trim();
                            password = edPassword.getText().toString().trim();
                            isRemember = chkRemember.isChecked();
                            if (checkFields())
                                webserviceCall();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        marshMallowPermission.requestPermissionForImeiNumber();
                    }
                } else {
                    try {
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        mP.setStringPref(P.IMEI_NUMBER, telephonyManager.getDeviceId());
                        empCode = edUserName.getText().toString().trim();
                        password = edPassword.getText().toString().trim();
                        isRemember = chkRemember.isChecked();
                        if (checkFields())
                            webserviceCall();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.txt_enquiry:
                Intent enquiry = new Intent(LoginActivity.this, EnquiryActivity.class);
                startActivity(enquiry);
                break;

            case R.id.txt_signup:
                Intent signUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUp);
                break;
        }
    }

    private void getToken(){
        token= FirebaseInstanceId.getInstance().getToken();
    }

    private boolean checkFields() {
       getToken();
        if (empCode.equals("")) {
            U.toast(getString(R.string.enter_code));
            return false;
        } else if (password.equals("")) {
            U.toast(getString(R.string.enter_password));
            return false;
        } else if(token.equals("")){
            U.toast(getString(R.string.getting_token));
            return false;
        }else {
            return true;
        }

    }

    private void webserviceCall() {
        String dealer = "No";
        if (chkDealer.isChecked())
            dealer = "Yes";
        if (U.isConnectedToInternet(mContext)) {
            U.showProgress(mContext);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<UserDetails> call = apiService.userLogin(empCode, password, token, "android", dealer,mP.getStringPref(P.IMEI_NUMBER));
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                    UserDetails data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        mP.setLogin(true);
                        mP.setStringPref(P.USER_ID, data.getUserData().getUserId());
                        mP.setStringPref(P.EMP_CODE, empCode);
                        mP.setStringPref(P.PASSWORD, data.getUserData().getVisiblePassword());
                        mP.setStringPref(P.USER_TYPE, data.getUserData().getRole());
                        mP.setStringPref(P.IMAGE, data.getUserData().getImage());
                        mP.setStringPref(P.STATE_CODE, data.getUserData().getAreaCode());
                        mP.setStringPref(P.STATE,data.getUserData().getState());
                        mP.setStringPref(P.NAME, data.getUserData().getName());
                        mP.setIsRemember(isRemember);

                        if (data.getUserData().getRole().equalsIgnoreCase(C.OTHER_USER)) {
                            Intent mIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(mIntent);
                        } else if (data.getUserData().getRole().equalsIgnoreCase(C.GM) || mP.getStringPref(P.USER_TYPE).equals(C.ASM) || mP.getStringPref(P.USER_TYPE).equals(C.SO)) {
                            Intent mIntent = new Intent(LoginActivity.this, BaseActivityTabs.class);
                            startActivity(mIntent);
                        } else if (data.getUserData().getRole().equalsIgnoreCase(C.CONSUMER)) {
                            Intent mIntent = new Intent(LoginActivity.this, BaseActivityTabsCons.class);
                            startActivity(mIntent);
                            finish();
                        }
                        finish();

                    } else {
                        U.toast(data.getMessage());
                    }
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.IMEI_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    mP.setStringPref(P.IMEI_NUMBER, telephonyManager.getDeviceId());
                    empCode = edUserName.getText().toString().trim();
                    password = edPassword.getText().toString().trim();
                    isRemember = chkRemember.isChecked();
                    if (checkFields())
                        webserviceCall();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
