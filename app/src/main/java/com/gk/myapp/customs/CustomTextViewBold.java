package com.gk.myapp.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewBold extends TextView {

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()){
//            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), C.RALEWAY_BOLD);
//            setTypeface(normalTypeface);
        }
    }
}