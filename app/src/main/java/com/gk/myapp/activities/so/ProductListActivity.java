package com.gk.myapp.activities.so;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivity;
import com.gk.myapp.activities.cons.CartConsActivity;
import com.gk.myapp.adapters.ProductExpendableConsAdapter;
import com.gk.myapp.app.AppController;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.CommonSuccessModel;
import com.gk.myapp.model.ProductListResponse;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 24-09-2017.
 */
public class ProductListActivity extends BaseActivity implements ProductExpendableConsAdapter.AddCartClick {

    private ExpandableListView lv;
    private List<ProductListResponse.ProductDetails> productList;
    private HashMap<String, List<ProductListResponse.ProductAttributes>> productAttList;
    private ProductExpendableConsAdapter mAdp;

    @Override
    protected int setView() {
        return R.layout.fragment_product_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIds();
        setListener();
        getProducts();
    }

    private void getIds() {
        lv = (ExpandableListView) findViewById(R.id.list_view);
        productList = new ArrayList<>();
        productAttList = new HashMap<>();

        ViewTreeObserver vto = lv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    lv.setIndicatorBounds((int) (AppController.getInstance().getScreenWidth() - U.dpToPx(35)), (int) (AppController.getInstance().getScreenWidth() - U.dpToPx(5)));
                } else {
                    lv.setIndicatorBoundsRelative((int) (AppController.getInstance().getScreenWidth() - U.dpToPx(35)), (int) (AppController.getInstance().getScreenWidth() - U.dpToPx(5)));
                }
            }
        });

        setRightButton(true, R.mipmap.ic_cart, new HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                Intent mIntent = new Intent(ProductListActivity.this, CartConsActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected String setHeaderTitle() {
        return getString(R.string.btn_products);
    }

    private void setListener() {
    }

    private void getProducts() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ProductListResponse> call = apiService.productList(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<ProductListResponse>() {
                @Override
                public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                    ProductListResponse data = response.body();
                    U.hideProgress();
                    productList.clear();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        productList.addAll(data.getData());
                        for (ProductListResponse.ProductDetails obj : productList) {
                            productAttList.put(obj.getId(), obj.getAttribute());
                        }
                    } else {
                        U.toast(data.getMessage());
                    }

                    mAdp = new ProductExpendableConsAdapter(getCont(), productList, productAttList, ProductListActivity.this);
                    lv.setAdapter(mAdp);
                }

                @Override
                public void onFailure(Call<ProductListResponse> call, Throwable t) {
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void addCart(ProductListResponse.ProductAttributes obj) {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonSuccessModel> call = apiService.addToCart(getPreferences().getStringPref(P.USER_ID), obj.getAttributeId(), "" + obj.getQuantity());
            call.enqueue(new Callback<CommonSuccessModel>() {
                @Override
                public void onResponse(Call<CommonSuccessModel> call, Response<CommonSuccessModel> response) {
                    CommonSuccessModel data = response.body();
                    U.hideProgress();
                    U.toast(data.getMessage());
                }

                @Override
                public void onFailure(Call<CommonSuccessModel> call, Throwable t) {
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onAddToCart(ProductListResponse.ProductAttributes obj) {
        addCart(obj);
    }
}
