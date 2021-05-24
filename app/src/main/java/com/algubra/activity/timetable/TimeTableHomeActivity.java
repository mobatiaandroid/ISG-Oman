package com.algubra.activity.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.schedules.SchedulesActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

/**
 * Created by gayatri on 20/7/17.
 */
public class TimeTableHomeActivity extends Activity implements View.OnClickListener {
    HeaderManager headermanager;
    RelativeLayout relativeHeader, relativeExamSchedule, relativeTimeTable;
    ImageView back;
    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_split);
        mContext = this;
        initUI();
        resetDisconnectTimer();

    }

    private void initUI() {

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        relativeExamSchedule = (RelativeLayout) findViewById(R.id.rlExamSchedule);
        relativeTimeTable = (RelativeLayout) findViewById(R.id.rlClassTimeTable);
        headermanager = new HeaderManager(mContext, getString(R.string.time_table));

        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);

        } else {
            headermanager.getHeader(relativeHeader, 3);

        }
        back = headermanager.getLeftButton();
        relativeExamSchedule.setOnClickListener(this);
        relativeTimeTable.setOnClickListener(this);
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
            case R.id.rlClassTimeTable:
                stopDisconnectTimer();

                Intent intent = new Intent(mContext, TimeTableActivity.class);
                intent.putExtra("page_type", "news");
                intent.putExtra("tab_type", "News Room");
                mContext.startActivity(intent);
                break;

            case R.id.rlExamSchedule:
                stopDisconnectTimer();

                Intent mintent = new Intent(mContext, SchedulesActivity.class);
               /* mintent.putExtra("page_type", "video_gallary");
                mintent.putExtra("tab_type", "Videos");*/
                mContext.startActivity(mintent);
                break;
        }
    }
   /* public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(TimeTableHomeActivity.this, false);
            AppPreferenceManager.setUserId(TimeTableHomeActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(TimeTableHomeActivity.this, false);
            AppPreferenceManager.setSchoolSelection(TimeTableHomeActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(TimeTableHomeActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(TimeTableHomeActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}