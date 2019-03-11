package com.gk.myapp.activities.cons;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.adapters.StateSpinnerAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.StateResponseModel;
import com.gk.myapp.utils.U;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 09-09-2017.
 */
public class EnquiryActivity extends BaseActivity implements View.OnClickListener {
    private final String SELECT_STATE = "Select state";
    private List<StateResponseModel.StateDetails> stateList;
    private StateSpinnerAdapter stateAdapter;

    private EditText edName, edPartyName, edPhone, edAddress, edCity, edMessage;
    private Spinner spnState;
    private Button btnSubmit;

    private String name, partyName, phone, city, state, address, message;

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.enquiry);
    }

    @Override
    protected int setView() {
        return R.layout.activity_enquiry;
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
        edAddress = (EditText) findViewById(R.id.ed_address);
        edPhone = (EditText) findViewById(R.id.ed_phone);
        spnState = (Spinner) findViewById(R.id.spn_state);
        edPartyName = (EditText) findViewById(R.id.ed_party_name);
        edMessage = (EditText) findViewById(R.id.ed_message);
        edCity = (EditText) findViewById(R.id.ed_city);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    private void setListener() {
        btnSubmit.setOnClickListener(this);

        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = stateList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (checkFields()) {
                    signUp();
                }
                break;
        }
    }

    private boolean checkFields() {
        name = edName.getText().toString().trim();
        partyName = edPartyName.getText().toString().trim();
        message = edMessage.getText().toString().trim();
        city = edCity.getText().toString().trim();
        address = edAddress.getText().toString().trim();
        phone = edPhone.getText().toString().trim();

        if (name.equals("")) {
            showToast(getString(R.string.enter_name));
            return false;
        }else if (name.length()<3) {
            showToast(getString(R.string.name_validation));
            return false;
        } else if (partyName.equals("")) {
            showToast(getString(R.string.enter_party_name));
            return false;
        } if (partyName.length()<3) {
            showToast(getString(R.string.party_name_validation));
            return false;
        }else if (phone.equals("")) {
            showToast(getString(R.string.enter_phone));
            return false;
        } else if (phone.length() != 10) {
            showToast(getString(R.string.phone_length));
            return false;
        } else if (address.equals("")) {
            showToast(getString(R.string.enter_address));
            return false;
        } else if (address.length() < 3) {
            showToast(getString(R.string.address_validation));
            return false;
        }else if (city.equals("")) {
            showToast(getString(R.string.enter_city));
            return false;
        } else if (city.length() < 3) {
            showToast(getString(R.string.city_validation));
            return false;
        }else if (state.equals(SELECT_STATE)) {
            showToast(getString(R.string.enter_state));
            return false;
        } else if (message.equals("")) {
            showToast(getString(R.string.enter_message));
            return false;
        }
        return true;
    }

    private void signUp() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.submitEnquiry(name, partyName, phone, address, city, state, message);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    if (data.getSuccess().equalsIgnoreCase("1")) {

                        finish();

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
}
