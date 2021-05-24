package com.algubra.activity.contactus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.contactus.adapter.ImageAdapter;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 31/5/17.
 */
public class FloorPlanActivity extends Activity {
    RelativeLayout relativeHeader, mProgressRelLayout;
    HeaderManager headermanager;
    ImageView back;
    ViewPager viewPager;
    ImageAdapter homeViewPagerAdapter;
    Context mContext=this;
    ArrayList<String> optString=new ArrayList<>();
    Bundle extras;
    public  static TextView indexOfFloorPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorplan);
        extras = getIntent().getExtras();
        if (extras != null) {
            optString = (ArrayList<String>) extras
                    .getSerializable("array");

        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        indexOfFloorPlan= (TextView) findViewById(R.id.indexOfFloorPlan);
        //mProgressRelLayout= (RelativeLayout) findViewById(R.id.progressDialog);
        //mProgressRelLayout.setVisibility(View.GONE);
        //web.setWebViewClient(new myWebClient());
        headermanager = new HeaderManager(FloorPlanActivity.this, "Floor Plan");
        headermanager.getHeader(relativeHeader, 0);
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
                //AppUtils.hideKeyBoard(mContext);
                stopDisconnectTimer();
                finish();
            }
        });

        homeViewPagerAdapter=new ImageAdapter(mContext,optString);
        //homeViewPagerAdapter=new HomeViewPagerAdapter_New(mContext,pagerCount,viewPager);

        viewPager.setAdapter(homeViewPagerAdapter);
        indexOfFloorPlan.setText(1+"/"+optString.size());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                indexOfFloorPlan.setText((position+1)+"/"+optString.size());

            }
        });
        resetDisconnectTimer();

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
            AppPreferenceManager.setIsGuest(FloorPlanActivity.this, false);
            AppPreferenceManager.setUserId(FloorPlanActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(FloorPlanActivity.this, false);
            AppPreferenceManager.setSchoolSelection(FloorPlanActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(FloorPlanActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(FloorPlanActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }

}
