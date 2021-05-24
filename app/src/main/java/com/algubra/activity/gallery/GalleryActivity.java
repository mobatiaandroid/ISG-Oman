package com.algubra.activity.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.loadweburl.LoadWebUrlActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

/**
 * Created by user2 on 4/5/17.
 */
public class GalleryActivity extends Activity implements View.OnClickListener {
    HeaderManager headermanager;
    RelativeLayout relativeHeader, mProgressRelLayout;
    TextView mTextMoreImage, mTextMoreVideo;
    ImageView back;
    Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initUI();
        resetDisconnectTimer();


    }

    private void initUI() {

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(GalleryActivity.this, "Gallery");
        mTextMoreImage = (TextView) findViewById(R.id.textMoreImage);
        mTextMoreVideo = (TextView) findViewById(R.id.textMoreVideo);
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            mTextMoreImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rect_green));
            mTextMoreVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rect_green));

        } else {
            headermanager.getHeader(relativeHeader, 3);
            mTextMoreImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.gallery_round_bg));
            mTextMoreVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.gallery_round_bg));

        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                finish();
            }
        });


        mTextMoreImage.setOnClickListener(this);
        mTextMoreVideo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textMoreImage:
                stopDisconnectTimer();

                Intent intent =new Intent(mContext, LoadWebUrlActivity.class);
                intent.putExtra("page_type","photo_gallery");
                intent.putExtra("tab_type","Photos");
                mContext.startActivity(intent);
                break;

            case R.id.textMoreVideo:
                stopDisconnectTimer();

                Intent mintent =new Intent(mContext, LoadWebUrlActivity.class);
                mintent.putExtra("page_type","video_gallary");
                mintent.putExtra("tab_type","Videos");
                mContext.startActivity(mintent);
                break;
        }
    }
    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(GalleryActivity.this, false);
            AppPreferenceManager.setUserId(GalleryActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(GalleryActivity.this, false);
            AppPreferenceManager.setSchoolSelection(GalleryActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(GalleryActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };
*/
    public void resetDisconnectTimer(){
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDisconnectTimer();
    }
  @Override protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(GalleryActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}