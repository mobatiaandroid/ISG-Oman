package com.algubra.activity.specialmessages;

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
import com.algubra.activity.specialmessages.model.SpecialMessageModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 4/5/17.
 */
public class SpecialMessageDetailActivity extends Activity{
    String pushNotificationDetail;
    Bundle extras;
    ArrayList<SpecialMessageModel> specialMessageModelArrayList=new ArrayList<>();
    int position;
    private Context mContext=this;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private boolean mErrorFlag = false;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    RotateAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_webview);
        resetDisconnectTimer();

    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        headermanager = new HeaderManager(SpecialMessageDetailActivity.this, "Special Message");
        headermanager.getHeader(relativeHeader, 0);
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

    private void getWebViewSettings() {


        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setBackgroundColor(0X00000000);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient());
//        int sdk = Build.VERSION.SDK_INT;
//        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
//            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
//        }
        mwebSettings = mWebView.getSettings();
        mwebSettings.setSaveFormData(true);
        mwebSettings.setBuiltInZoomControls(false);
        mwebSettings.setSupportZoom(false);

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
        mLoadUrl=pushNotificationDetail;

//		refreshBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//						+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//				getWebUrl.execute();
//			}
//		});

        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                //view.loadData(mLoadUrl, "text/html", "UTF-8");
                view.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");

                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtilityMethods.isNetworkConnected(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//                    view.loadUrl(url);
                    // view.loadData(mLoadUrl, "text/html", "UTF-8");
                    view.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
                    loadingFlag = false;
                } else if (!AppUtilityMethods.isNetworkConnected(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//                    view.loadUrl(url);
                    //view.loadData(mLoadUrl, "text/html", "UTF-8");
                    view.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
                    System.out.println("CACHE LOADING");
                    loadingFlag = false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            /*
             * (non-Javadoc)
             *
             * @see
             * android.webkit.WebViewClient#onReceivedError(android.webkit.WebView
             * , int, java.lang.String, java.lang.String)
             */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    AppUtilityMethods.showAlertFinish((Activity) mContext, getResources()
                                    .getString(R.string.common_error), "",
                            "Ok", false);
                }

                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("NAS load url " + mLoadUrl);
            //mWebView.loadData(mLoadUrl, "text/html", "UTF-8");
            mWebView.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtilityMethods.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_page), "",
                    "Ok", false);
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
            AppPreferenceManager.setIsGuest(SpecialMessageDetailActivity.this, false);
            AppPreferenceManager.setUserId(SpecialMessageDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(SpecialMessageDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(SpecialMessageDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(SpecialMessageDetailActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(SpecialMessageDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
