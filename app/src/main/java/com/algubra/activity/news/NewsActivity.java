package com.algubra.activity.news;

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
import com.algubra.activity.newsletter.NewsLetterActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

/**
 * Created by gayatri on 20/7/17.
 */
public class NewsActivity extends Activity implements View.OnClickListener {
    HeaderManager headermanager;
    RelativeLayout relativeHeader, relativeNewsRoom, relativeNewsLetter;
    ImageView back;
    Activity mContext;
    TextView textLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_split);
        mContext = this;
        initUI();
        resetDisconnectTimer();

    }

    private void initUI() {

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        relativeNewsRoom = (RelativeLayout) findViewById(R.id.rlNewsRoom);
        relativeNewsLetter = (RelativeLayout) findViewById(R.id.rlNewsLetter);
        textLearn = (TextView) findViewById(R.id.textLearn);
        headermanager = new HeaderManager(mContext, getString(R.string.news));

        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            textLearn.setText(getResources().getString(R.string.phantas_magoria));

        } else {
            headermanager.getHeader(relativeHeader, 3);
            textLearn.setText(getResources().getString(R.string.i_learn));

        }
        back = headermanager.getLeftButton();
        relativeNewsRoom.setOnClickListener(this);
        relativeNewsLetter.setOnClickListener(this);
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlNewsRoom:
                stopDisconnectTimer();

                Intent intent = new Intent(mContext, LoadWebUrlActivity.class);
                intent.putExtra("page_type", "news");
                intent.putExtra("tab_type", "News Room");
                mContext.startActivity(intent);
                break;

            case R.id.rlNewsLetter:
                stopDisconnectTimer();

                Intent mintent = new Intent(mContext, NewsLetterActivity.class);
               /* mintent.putExtra("page_type", "video_gallary");
                mintent.putExtra("tab_type", "Videos");*/
                mContext.startActivity(mintent);
                break;
        }
    }

    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(NewsActivity.this, false);
            AppPreferenceManager.setUserId(NewsActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(NewsActivity.this, false);
            AppPreferenceManager.setSchoolSelection(NewsActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(NewsActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };*/

    public void resetDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
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
            Intent mIntent = new Intent(NewsActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}