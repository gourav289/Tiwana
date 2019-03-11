package com.gk.myapp.activities;

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
import com.gk.myapp.fragments.PaymentConsFragment;
import com.gk.myapp.fragments.ProductListFragment;
import com.gk.myapp.fragments.TargetConsFragment;
import com.gk.myapp.fragments.TargetListFragment;

/**
 * Created by Gk on 06-09-2017.
 */
public class BaseActivityTabsCons extends FragmentActivity {

    private CustomTabButton tabTarget, tabProducts, tabPayments;

    private FrameLayout frmContainer;
    private ImageButton ibtnLeft, ibtnRight;
    private TextView txtTitle,txtRight,txtLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_cons);

        getIds();
        setListener();
        setHeaderButtonsToNull();
        switchFragment(new ProductListFragment(), false, getString(R.string.btn_products));
    }

    private void getIds() {
        tabTarget = (CustomTabButton) findViewById(R.id.tab_target);
        tabProducts = (CustomTabButton) findViewById(R.id.tab_products);
        tabPayments = (CustomTabButton) findViewById(R.id.tab_payments);

        ibtnLeft = (ImageButton) findViewById(R.id.ibtn_back);
        ibtnRight = (ImageButton) findViewById(R.id.ibtn_right);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        frmContainer = (FrameLayout) findViewById(R.id.container);
        txtRight= (TextView) findViewById(R.id.txt_right);
        txtLeft= (TextView) findViewById(R.id.txt_left);
    }

    private void setListener() {
        tabTarget.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(true);
                tabPayments.setSelected(false);
                tabProducts.setSelected(false);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof TargetListFragment) {
                } else {
                    switchFragment(new TargetConsFragment(), false, getString(R.string.target));
                }
            }
        });


        tabPayments.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(false);
                tabPayments.setSelected(true);
                tabProducts.setSelected(false);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof TargetListFragment) {
                } else {
                    switchFragment(new PaymentConsFragment(), false, getString(R.string.btn_payments));
                }
            }
        });

        tabProducts.setTabListener(new CustomTabButton.TabClicked() {
            @Override
            public void onTabClicked(View id) {
                tabTarget.setSelected(false);
                tabPayments.setSelected(false);
                tabProducts.setSelected(true);
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if (frag != null && frag instanceof TargetListFragment) {
                } else {
                    switchFragment(new ProductListFragment(), false, getString(R.string.btn_products));
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
                if (mListener != null)
                    mListener.onHeaderButtonClicked(v.getId());
            }
        });
    }


    private void setHeaderButtonsToNull() {
        setLeftButton(false, 0, null);
        setRightButton(false, 0, null);
        txtRight.setVisibility(View.GONE);
        txtLeft.setVisibility(View.GONE);
        setRightText("", null);
        setLeftText("", null);
    }

}
