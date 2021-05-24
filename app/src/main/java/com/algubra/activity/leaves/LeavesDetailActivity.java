package com.algubra.activity.leaves;

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
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.leaves.model.LeavesModel;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.staffdirectory.model.StaffModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class LeavesDetailActivity extends Activity{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    LinearLayout fromlayout,reasonlayout;
    TextView stnameValue,leaveDateFromValue,leaveDateToValue,reasonValue;
    Bundle extras;
    ArrayList<LeavesModel> leavesModelArrayList=new ArrayList<>();
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_detailpage);
        initUI();
        resetDisconnectTimer();

        setValues();
    }
    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            leavesModelArrayList = (ArrayList<LeavesModel>) extras
                    .getSerializable("array");
             position=extras.getInt("position");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        fromlayout= (LinearLayout) findViewById(R.id.fromlayout);
        reasonlayout= (LinearLayout) findViewById(R.id.reasonlayout);
        stnameValue= (TextView) findViewById(R.id.stnameValue);
        leaveDateFromValue= (TextView) findViewById(R.id.leaveDateFromValue);
        leaveDateToValue= (TextView) findViewById(R.id.leaveDateToValue);
        reasonValue= (TextView) findViewById(R.id.reasonValue);

        headermanager = new HeaderManager(LeavesDetailActivity.this, "Leaves");
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
                stopDisconnectTimer();

                finish();
            }
        });


    }

    private void setValues(){
        stnameValue.setText(AppPreferenceManager.getStudentName(mContext));
        leaveDateFromValue.setText(AppUtilityMethods.dateParsingToDdmmYyyyAmPm(leavesModelArrayList.get(position).getFrom_date()));
        leaveDateToValue.setText(AppUtilityMethods.dateParsingToDdmmYyyyAmPm(leavesModelArrayList.get(position).getTo_date()));
        reasonValue.setText(leavesModelArrayList.get(position).getReason());    }


    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(LeavesDetailActivity.this, false);
            AppPreferenceManager.setUserId(LeavesDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(LeavesDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(LeavesDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(LeavesDetailActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(LeavesDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
