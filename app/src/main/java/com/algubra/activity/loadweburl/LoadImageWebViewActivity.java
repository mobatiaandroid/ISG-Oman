package com.algubra.activity.loadweburl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

/**
 * Created by gayatri on 23/5/17.
 */
public class LoadImageWebViewActivity extends Activity implements
        JsonTagConstants,StausCodes,URLConstants {

    private Context mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private String mLoadUrl = "";
    Bundle extras;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    String tab_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_webview);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("url");
            tab_type=extras.getString("tab_type");
        }
        initialiseUI();
        resetDisconnectTimer();

        getWebViewSettings();
    }


    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Oct 30, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        headermanager = new HeaderManager(LoadImageWebViewActivity.this, tab_type);
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            headermanager.getHeader(relativeHeader, 1);
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            headermanager.getHeader(relativeHeader, 3);
        }        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                AppUtilityMethods.hideKeyBoard(mContext);
                finish();
            }
        });

    }

    /*******************************************************
     * Method name : getWebViewSettings Description : get web view settings
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void getWebViewSettings() {


        mProgressRelLayout.setVisibility(View.GONE);

        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setBackgroundColor(0X00000000);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient());
        mwebSettings = mWebView.getSettings();
        mwebSettings.setSaveFormData(true);
        mwebSettings.setSupportZoom(true);
        mwebSettings.setBuiltInZoomControls(true);
        mwebSettings.setDisplayZoomControls(false);
        mwebSettings.setPluginState(WebSettings.PluginState.ON);
        mwebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mwebSettings.setDomStorageEnabled(true);
        mwebSettings.setDatabaseEnabled(true);
        mwebSettings.setDefaultTextEncodingName("utf-8");
        mwebSettings.setLoadsImagesAutomatically(true);
        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        mWebView.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        mWebView.loadDataWithBaseURL(null, "<html><head></head><body><table style=\"width:100%; height:100%;\"><tr><td style=\"vertical-align:middle;vertical-align:middle;\"><img src=\"" + mLoadUrl + "\"></td></tr></table></body></html>", "html/css", "utf-8", null);
        mWebView.loadDataWithBaseURL(null, "<html><head></head><body><div style=\"width:100%; height:100%;\"><img src=\"" + mLoadUrl + "\" style=\"width:100%;margin-top:35%\"></div></body></html>", "html/css", "utf-8", null);


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
            AppPreferenceManager.setIsGuest(LoadImageWebViewActivity.this, false);
            AppPreferenceManager.setUserId(LoadImageWebViewActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(LoadImageWebViewActivity.this, false);
            AppPreferenceManager.setSchoolSelection(LoadImageWebViewActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(LoadImageWebViewActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(LoadImageWebViewActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}