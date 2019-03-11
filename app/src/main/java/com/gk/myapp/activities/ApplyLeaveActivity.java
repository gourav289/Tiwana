package com.gk.myapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.adapters.SpinnerAdapter;
import com.gk.myapp.fragments.LeaveFragment;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.UserDetails;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 18-12-2016.
 */
public class ApplyLeaveActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

//    private Spinner spnNoOfDays;
    private SpinnerAdapter spnDaysAdp;
    private ArrayList<String> daysList;
    private TextView txtFromDate, txtToDate,txtDays;
    private Button btnApply;
    private EditText edReason;

    private RadioButton radioButton;
    private RadioGroup radioGroup;

    private String dateSelectedFrom = "";
    private String reason, dateFrom, dateTo, days, leaveType;
    Calendar cal=null;
    @Override
    protected String setHeaderTitle() {
        return getString(R.string.apply_leave);
    }

    @Override
    protected int setView() {
        return R.layout.activity_apply_leave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cal=Calendar.getInstance();
        getIds();
        setListeners();
        U.hideKeyboard(this);
    }

    private void getIds() {
//        spnNoOfDays = (Spinner) findViewById(R.id.spn_no_of_days);
        txtFromDate = (TextView) findViewById(R.id.txt_from);
        txtToDate = (TextView) findViewById(R.id.txt_to);
        btnApply = (Button) findViewById(R.id.btn_apply);
        edReason = (EditText) findViewById(R.id.ed_reason);
        radioGroup = (RadioGroup) findViewById(R.id.rad_group);
        txtDays= (TextView) findViewById(R.id.txt_days);

        daysList = new ArrayList<>();
        for (int i = 0; i <= 31; i++) {
            daysList.add("" + (i));
        }
//        spnDaysAdp = new SpinnerAdapter(this, daysList);
//        spnNoOfDays.setAdapter(spnDaysAdp);
//
//        spnNoOfDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                days = daysList.get(position);
//                txtFromDate.setText("");
//                txtToDate.setText("");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void setListeners() {
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    private void showDatePicker(String from) {
        dateSelectedFrom = from;
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ApplyLeaveActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.action_bar_color));
        dpd.setTitle("Select Date");
        dpd.show(getFragmentManager(), "Select Date");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
        m++;
        String day = d < 9 ? "0" + d : "" + d;
        String month = m < 9 ? "0" + m : "" + m;
//        int da=Integer.parseInt(days)-1;
//        cal.set(Calendar.MONTH,m-1);
//        cal.set(Calendar.YEAR, y);
//        cal.set(Calendar.DAY_OF_MONTH,d);
//        cal.add(Calendar.DAY_OF_MONTH, da);
//
//        SimpleDateFormat smf=new SimpleDateFormat("dd-MM-yyyy");
//        String toDt=smf.format(cal.getTimeInMillis());

        if (dateSelectedFrom.equals("from")) {
            txtFromDate.setText(day + "-" + month + "-" + y);
            txtToDate.setText("");
        }
        else {
            txtToDate.setText(day + "-" + month + "-" + y);
            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date1 = myFormat.parse(txtFromDate.getText().toString());
                Date date2 = myFormat.parse(txtToDate.getText().toString());
                long diff = date2.getTime() - date1.getTime();
                int da = (int)(diff / (1000*60*60*24));
                days=""+(da+1);
                txtDays.setText(days);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyLeaveWebservice() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.applyLeave(getPreferences().getStringPref(P.USER_ID), reason, getPreferences().getStringPref(P.USER_TYPE), dateFrom,dateTo,days,leaveType);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        LeaveFragment.update=true;
                        finish();

                    }
                        U.toast(data.getMessage());

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

    private boolean checkFields() {
        reason = edReason.getText().toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        leaveType = radioButton.getText().toString();
        dateFrom = txtFromDate.getText().toString();
        dateTo = txtToDate.getText().toString();
     /*  if (days.equals("0")) {
            showToast(getString(R.string.select_days));
            return false;
        } else */if (leaveType.equals("")) {
            showToast(getString(R.string.select_leave_type));
            return false;
        } else if (dateFrom.equals("")) {
            showToast(getString(R.string.select_from_date));
            return false;
        } else if (dateTo.equals("")) {
            showToast(getString(R.string.select_to_date));
            return false;
        }else if(reason.equals("")) {
            showToast(getString(R.string.enter_reason));
            return false;
        }


        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_from:

//                if(days.equals("0"))
//                    showToast(getString(R.string.select_days));
//                else
                    showDatePicker("from");
                break;

            case R.id.txt_to:
                showDatePicker("to");
                break;

            case R.id.btn_apply:
                if (checkFields()) {
                    if (U.isConnectedToInternet(getCont())) {
                        applyLeaveWebservice();
                    }
                }
                break;
        }
    }
}
