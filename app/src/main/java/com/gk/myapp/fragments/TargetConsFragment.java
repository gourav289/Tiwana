package com.gk.myapp.fragments;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.BaseActivityTabsCons;
import com.gk.myapp.activities.LoginActivity;
import com.gk.myapp.activities.OrderHistoryActivity;
import com.gk.myapp.activities.SelectAsmActivity;
import com.gk.myapp.activities.TargetDetailsActivity;
import com.gk.myapp.activities.cons.TargetDetailsActivityCons;
import com.gk.myapp.activities.so.AddPaymentActivity;
import com.gk.myapp.adapters.MonthListAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.interfaces.YesNoDialogConfirmation;
import com.gk.myapp.model.DealerTargetList;
import com.gk.myapp.model.YearTargetResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 10-09-2017.
 */
public class TargetConsFragment extends BaseFragment implements YesNoDialogConfirmation {
    private String year;
    private BaseActivityTabsCons mActivity;
    private ListView lv;
    private List<DealerTargetList.MonthList> mList;
    private MonthListAdapter mAdp;

    @Override
    protected int setView() {
        return R.layout.fragment_select_target;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        year = "" + Calendar.getInstance().get(Calendar.YEAR);
        getIds(view);
        setListeners();

        getMonthList();
    }


    private void getIds(View v) {
        mActivity = (BaseActivityTabsCons) getActivity();
        mList = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.list_view);
        mActivity.setRightButton(true, R.mipmap.ic_logout, new BaseActivityTabsCons.HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                U.yesNoDialog(getCont(), TargetConsFragment.this, "", getString(R.string.logot_msg), 0, "logout");
            }
        });
        mActivity.setLeftText("Order\nHistory", new BaseActivityTabsCons.HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                Intent mIntent = new Intent(mActivity, OrderHistoryActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void setListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openNextActivity(mList.get(i).getMonth(),mList.get(i).getMonthDetail());
            }
        });
    }

    private void getMonthList() {
        if (U.isConnectedToInternet(getCont())) {
            U.showProgress(getCont());
            mList.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DealerTargetList> call = apiService.getDealerMonthList(getPreferences().getStringPref(P.USER_ID), year);
            call.enqueue(new Callback<DealerTargetList>() {
                @Override
                public void onResponse(Call<DealerTargetList> call, Response<DealerTargetList> response) {
                    DealerTargetList data = response.body();
                    if (data.getSuccess().equals("1")) {
                        mList=data.getData();
                    } else {
                        showToast(data.getMessage());
                    }
                    mAdp=new MonthListAdapter(getActivity(),mList);
                    lv.setAdapter(mAdp);
                    U.hideProgress();

                }

                @Override
                public void onFailure(Call<DealerTargetList> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void openNextActivity(String month, String title) {

        Intent mIntent = new Intent(getActivity(), TargetDetailsActivityCons.class);
        mIntent.putExtra("title", title);
        mIntent.putExtra("year", year);
        mIntent.putExtra("month", month);
        startActivity(mIntent);

    }


    @Override
    public void onYesClicked(int pos, String type) {
        if (type.equals("logout")) {
            getPreferences().clearLoginData();
            startActivity(new Intent(mActivity, LoginActivity.class));
            mActivity.finish();
        }
    }
}




