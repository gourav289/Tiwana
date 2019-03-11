package com.gk.myapp.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoComplete extends AutoCompleteTextView {



    public CustomAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAutoComplete(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()){
//            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), C.LATO_REGULAR);
//            setTypeface(normalTypeface);
        }
    }
}