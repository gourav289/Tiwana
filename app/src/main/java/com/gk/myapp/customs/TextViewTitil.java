package com.gk.myapp.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gk.myapp.utils.C;

/**
 * Created by Gk on 20-12-2016.
 */
public class TextViewTitil extends TextView {


    public TextViewTitil(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewTitil(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewTitil(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), C.TITIL_SEMI_BOLD);
            setTypeface(normalTypeface);
        }
    }
}