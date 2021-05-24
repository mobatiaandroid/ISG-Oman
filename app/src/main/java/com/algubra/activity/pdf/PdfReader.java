package com.algubra.activity.pdf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;


public class PdfReader extends Activity implements
        JsonTagConstants, URLConstants {

    private Context mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private String pdfUrl = null;
    private boolean mErrorFlag = false;
    Bundle extras;
    ImageView backImageView;
    ImageView pdfDownloadImgView;
    RotateAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view_layout);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("pdf_url");
            pdfUrl =  extras.getString("pdf_url");
        }
        resetDisconnectTimer();

//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();
        getWebViewSettings();
    }


    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Oct 30, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {

        mWebView = (WebView) findViewById(R.id.webView);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        pdfDownloadImgView = (ImageView) findViewById(R.id.pdfDownloadImgView);
    }

    /*******************************************************
     * Method name : getWebViewSettings Description : get web view settings
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void getWebViewSettings() {

        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });
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
        mWebView.setBackgroundColor(0Xffffffff);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebChromeClient(new WebChromeClient());
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        mwebSettings = mWebView.getSettings();
        mwebSettings.setSaveFormData(true);
        mwebSettings.setBuiltInZoomControls(false);
        mwebSettings.setSupportZoom(false);

        mwebSettings.setPluginState(PluginState.ON);
        mwebSettings.setRenderPriority(RenderPriority.HIGH);
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
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);

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
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtilityMethods.isNetworkConnected(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    view.loadUrl(url);
                    loadingFlag = false;
                } else if (!AppUtilityMethods.isNetworkConnected(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);
                    view.loadUrl(url);
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
                            getResources().getString(R.string.ok), false);
                }

                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        pdfDownloadImgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                   /* Intent browserIntent = new Intent(CircularActivity.this, PDFViewActivity.class);
                    browserIntent.putExtra("pdf_url",listTimetable.get(position).getPdf_link());
                    browserIntent.putExtra("title","Circular");
                    startActivity(browserIntent);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)));*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(mLoadUrl);
                sharingIntent.setType("application/pdf");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));*/

            }
        });
        if (mLoadUrl.endsWith(".pdf")) {
            //mLoadUrl=mLoadUrl;
            mLoadUrl = "http://docs.google.com/gview?embedded=true&url=" + mLoadUrl;
//             mLoadUrl="<iframe src="+"'http://docs.google.com/gview?embedded=true&url="+AppUtils.replace(mLoadUrl)+"'width='100%' height='100%'style='border: none;'></iframe>";

        }else{
            mLoadUrl=mLoadUrl;
        }
        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("NAS load url " + mLoadUrl);
            mWebView.loadUrl(mLoadUrl);
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtilityMethods.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_pdf), "",
                    getResources().getString(R.string.ok), false);
        }
//		GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//				+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//		getWebUrl.execute();
    }

//	private class GetWebUrlAsyncTask extends AsyncTask<Void, Integer, Void> {
//		private String tabId;
//		private String url;
//		private String dirName;
//		private int isCache;
//		private InternetManager manager;
//		private WebContentApi webApi;
//
//		public GetWebUrlAsyncTask(String url, String dirName, int isCache,
//				String tabId) {
//			this.url = url;
//			this.dirName = dirName;
//			this.isCache = isCache;
//			this.tabId = tabId;
//			manager = new InternetManager(url, mContext, dirName, isCache);
//			webApi = new WebContentApi(mContext, manager, tabId);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			mProgressRelLayout.setVisibility(View.VISIBLE);
//		}
//
//		/*
//		 * (non-Javadoc)
//		 *
//		 * @see android.os.AsyncTask#doInBackground(Params[])
//		 */
//		@Override
//		protected Void doInBackground(Void... params) {
//			mLoadUrl = webApi.getWebContentApiResponse();
//			if (mLoadUrl.endsWith(".pdf")) {
//				mLoadUrl = "http://docs.google.com/gview?embedded=true&url=" + mLoadUrl;
//			}
//			if (mLoadUrl.equals("")) {
//				mErrorFlag = true;
//			} else {
//				mErrorFlag = false;
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void arg0) {
//			super.onPostExecute(arg0);
//			if (mLoadUrl != null && !mErrorFlag) {
//				System.out.println("wiss load url " + mLoadUrl);
//				mWebView.loadUrl(mLoadUrl);
//			}
//
//		}
//	}


    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(PdfReader.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
        else {
            mLoadUrl = pdfUrl;
//        pdfUrl=mLoadUrl;
            getWebViewSettings();
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
            AppPreferenceManager.setIsGuest(PdfReader.this, false);
            AppPreferenceManager.setUserId(PdfReader.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(PdfReader.this, false);
            AppPreferenceManager.setSchoolSelection(PdfReader.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(PdfReader.this, LoginActivity.class);
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

}
