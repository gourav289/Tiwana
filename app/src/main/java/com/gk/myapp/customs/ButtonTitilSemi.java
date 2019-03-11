package com.gk.myapp.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.gk.myapp.utils.C;

/**
 * Created by Gk on 20-12-2016.
 */
public class ButtonTitilSemi extends Button {
    public ButtonTitilSemi(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonTitilSemi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonTitilSemi(Context context) {
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