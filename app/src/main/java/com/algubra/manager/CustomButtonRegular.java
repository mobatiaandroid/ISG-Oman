package com.algubra.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.algubra.constants.JsonTagConstants;

/**
 * Created by gayatri on 17/4/17.
 */
public class CustomButtonRegular extends Button implements JsonTagConstants {

    public CustomButtonRegular(Context context) {
        super(context);
        Typeface type = Typeface.createFromAsset(context.getAssets(), FONT_REGULAR);
        this.setTypeface(type);
    }

    public CustomButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(),FONT_REGULAR);
        this.setTypeface(type);
    }

    public CustomButtonRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface type = Typeface.createFromAsset(context.getAssets(), FONT_REGULAR);
        this.setTypeface(type);
    }

}