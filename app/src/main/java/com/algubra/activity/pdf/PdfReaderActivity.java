package com.algubra.activity.pdf;

import android.app.Activity;
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
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;


public class PdfReaderActivity extends Activity{

    private Activity mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private String pdfUrl = null;
    private boolean mErrorFlag = false;
    Bundle extras;
    ImageView backImageView,back;
    ImageView pdfDownloadImgView;
    RotateAnimation anim;
    String title;
    String type;
//    String url;
RelativeLayout relativeHeader;
    HeaderManager headermanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view_layout_new);
        mContext = this;
        extras = getIntent().getExtras();
//        if (extras != null) {
//            mLoadUrl = AppUtilityMethods.replace(extras.getString("pdf_url").replace("&","%26"));
//            pdfUrl = AppUtilityMethods.replace(extras.getString("pdf_url").replace("&","%26"));
//        }

        if (extras != null) {
            mLoadUrl = extras.getString("pdf_url");
            title = extras.getString("title");
            type = extras.getString("type");
//            title = extras.getString("title");
//            name = extras.getString("filename");

        }
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
        pdfDownloadImgView.setVisibility(View.GONE);
        relativeHeader = findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(PdfReaderActivity.this, title);
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
               // stopDisconnectTimer();
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

        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(4000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setBackgroundColor(0Xffffff);
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
        mwebSettings.setAllowUniversalAccessFromFileURLs(true);
        mwebSettings.setAllowContentAccess(true);
        mwebSettings.setAllowFileAccessFromFileURLs(true);

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
//        if (mLoadUrl.endsWith(".pdf")) {
//            mLoadUrl = "http://docs.google.com/gview?embedded=true&url=" + mLoadUrl;
        if (type.equalsIgnoreCase("jpg")||type.equalsIgnoreCase("jpeg")||type.equalsIgnoreCase("JPG")||type.equalsIgnoreCase("JPEG")||type.equalsIgnoreCase("png")||type.equalsIgnoreCase("PNG"))
        {
            mLoadUrl = mLoadUrl;
        }
        else if (type.equalsIgnoreCase("drive"))
        {
            mLoadUrl=mLoadUrl;
            System.out.println("mLoadUrl"+mLoadUrl);
        }
        else
        {
            mLoadUrl = "https://view.officeapps.live.com/op/embed.aspx?src=" + mLoadUrl;

        }
//             mLoadUrl="<iframe src="+"'http://docs.google.com/gview?embedded=true&url="+AppUtils.replace(mLoadUrl)+"'width='100%' height='100%'style='border: none;'></iframe>";

//        }
        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("BISAD load url " + mLoadUrl);
            mWebView.loadUrl(mLoadUrl);
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtilityMethods.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_pdf), "",
                    getResources().getString(R.string.ok), false);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                System.out.println("===Page LOADING1111==="+url);

                if (mProgressRelLayout.getVisibility()== View.GONE)
                    mProgressRelLayout.setVisibility(View.VISIBLE);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                System.out.println("===Page LOADING2222===");


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
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)));

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });
    }

}
