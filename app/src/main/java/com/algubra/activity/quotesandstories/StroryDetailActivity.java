package com.algubra.activity.quotesandstories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.quotesandstories.model.QuotesandStoryModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 19/5/17.
 */
public class StroryDetailActivity extends Activity{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, about_us_bannerImgView;
    Context mContext = this;
    ArrayList<QuotesandStoryModel> listQuotes;
    TextView text,content;
    Bundle extras;
    String story,title;
    ScrollView layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        initUI();
        resetDisconnectTimer();

    }
    private void initUI() {
        extras=getIntent().getExtras();

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        layout= (ScrollView) findViewById(R.id.layout);
        text= (TextView) findViewById(R.id.text);
        content= (TextView) findViewById(R.id.content);

        headermanager = new HeaderManager(StroryDetailActivity.this, "Weekly Story");
//        headermanager = new HeaderManager(StroryDetailActivity.this, "Today's Story");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
           // layout.setBackground(getResources().getDrawable(R.drawable.transparentquote_green));
        } else {
            headermanager.getHeader(relativeHeader, 3);
           // layout.setBackground(getResources().getDrawable(R.drawable.transparentquote_blue));

        }
        if(extras!=null){
            story=extras.getString("story");
            title=extras.getString("title");
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

        text.setText(title);
        content.setText(story);

    }

   /* public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(StroryDetailActivity.this, false);
            AppPreferenceManager.setUserId(StroryDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(StroryDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(StroryDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(StroryDetailActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };*/

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
            Intent mIntent = new Intent(StroryDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
