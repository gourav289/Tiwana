package com.gk.myapp.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gk.myapp.R;

/**
 * Created by Gk on 11-12-2016.
 */
public class CustomTabButton extends FrameLayout {
    private Context mContext;
    private String BTN_TEXT = "";
    private int BTN_BG = 0;
    private boolean IS_SELECTED = false;

    private Button btn;
    private ImageView ivSelected;
    private TabClicked mListener;

    public CustomTabButton(Context context) {
        super(context);
        this.mContext = context;
        init(null);
    }

    public CustomTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    public CustomTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.MyCustomTabButton, 0, 0);
            BTN_BG = ta.getColor(R.styleable.MyCustomTabButton_bg_color, mContext.getResources().getColor(R.color.green_text));
            BTN_TEXT = ta.getString(R.styleable.MyCustomTabButton_text);
            IS_SELECTED = ta.getBoolean(R.styleable.MyCustomTabButton_selected, false);
        }

        LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inf.inflate(R.layout.custom_tab_btn, null);
        btn = (Button) view.findViewById(R.id.btn);
        ivSelected = (ImageView) view.findViewById(R.id.iv_selected);

        btn.setText(BTN_TEXT);
        btn.setBackgroundColor(BTN_BG);

        if(IS_SELECTED)
            ivSelected.setVisibility(View.VISIBLE);
        else
            ivSelected.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onTabClicked(v);
                }
            }
        });

        this.addView(view);

    }

    public void setSelected(boolean selected){
        if(selected)
            ivSelected.setVisibility(View.VISIBLE);
        else
            ivSelected.setVisibility(View.INVISIBLE);
//        invalidate();
    }

    public void setTabListener(TabClicked mListener){
        this.mListener=mListener;
    }


    public interface TabClicked{
        public void onTabClicked(View id);
    }

}
