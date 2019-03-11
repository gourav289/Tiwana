package com.gk.myapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.customs.CustomTabButton;
import com.gk.myapp.fragments.LeaveFragment;
import com.gk.myapp.fragments.MessagesFragment;
import com.gk.myapp.fragments.MyJobsFragment;
import com.gk.myapp.fragments.NotificationMainFragment;
import com.gk.myapp.fragments.SelectTargetFragment;
import com.gk.myapp.fragments.TargetListFragment;
import com.gk.myapp.utils.P;

/**
 * Created by Gk on 11-12-2016.
 */
public class BaseActivityTabs extends FragmentActivity {

    private CustomTabButton tabTarget, tabNotification, tabMessage, tabHistory, tabMyJobs;

    private FrameLayout frmContainer;
    private ImageButton ibtnLeft, ibtnRight;
    private TextView txtTitle;
    private TextView txtRight,txtLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_tabs);

        getIds();
        setListener();
        setHeaderButtonsToNull();
        switchFragment(new MyJobsFragment(), false, getString(R.string.my_job_title));
    }

    private void getIds() {
        tabTarget = (CustomTabButton) findViewById(R.id.tab_target);
        tabNotification = (CustomTabButton) findViewById(R.id.tab_notification);
        tabHistory = (CustomTabButton) findViewById(R.id.tab_history);
        tabMessage = (CustomTabButton) findViewById(R.id.tab_message);
        tabMyJobs = (CustomTabButton) findViewById(R.id.tab_my_jobs);

        ibtnLeft = (ImageButton) findViewById(R.id.ibtn_back);
        ibtnRight = (ImageButton) findViewById(R.id.ibtn_right);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        frmContainer = (FrameLayout) findViewById(R.id.container);
        txtRight = (TextView) findViewById(R.id.txt_right);
        txtLeft= (TextView) findViewById(R.id.txt_left);
    }

    private void setListener() {
        tabTarget.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(true);
                tabHistory.setSelected(false);
                tabMessage.setSelected(false);
                tabNotification.setSelected(false);
                tabMyJobs.setSelected(false);
//                U.toast(BaseActivityTabs.this, "target clicked");
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof SelectTargetFragment) {
                } else {
                    switchFragment(new SelectTargetFragment(), false, getString(R.string.target));
                }
            }
        });

        tabNotification.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(false);
                tabHistory.setSelected(false);
                tabMessage.setSelected(false);
                tabNotification.setSelected(true);
                tabMyJobs.setSelected(false);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof NotificationMainFragment) {
                } else {
                    switchFragment(new NotificationMainFragment(), false, getString(R.string.notifications));
                }
            }
        });

        tabHistory.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(false);
                tabHistory.setSelected(true);
                tabMessage.setSelected(false);
                tabNotification.setSelected(false);
                tabMyJobs.setSelected(false);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof LeaveFragment) {
                } else {
                    switchFragment(new LeaveFragment(), false, getString(R.string.leave));
                }
            }
        });

        tabMessage.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(false);
                tabHistory.setSelected(false);
                tabMessage.setSelected(true);
                tabNotification.setSelected(false);
                tabMyJobs.setSelected(false);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof MessagesFragment) {
                } else {
                    switchFragment(new MessagesFragment(), false, getString(R.string.message));
                }
            }
        });

        tabMyJobs.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(false);
                tabHistory.setSelected(false);
                tabMessage.setSelected(false);
                tabNotification.setSelected(false);
                tabMyJobs.setSelected(true);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof MyJobsFragment) {
                } else {
                    switchFragment(new MyJobsFragment(), false, getString(R.string.my_job_title));
                }
            }
        });
    }

    protected void setHeaderTitle(String title) {
        txtTitle.setText(title);
    }


    public void setLeftButton(boolean visibility, int icon, final HeaderButtonClick mListener) {
        if (visibility) {
            ibtnLeft.setVisibility(View.VISIBLE);
        } else {
            ibtnLeft.setVisibility(View.INVISIBLE);
        }

        ibtnLeft.setImageResource(icon);

        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onHeaderButtonClicked(v.getId());
            }
        });
    }


    public void setRightButton(boolean visibility, int icon, final HeaderButtonClick mListener) {
        if (visibility) {
            ibtnRight.setVisibility(View.VISIBLE);
        } else {
            ibtnRight.setVisibility(View.INVISIBLE);
        }

        ibtnRight.setImageResource(icon);

        ibtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onHeaderButtonClicked(v.getId());
            }
        });
    }

    public void setRightText(String text, final HeaderButtonClick mListener){
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setText(text);
        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                    mListener.onHeaderButtonClicked(v.getId());
            }
        });
    }

    public void setLeftText(String text, final HeaderButtonClick mListener){
        txtLeft.setVisibility(View.VISIBLE);
        txtLeft.setText(text);
        txtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                    mListener.onHeaderButtonClicked(v.getId());
            }
        });
    }


    public interface HeaderButtonClick {
        public void onHeaderButtonClicked(int id);
    }


    public void switchFragment(Fragment fragment, boolean addToStack, String title) {
        setHeaderButtonsToNull();
        if (title != null) {
            txtTitle.setText(title);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.container, fragment);
        if (addToStack)
            transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }

    private void setHeaderButtonsToNull() {
        setLeftButton(false, 0, null);
        setRightButton(false, 0, null);
        txtRight.setVisibility(View.GONE);
        txtLeft.setVisibility(View.GONE);
        setRightText("",null);
        setLeftText("",null);
    }

}
