package com.gk.myapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.gk.myapp.R;
import com.gk.myapp.activities.BaseActivityTabs;
import com.gk.myapp.activities.CreateMessageActivity;
import com.gk.myapp.activities.MessageDetailsActivity;
import com.gk.myapp.activities.ProfileActivity;
import com.gk.myapp.adapters.MessageAdapter;
import com.gk.myapp.interfaces.ApiClient;
import com.gk.myapp.interfaces.ApiInterface;
import com.gk.myapp.model.MessageListModel;
import com.gk.myapp.model.MessageModel;
import com.gk.myapp.model.NotificationModel;
import com.gk.myapp.model.UserDetails;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;
import com.gk.myapp.utils.U;
import com.gk.myapp.utils.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gk on 18-12-2016.
 */
public class MessagesFragment extends BaseFragment implements View.OnClickListener {
    private final int INBOX = 1;
    private final int SENT = 2;
    private int selected = INBOX;
    private Button btnInbox, btnSent;
    private ListView lv;
    private ArrayList<MessageModel> mListMessages, mListSent;
    private MessageAdapter mAdp;
    private BaseActivityTabs mActivity;

    public static boolean update=false;


    protected int setView() {
        return R.layout.fragment_messages;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivityTabs) getActivity();
        mListMessages = new ArrayList<>();
        mListSent = new ArrayList<>();
        getIds(view);
        setListeners();
    }

    private void getIds(View v) {
        lv = (ListView) v.findViewById(R.id.list_view);
        btnInbox = (Button) v.findViewById(R.id.btn_inbox);
        btnSent = (Button) v.findViewById(R.id.btn_sent);
        mActivity.setRightButton(true, R.mipmap.ic_chat, new BaseActivityTabs.HeaderButtonClick() {
            @Override
            public void onHeaderButtonClicked(int id) {
                Intent mIntent = new Intent(mActivity, CreateMessageActivity.class);
                startActivity(mIntent);
            }
        });

//        for(int i=0;i<5;i++){
//            MessageModel obj=new MessageModel();
//            obj.setMessage("Dummy message just for testing UI of the application");
//            if(i==0){
//                obj.setTitle("GM");
//            }else if(i==4){
//                obj.setTitle("SO");
//            }else{
//                obj.setTitle("Me");
//            }
//            mListMessages.add(obj);
//        }
        mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
        lv.setAdapter(mAdp);

        if (getPreferences().getStringPref(P.USER_TYPE).equalsIgnoreCase(C.GM)) {
            hitGMMessagesWS();
        } else if (getPreferences().getStringPref(P.USER_TYPE).equalsIgnoreCase(C.ASM)) {
            getASMMessages();
        } else if (getPreferences().getStringPref(P.USER_TYPE).equalsIgnoreCase(C.SO)) {
            getSOMessages();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected == INBOX) {
                    Intent mIntent = new Intent(mActivity, MessageDetailsActivity.class);
                    mIntent.putExtra("data", mListMessages.get(position));
                    startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(mActivity, MessageDetailsActivity.class);
                    mIntent.putExtra("data", mListSent.get(position));
                    startActivity(mIntent);
                }
            }
        });
    }

    private void setListeners() {
        btnInbox.setOnClickListener(this);
        btnSent.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(update){
            update=false;
            if (getPreferences().getStringPref(P.USER_TYPE).equalsIgnoreCase(C.GM)) {
                hitGMMessagesWS();
            } else if (getPreferences().getStringPref(P.USER_TYPE).equalsIgnoreCase(C.ASM)) {
                getASMMessages();
            } else if (getPreferences().getStringPref(P.USER_TYPE).equalsIgnoreCase(C.SO)) {
                getSOMessages();
            }
        }
    }

    private void hitGMMessagesWS() {
        if (U.isConnectedToInternet(getActivity())) {
            U.showProgress(getActivity());
            mListMessages.clear();
            mListSent.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MessageListModel> call = apiService.getGMMessages(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<MessageListModel>() {
                @Override
                public void onResponse(Call<MessageListModel> call, Response<MessageListModel> response) {
                    MessageListModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {
//                        mListMessages.addAll(data.getData());
                        for (MessageModel obj : data.getData()) {
                            if (!obj.getTitle().equals(getPreferences().getStringPref(P.USER_TYPE))) {
                                mListMessages.add(obj);
                            } else {
                                mListSent.add(obj);
                            }
                        }
                    } else {
                        U.toast(data.getMessage());
                    }
                    selected = INBOX;
                    btnInbox.setBackgroundResource(R.drawable.btn_notif_rounded_left_selected);
                    btnSent.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);
                    mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                    lv.setAdapter(mAdp);
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<MessageListModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                    mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                    lv.setAdapter(mAdp);
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void getASMMessages() {
        if (U.isConnectedToInternet(getActivity())) {
            U.showProgress(getActivity());
            mListMessages.clear();
            mListSent.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MessageListModel> call = apiService.getASMMessages(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<MessageListModel>() {
                @Override
                public void onResponse(Call<MessageListModel> call, Response<MessageListModel> response) {
                    MessageListModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {
//                        mListMessages.addAll(data.getData());
                        for (MessageModel obj : data.getData()) {
                            if (!obj.getTitle().equals(getPreferences().getStringPref(P.USER_TYPE))) {
                                mListMessages.add(obj);
                            } else {
                                mListSent.add(obj);
                            }
                        }
                    } else {
                        U.toast(data.getMessage());

                    }
                    selected = INBOX;
                    btnInbox.setBackgroundResource(R.drawable.btn_notif_rounded_left_selected);
                    btnSent.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);
                    mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                    lv.setAdapter(mAdp);
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<MessageListModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                    mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                    lv.setAdapter(mAdp);
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    private void getSOMessages() {
        if (U.isConnectedToInternet(getActivity())) {
            U.showProgress(getActivity());
            mListMessages.clear();
            mListSent.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MessageListModel> call = apiService.getSOMessages(getPreferences().getStringPref(P.USER_ID));
            call.enqueue(new Callback<MessageListModel>() {
                @Override
                public void onResponse(Call<MessageListModel> call, Response<MessageListModel> response) {
                    MessageListModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase("1")) {
                        for (MessageModel obj : data.getData()) {
                            if (!obj.getTitle().equals(getPreferences().getStringPref(P.USER_TYPE))) {
                                mListMessages.add(obj);
                            } else {
                                mListSent.add(obj);
                            }
                        }

                    } else {
                        U.toast(data.getMessage());
                    }
                    selected = INBOX;
                    btnInbox.setBackgroundResource(R.drawable.btn_notif_rounded_left_selected);
                    btnSent.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);
                    mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                    lv.setAdapter(mAdp);
                    U.hideProgress();
                }

                @Override
                public void onFailure(Call<MessageListModel> call, Throwable t) {
                    // Log error here since request failed
                    U.toast(getString(R.string.error_message));
                    U.hideProgress();
                    mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                    lv.setAdapter(mAdp);
                }
            });
        } else {
            U.toast(getString(R.string.no_internet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_inbox:
                selected = INBOX;
                btnInbox.setBackgroundResource(R.drawable.btn_notif_rounded_left_selected);
                btnSent.setBackgroundResource(R.drawable.btn_notif_rounded_right_unselected);
                mAdp = new MessageAdapter(getActivity(), mListMessages,selected);
                lv.setAdapter(mAdp);
                break;

            case R.id.btn_sent:
                selected = SENT;
                btnInbox.setBackgroundResource(R.drawable.btn_notif_rounded_left_unselected);
                btnSent.setBackgroundResource(R.drawable.btn_notif_rounded_right_selected);
                mAdp = new MessageAdapter(getActivity(), mListSent,selected);
                lv.setAdapter(mAdp);
                break;
        }
    }
}