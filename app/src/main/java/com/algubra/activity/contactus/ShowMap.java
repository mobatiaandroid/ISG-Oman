package com.algubra.activity.contactus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

/**
 * Created by gayatri on 3/5/17.
 */
public class ShowMap extends Activity{
    WebView viewmap;
    String latitude,longitude;
    Bundle extras;
    Context mContext=this;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView backImgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlocation);
        extras=getIntent().getExtras();
        if(extras!=null){
            latitude=extras.getString("latitude");
            longitude=extras.getString("longitude");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ShowMap.this, "Location");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        backImgView = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        backImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                AppUtilityMethods.hideKeyBoard(mContext);
                finish();
            }
        });
        viewmap= (WebView) findViewById(R.id.viewmap);
        resetDisconnectTimer();

        loadUrl();
    }

    private void loadUrl() {

        viewmap.getSettings().setJavaScriptEnabled(true);
        viewmap.setWebViewClient(new WebViewClient());
        viewmap.loadUrl("http://maps.google.com/maps?q="+latitude+","+longitude);
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
            AppPreferenceManager.setIsGuest(ShowMap.this, false);
            AppPreferenceManager.setUserId(ShowMap.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(ShowMap.this, false);
            AppPreferenceManager.setSchoolSelection(ShowMap.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(ShowMap.this, LoginActivity.class);
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
            Intent mIntent = new Intent(ShowMap.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
