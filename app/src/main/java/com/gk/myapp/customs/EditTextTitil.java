package com.gk.myapp.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.gk.myapp.utils.C;

/**
 * Created by Gk on 21-12-2016.
 */
public class EditTextTitil extends EditText {

    public EditTextTitil(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextTitil(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextTitil(Context context) {
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