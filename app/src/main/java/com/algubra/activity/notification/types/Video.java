package com.algubra.activity.notification.types;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class Video extends  Activity implements View.OnClickListener,
        JsonTagConstants, URLConstants, StausCodes {

    ArrayList<NotificationModel> videolist;
    WebView webView;
    int position;
    ProgressBar proWebView;

    ImageView back;
    Activity mActivity;
    TextView textcontent;

    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videopush_webview);
        mActivity = this;

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            position = extra.getInt("position");

            videolist = (ArrayList<NotificationModel>) extra
                    .getSerializable("array");
        }		webView = (WebView) findViewById(R.id.webView);

        proWebView = (ProgressBar) findViewById(R.id.proWebView);
        textcontent = (TextView) findViewById(R.id.txtContent);
        textcontent.setVisibility(View.INVISIBLE);

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(mActivity, "Notification");

        if (AppPreferenceManager.getSchoolSelection(mActivity).equals("ISG")) {
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
                finish();
            }
        });

        webView.setWebViewClient(new HelloWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//	    int height = displaymetrics.heightPixels;
        String frameVideo = "<html>"+"<br><iframe width=\"100%\" height=\"auto\" src=\"";
        String url_Video=frameVideo+videolist.get(position).getUrl()+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        String urlYoutube = url_Video.replace("watch?v=", "embed/");
        System.out.println("urlYoutube:"+urlYoutube);
        webView.loadData(urlYoutube, "text/html", "utf-8");

//		webView.loadUrl(videolist.get(position).getUrl());
        textcontent.setText(videolist.get(position).getMessage());
        proWebView.setVisibility(View.VISIBLE);

    }


    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            proWebView.setVisibility(View.GONE);
            textcontent.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}