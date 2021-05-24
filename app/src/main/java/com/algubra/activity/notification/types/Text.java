package com.algubra.activity.notification.types;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class Text extends Activity{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,about_us_bannerImgView;
    Context mContext = this;
    Bundle extras;
    ArrayList<NotificationModel> specialMessageModelArrayList=new ArrayList<>();
    int position;
    TextView txt,mDateTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_alert);
        extras = getIntent().getExtras();
        if (extras != null) {
            specialMessageModelArrayList = (ArrayList<NotificationModel>) extras
                    .getSerializable("array");
            position=extras.getInt("position");

        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        txt= (TextView) findViewById(R.id.txt);
        mDateTv= (TextView) findViewById(R.id.mDateTv);
        headermanager = new HeaderManager(Text.this, "Notification Details");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt.setText(specialMessageModelArrayList.get(position).getMessage());
        mDateTv.setText(specialMessageModelArrayList.get(position).getCreated_time());

    }
}
