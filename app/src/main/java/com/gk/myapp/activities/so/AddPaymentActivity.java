package com.gk.myapp.activities.so;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.activities.PaymentHistoryActivity;
import com.gk.myapp.adapters.PartyListAdapter;
import com.gk.myapp.adapters.SpinnerAdapter;
import com.gk.myapp.fragments.LeaveFragment;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.PartyListModel;
import com.gk.myapp.model.PartyModel;
import com.gk.myapp.model.PaymentType;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 27-08-2017.
 */
public class AddPaymentActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private final int CHEQUE = 1;
    private final int INTERNET_BANKING = 2;
    private final int CASH = 3;
    private int selectedPaymentMode = 0;
    private LinearLayout linChequeNo, linTransactionId;
    private EditText edAmount, edTransactionId, edChequeno;
    private Spinner spnPartyName, spnPaymentMode;
    private TextView txtDate;
    private Button btnAdd;

    private List<PartyModel> partyList;
    private List<PartyModel> paymentTypes;

    private PartyListAdapter partyListAdapter, paymentListAdapter;

    private String amount = "", transactionId = "", chequeNo = "", partyName = "", paymentMode = "", date = "";

    private String paymentNameArray[] = {"Cheque", "Cash", "NEFT", /*"Online",*/ "IMPS", "RTGS"};
    private String paymentIDArray[] = {"1", "3", "2", /*"4",*/ "5", "6"};

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.add_new_payment);
    }

    @Override
    protected int setView() {
        return R.layout.activity_add_payment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyList = new ArrayList<>();
        paymentTypes = new ArrayList<>();
        getIds();
        setListeners();

        getPartyList();
    }

    private void getIds() {
        linChequeNo = (LinearLayout) findViewById(R.id.lin_cheque_number);
        linTransactionId = (LinearLayout) findViewById(R.id.lin_transaction_id);
        edAmount = (EditText) findViewById(R.id.ed_amount);
        edTransactionId = (EditText) findViewById(R.id.ed_transaction_id);
        edChequeno = (EditText) findViewById(R.id.ed_cheque_no);
        txtDate = (TextView) findViewById(R.id.txt_date);
        spnPartyName = (Spinner) findViewById(R.id.spn_party_name);
        spnPaymentMode = (Spinner) findViewById(R.id.spn_payment_mode);
        btnAdd = (Button) findViewById(R.id.btn_add_payment);

        PartyModel obj = new PartyModel();
        obj.setPartyName(getString(R.string.select_payment_mode));
        obj.setPartyId("-1");
        paymentTypes.add(0, obj);

        for (int i = 0; i < paymentNameArray.length; i++) {
            PartyModel obj1 = new PartyModel();
            obj1.setPartyName(paymentNameArray[i]);
            obj1.setPartyId(paymentIDArray[i]);
            paymentTypes.add(obj1);
        }
        paymentListAdapter = new PartyListAdapter(getCont(), paymentTypes);
        spnPaymentMode.setAdapter(paymentListAdapter);


        partyListAdapter = new PartyListAdapter(getCont(), partyList);
        spnPartyName.setAdapter(partyListAdapter);

        setRightButton(true, R.mipmap.ic_history, new HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                Intent mIntent=new Intent(AddPaymentActivity.this,PaymentHistoryActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void setListeners() {
        txtDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        spnPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentMode = paymentTypes.get(position).getPartyId();
                if (position == 1) {
                    selectedPaymentMode = CHEQUE;
                    linChequeNo.setVisibility(View.VISIBLE);
                    linTransactionId.setVisibility(View.GONE);
                } else if (position == 2) {
                    selectedPaymentMode = CASH;
                    linChequeNo.setVisibility(View.GONE);
                    linTransactionId.setVisibility(View.GONE);
                } else if (position > 2 && position < 7) {
                    selectedPaymentMode = INTERNET_BANKING;
                    linChequeNo.setVisibility(View.GONE);
                    linTransactionId.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnPartyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                partyName = partyList.get(position).getUserId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddPaymentActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        dpd.setAccentColor(getResources().getColor(R.color.action_bar_color));
        dpd.setTitle("Select Date");
        dpd.show(getFragmentManager(), "Select Date");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
        m++;
        String day = d < 9 ? "0" + d : "" + d;
        String month = m < 9 ? "0" + m : "" + m;
        txtDate.setText(day + "-" + month + "-" + y);
    }

    private boolean checkFields() {
        amount = edAmount.getText().toString().trim();
        if (selectedPaymentMode == CHEQUE)
            transactionId = edChequeno.getText().toString().trim();
        else if (selectedPaymentMode == INTERNET_BANKING)
            transactionId = edTransactionId.getText().toString().trim();
        date = txtDate.getText().toString().trim();
        if (amount.equals("")) {
            showToast(getString(R.string.enter_amount));
            return false;
        } else if (paymentMode.equals("") || paymentMode.equals("-1")) {
            showToast(getString(R.string.select_payment_mode));
            return false;
        } else if (selectedPaymentMode == CHEQUE && transactionId.equals("")) {
            showToast(getString(R.string.enter_cheque_number));
            return false;
        } else if (selectedPaymentMode == INTERNET_BANKING && transactionId.equals("")) {
            showToast(getString(R.string.enter_transaction_id));
            return false;
        } else if (date.equals("")) {
            showToast(getString(R.string.select_date));
            return false;
        } else if (partyName.equals("") || partyName.equals("-1")) {
            showToast(getString(R.string.party_name));
            return false;
        }
        return true;
    }

    private void getPartyList() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            partyList.clear();
            PartyModel obj = new PartyModel();
            obj.setPartyId("-1");
            obj.setPartyName(getString(R.string.select_party_name));
            partyList.add(0, obj);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<PartyListModel> call = apiService.partyList(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<PartyListModel>() {
                @Override
                public void onResponse(Call<PartyListModel> call, Response<PartyListModel> response) {
                    PartyListModel data = response.body();
                    U.hideProgress();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        partyList.addAll(data.getData());
                    }else {
                        U.toast(data.getMessage());
                    }
                    partyListAdapter.notifyList(partyList);
                }

                @Override
                public void onFailure(Call<PartyListModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }


    private void addPayment() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.addPayment(getPreferences().getStringPref(P.USER_ID),amount,paymentMode,transactionId,date,partyName);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        edAmount.setText("");
                        edTransactionId.setText("");
                        edChequeno.setText("");
                        spnPartyName.setSelection(0);
                        spnPaymentMode.setSelection(0);
                        txtDate.setText("");

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_payment:
                if (checkFields()) {
                    addPayment();
                }
                break;


            case R.id.txt_date:
                showDatePicker();
                break;
        }
    }
}
