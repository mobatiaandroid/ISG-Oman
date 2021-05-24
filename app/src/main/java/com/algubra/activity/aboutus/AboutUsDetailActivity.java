package com.algubra.activity.aboutus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
 * Created by gayatri on 28/4/17.
 */
public class AboutUsDetailActivity extends Activity{
    HeaderManager headermanager;
    RelativeLayout relativeHeader,mProgressRelLayout;
    ImageView backImgView;
    Context mContext=this;
    Bundle extras;
    String title,content;
    WebView web;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    String mLoadUrl;
    private boolean mErrorFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_detail);
        resetDisconnectTimer();
        initUI();
        getWebViewSettings();
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            title=extras.getString("title");
            content=extras.getString("content");
        }
        relativeHeader= (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(AboutUsDetailActivity.this, "About Us");
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            headermanager.getHeader(relativeHeader, 1);
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            headermanager.getHeader(relativeHeader, 3);
        }        mProgressRelLayout= (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);
        //web.setWebViewClient(new myWebClient());
        web= (WebView) findViewById(R.id.webView);
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
    }

    private void getWebViewSettings() {
        web.getSettings().setJavaScriptEnabled(true);
        web.setPadding(0, 0, 0, 0);
        web.setBackgroundColor(Color.TRANSPARENT);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(false);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
           // web.setBackgroundColor(Color.WHITE);
            web.loadUrl("about:blank");
        }
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            web.clearView();
        }

        if(content!=null){
            try {
                String head = "<head><style>@font-face {font-family: 'Roboto', sans-serif;src: url(\"https://fonts.googleapis.com/css?family=Roboto\");}body {font-family: 'verdana';}</style>"+title+"<br><br>"+content+"</head>";
                web.loadData(head, "text/html; charset=utf-8", "utf-8");
                web.setBackgroundColor(Color.TRANSPARENT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



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
            AppPreferenceManager.setIsGuest(AboutUsDetailActivity.this, false);
            AppPreferenceManager.setUserId(AboutUsDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(AboutUsDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(AboutUsDetailActivity.this,"ISG");
            if (AppUtilityMethods.isAppInForeground(mContext))
            {
             Intent mIntent = new Intent(AboutUsDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
            }
//            Intent mIntent = new Intent(AboutUsDetailActivity.this, LoginActivity.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(mIntent);
//            finish();
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

  @Override protected void onRestart() { super.onRestart();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(AboutUsDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }


}
