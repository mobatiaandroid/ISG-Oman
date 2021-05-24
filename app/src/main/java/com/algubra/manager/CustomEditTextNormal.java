package com.algubra.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.algubra.constants.JsonTagConstants;

/**
 * Created by gayatri on 17/4/17.
 */
public class CustomEditTextNormal extends EditText implements JsonTagConstants {

    public CustomEditTextNormal(Context context) {
        super(context);
        Typeface type = Typeface.createFromAsset(context.getAssets(), FONT_NORMAL);
        this.setTypeface(type);
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(),FONT_NORMAL);
        this.setTypeface(type);
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface type = Typeface.createFromAsset(context.getAssets(), FONT_NORMAL);
        this.setTypeface(type);
    }

}