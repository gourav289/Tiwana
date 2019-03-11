package com.gk.myapp.customs;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.gk.myapp.utils.C;

/**
 * Created by Gk on 19-12-2016.
 */
public class EditTextRobotoItalic extends EditText {

    public EditTextRobotoItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextRobotoItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextRobotoItalic(Context context) {
        super(context);
        init();
    }

    public void init() {
        if (!isInEditMode()) {
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), C.ROBOTO_ITALIC);
            setTypeface(normalTypeface);
        }
    }
}