package com.algubra.activity.quotesandstories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.quotesandstories.model.QuotesandStoryModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by user2 on 5/5/17.
 */
public class QuotesDetailActivity extends Activity {
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, about_us_bannerImgView;
    Context mContext = this;
    ArrayList<QuotesandStoryModel> listQuotes;
    TextView text;
    Bundle extras;
    String quote;
    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes_detail);
        initUI();
        resetDisconnectTimer();

    }

    private void initUI() {
        extras=getIntent().getExtras();

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        layout= (RelativeLayout) findViewById(R.id.layout);
        text= (TextView) findViewById(R.id.text);

//        headermanager = new HeaderManager(QuotesDetailActivity.this, "Today's Quote");
        headermanager = new HeaderManager(QuotesDetailActivity.this, "Weekly Quote");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            layout.setBackground(getResources().getDrawable(R.drawable.transparentquote_green));
        } else {
            headermanager.getHeader(relativeHeader, 3);
            layout.setBackground(getResources().getDrawable(R.drawable.transparentquote_blue));

        }
        if(extras!=null){
            quote=extras.getString("quote");

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

        text.setText(quote);

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
            AppPreferenceManager.setIsGuest(QuotesDetailActivity.this, false);
            AppPreferenceManager.setUserId(QuotesDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(QuotesDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(QuotesDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(QuotesDetailActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(QuotesDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
