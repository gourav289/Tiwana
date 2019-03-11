package com.gk.myapp.activities.cons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.activities.PaymentHistoryActivity;
import com.gk.myapp.adapters.CartConsAdapter;
import com.gk.myapp.adapters.PartyListAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.interfaces.YesNoDialogConfirmation;
import com.gk.myapp.model.CartListResponse;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.PartyListModel;
import com.gk.myapp.model.PartyModel;
import com.gk.myapp.model.ProductListResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 10-09-2017.
 */
public class CartConsActivity extends BaseActivity implements View.OnClickListener, CartConsAdapter.DeleteCartClick, YesNoDialogConfirmation {
    private ListView lv;
    private List<CartListResponse.CartDetails> productList;
    private CartConsAdapter mAdp;

    private Button btnPlaceOrder;

    private Spinner spnPartyName;
    private List<PartyModel> partyList;
    private PartyListAdapter partyListAdapter;
    private String partyName;
    private String createdFor;
    private String outstandingAmt="";

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.place_order);
    }

    @Override
    protected int setView() {
        return R.layout.activity_cart_cons;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<>();
        partyList = new ArrayList<>();
        getIds();
        setListener();

        getCart();
    }

    private void getIds() {
        lv = (ListView) findViewById(R.id.list_view);
        btnPlaceOrder = (Button) findViewById(R.id.btn_place_order);

        createdFor = getPreferences().getStringPref(P.USER_ID);

        if (getPreferences().getStringPref(P.USER_TYPE).equals(C.SO)) {
            spnPartyName = (Spinner) findViewById(R.id.spn_party_name);
            spnPartyName.setVisibility(View.VISIBLE);
            partyListAdapter = new PartyListAdapter(getCont(), partyList);
            spnPartyName.setAdapter(partyListAdapter);

            spnPartyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    partyName = partyList.get(position).getUserId();
                    outstandingAmt=partyList.get(position).getOutstandingPayment();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            getPartyList();

        }


    }

    private void setListener() {
        btnPlaceOrder.setOnClickListener(this);
    }

    private void getCart() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CartListResponse> call = apiService.getCart(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<CartListResponse>() {
                @Override
                public void onResponse(Call<CartListResponse> call, Response<CartListResponse> response) {
                    CartListResponse data = response.body();
                    productList.clear();
                    U.hideProgress();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        productList.addAll(data.getData());
                    } else {
                        U.toast(data.getMessage());
                    }
                    mAdp = new CartConsAdapter(CartConsActivity.this, productList, CartConsActivity.this);
                    lv.setAdapter(mAdp);
                }

                @Override
                public void onFailure(Call<CartListResponse> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void deleteCart(String cartId) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.deleteCart(getPreferences().getStringPref(P.USER_ID), cartId);
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();

                    if (data.getSuccess().equalsIgnoreCase("1")) {
                        getCart();
                    } else {
                        U.hideProgress();
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

    private void placeOrder(String cartId) {
        if (getPreferences().getStringPref(P.USER_TYPE).equals(C.SO)) {
            if (partyName == null) {
                showToast(getString(R.string.missing_party_id));
                return;
            } else if (partyName.equals("-1")) {
                showToast(getString(R.string.select_party_name));
                return;
            }
            createdFor = partyName;
        }
        U.yesNoDialog(getCont(), this, "Confirmation", "Outstanding amount for this Party is: " + outstandingAmt, 0, cartId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_place_order:
                if (productList.size() > 0) {
                    String cartIds = "";
                    for (CartListResponse.CartDetails obj : productList) {
                        cartIds = cartIds + "," + obj.getCartId();
                    }
                    if (cartIds.length() > 0)
                        cartIds = cartIds.substring(1, cartIds.length());

                    placeOrder(cartIds);
                } else {
                    showToast(getString(R.string.empty_cart));
                }
                break;
        }
    }

    @Override
    public void onCartDelete(CartListResponse.CartDetails obj) {
        deleteCart(obj.getCartId());
    }


    private void getPartyList() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            partyList.clear();
            PartyModel obj = new PartyModel();
            obj.setUserId("-1");
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
                    } else {
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

    @Override
    public void onYesClicked(int pos, String type) { //using type for getting the cart ids for now, as there is no other variable

            if (U.isConnectedToInternet(getCont())) {
                U.showProgress(getCont());
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonSuccessModel> call = apiService.placeOrder(getPreferences().getStringPref(P.USER_ID), type, createdFor);
                call.enqueue(new Callback<CommonSuccessModel>() {
                    @Override
                    public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                        CommonSuccessModel data = response.body();
                        U.hideProgress();
                        if (data.getSuccess().equalsIgnoreCase("1")) {
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
}
