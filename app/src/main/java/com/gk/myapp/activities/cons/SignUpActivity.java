package com.gk.myapp.activities.cons;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.adapters.StateSpinnerAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.interfaces.YesNoDialogConfirmation;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.StateResponseModel;
import com.gk.myapp.utils.U;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 07-09-2017.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener, YesNoDialogConfirmation, DatePickerDialog.OnDateSetListener {
    private final String SELECT_STATE = "Plant name";
    private List<StateResponseModel.StateDetails> stateList;
    private StateSpinnerAdapter stateAdapter;

    private TextView edAge;
    private EditText edName, edPassword, edEmail, edCompanyName, edPhone, edAddress, edCity, edAadharNo, edFatherName;
    private Spinner spnState;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Button btnSignUp;

    private String name, email, password, age, gender, companyName, phone, city, state, address, fatherName;
    private String aadharNumber;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.btn_signup);
    }

    @Override
    protected int setView() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateList = new ArrayList<>();

        getId();
        setListener();
        getStateList();
    }

    private void getId() {
        edName = (EditText) findViewById(R.id.ed_name);
        edPassword = (EditText) findViewById(R.id.ed_password);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edAge = (TextView) findViewById(R.id.ed_age);
        edCompanyName = (EditText) findViewById(R.id.ed_company_name);
        radioGroup = (RadioGroup) findViewById(R.id.rad_group);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        edCity = (EditText) findViewById(R.id.ed_city);
        edAddress = (EditText) findViewById(R.id.ed_address);
        edPhone = (EditText) findViewById(R.id.ed_phone);
        edAadharNo = (EditText) findViewById(R.id.ed_adhar_no);
        spnState = (Spinner) findViewById(R.id.spn_state);
        edFatherName = (EditText) findViewById(R.id.ed_father_name);
    }

    private void setListener() {
        btnSignUp.setOnClickListener(this);

        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = stateList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker("from");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                if (checkFields()) {
                    signUp();
                }
                break;
        }
    }

    private boolean checkFields() {
        name = edName.getText().toString().trim();
        email = edEmail.getText().toString().trim();
        password = edPassword.getText().toString().trim();
        age = edAge.getText().toString().trim();
        companyName = edCompanyName.getText().toString().trim();
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(id);
        gender = radioButton.getText().toString();
        city = edCity.getText().toString().trim();
        address = edAddress.getText().toString().trim();
        phone = edPhone.getText().toString().trim();
        aadharNumber = edAadharNo.getText().toString().trim();
        fatherName = edFatherName.getText().toString().trim();

        if (name.equals("")) {
            showToast(getString(R.string.enter_name));
            return false;
        } else if (name.length() < 3) {
            showToast(getString(R.string.name_validation));
            return false;
        } else if (fatherName.equals("")) {
            showToast(getString(R.string.enter_father_name));
            return false;
        } else if (fatherName.length() < 3) {
            showToast(getString(R.string.father_name_validation));
            return false;
        } else if (email.equals("")) {
            showToast(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(email)) {
            showToast(getString(R.string.enter_valid_email));
            return false;
        } else if (password.equals("")) {
            showToast(getString(R.string.enter_password));
            return false;
        } else if (password.length() < 6) {
            showToast(getString(R.string.valid_password));
            return false;
        } else if (phone.equals("")) {
            showToast(getString(R.string.enter_phone));
            return false;
        } else if (phone.length() != 10) {
            showToast(getString(R.string.phone_length));
            return false;
        } else if (age.equals("")) {
            showToast(getString(R.string.enter_age));
            return false;
        } else if (address.equals("")) {
            showToast(getString(R.string.enter_address));
            return false;
        } else if (address.length() < 3) {
            showToast(getString(R.string.address_validation));
            return false;
        } else if (city.equals("")) {
            showToast(getString(R.string.enter_city));
            return false;
        } else if (city.length() < 3) {
            showToast(getString(R.string.city_validation));
            return false;
        } else if (state.equals(SELECT_STATE)) {
            showToast(getString(R.string.enter_state));
            return false;
        } else if (aadharNumber.equals("")) {
            showToast(getString(R.string.enter_aadhar_no));
            return false;
        } else if (aadharNumber.length() != 12) {
            showToast(getString(R.string.enter_valid_aadhar));
            return false;
        } else if (companyName.equals("")) {
            showToast(getString(R.string.enter_company_name));
            return false;
        } else if (companyName.length() < 3) {
            showToast(getString(R.string.company_validation));
            return false;
        }
        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void signUp() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.signUp(name, fatherName, email, password, age, companyName, gender, phone, address, city, state, aadharNumber, companyName);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        U.commonDialog(getCont(), SignUpActivity.this, "", getString(R.string.sign_up_msg), 0, "signup");

                    } else {
                        U.toast(data.getMessage());
                    }
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }


    private void getStateList() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            StateResponseModel.StateDetails obj = new StateResponseModel().new StateDetails(SELECT_STATE, SELECT_STATE);
            stateList.add(0, obj);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<StateResponseModel> call = apiService.stateList();
            call.enqueue(new Callback<StateResponseModel>() {
                @Override
                public void onResponse(Call<StateResponseModel> call, Response<StateResponseModel> response) {
                    StateResponseModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        stateList.addAll(data.getData());

                    } else {
                        U.toast(data.getMessage());
                    }
                    stateAdapter = new StateSpinnerAdapter(getCont(), stateList);
                    spnState.setAdapter(stateAdapter);

                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<StateResponseModel> call, Throwable t) {
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
    public void onYesClicked(int pos, String type) {
        if (type.equals("signup")) {
            finish();
        }
    }

    private void showDatePicker(String from) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                SignUpActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar max=Calendar.getInstance();
        max.add(Calendar.MONTH,-18);
        dpd.setMaxDate(max);
        dpd.setAccentColor(getResources().getColor(R.color.action_bar_color));
        dpd.setTitle("Select Date");
        dpd.show(getFragmentManager(), "Select Date");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
        m++;
        String day = d < 10 ? "0" + d : "" + d;
        String month = m < 10 ? "0" + m : "" + m;
        edAge.setText(day + "/" + month + "/" + y);
//        try {
//            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
//            Date date1 = myFormat.parse(txtFromDate.getText().toString());
//            Date date2 = myFormat.parse(txtToDate.getText().toString());
//            long diff = date2.getTime() - date1.getTime();
//            int da = (int) (diff / (1000 * 60 * 60 * 24));
//            days = "" + (da + 1);
//            txtDays.setText(days);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }
}
