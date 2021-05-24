package com.algubra.volleymanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.algubra.R;

/**
 * Created by gayatri on 11/4/17.
 */
 public class CustomProgressBar extends Dialog {

    private ImageView iv;
    private Context context;

    public CustomProgressBar(Context context, int resourceIdOfImage) {
        super(context, R.style.TransparentProgressDialog);
        this.context = context;
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        iv = new ImageView(context);
        iv.setImageResource(resourceIdOfImage);
        layout.addView(iv, params);
        addContentView(layout, params);
    }

    @Override
    public void show() {
        if (!((Activity) context).isFinishing()) {
            super.show();
            RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(context, android.R.interpolator.linear);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(1000);
            iv.setAnimation(anim);
            iv.startAnimation(anim);
        }
    }
}