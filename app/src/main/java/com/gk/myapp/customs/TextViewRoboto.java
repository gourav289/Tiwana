package com.gk.myapp.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gk.myapp.utils.C;

/**
 * Created by Gk on 20-12-2016.
 */
public class TextViewRoboto extends TextView {



    public TextViewRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRoboto(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()){
        	Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), C.ROBOTO);
        	setTypeface(normalTypeface);
        }
    }
}