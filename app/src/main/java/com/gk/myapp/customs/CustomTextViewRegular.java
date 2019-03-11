package com.gk.myapp.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewRegular extends TextView {

    

    public CustomTextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()){
//        	Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), C.LATO_REGULAR);
//        	setTypeface(normalTypeface);
        }
    }
}