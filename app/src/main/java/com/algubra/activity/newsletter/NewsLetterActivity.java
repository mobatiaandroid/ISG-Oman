package com.algubra.activity.newsletter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.loadweburl.LoadWebViewActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.newsletter.adapter.NewsLetterAdapter;
import com.algubra.activity.newsletter.model.NewsLetterModel;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.CustomProgressBar;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 4/5/17.
 */
public class NewsLetterActivity extends Activity implements JsonTagConstants, StausCodes, URLConstants {
    HeaderManager headermanager;
    RelativeLayout relativeHeader, mProgressRelLayout;
    ImageView backImgView;
    Context mContext;
    RecyclerView listViewNews;
    ImageView back, banner_img;
    ArrayList<NewsLetterModel> newsLetterModelArrayList = new ArrayList<>();
    CustomProgressBar pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letter);
        mContext = this;
        initUI();
        resetDisconnectTimer();

        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            getNewsList(URL_GET_NEWSLETTERS);
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initUI() {
//        pDialog = new CustomProgressBar(mContext,
//                R.drawable.spinner);
//        pDialog.show();
        listViewNews = (RecyclerView) findViewById(R.id.recyclerNewsLetter);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        banner_img = (ImageView) findViewById(R.id.about_us_bannerImgView);
        banner_img.setVisibility(View.GONE);
        /*headermanager = new HeaderManager(NewsLetterActivity.this, "News Letters");
        headermanager.getHeader(relativeHeader, 0);*/
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listViewNews.setLayoutManager(llm);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        listViewNews.addItemDecoration(itemDecoration);

        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager = new HeaderManager(NewsLetterActivity.this, getString(R.string.phantas_magoria));
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager = new HeaderManager(NewsLetterActivity.this, getString(R.string.i_learn));
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

        listViewNews.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), listViewNews,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if(newsLetterModelArrayList.get(position).getNewsPdfLink().equalsIgnoreCase("")) {
                            stopDisconnectTimer();
                            Intent intent = new Intent(NewsLetterActivity.this, NewsLetterDetailActivity.class);
                            intent.putExtra("array", newsLetterModelArrayList);
                            intent.putExtra("position", position);
                            startActivity(intent);
                        }
                        else
                        {
                            /*Intent intent = new Intent(NewsLetterActivity.this, PdfReader.class);
                            intent.putExtra("pdf_url",newsLetterModelArrayList.get(position).getNewsPdfLink());
                            startActivity(intent);*/
                            if (!(newsLetterModelArrayList.get(position).getNewsPdfLink().equalsIgnoreCase(""))) {
                                Intent browserIntent = new Intent(NewsLetterActivity.this, PDFViewActivity.class);
                                browserIntent.putExtra("pdf_url",newsLetterModelArrayList.get(position).getNewsPdfLink());
                                browserIntent.putExtra("title","News Letter");
                                browserIntent.putExtra("filename",newsLetterModelArrayList.get(position).getTitle());
                                startActivity(browserIntent);

//                                                                if (newsLetterModelArrayList.get(position).getNewsPdfLink().endsWith(".pdf")) {
//                                    Intent browserIntent = new Intent(NewsLetterActivity.this, PDFViewActivity.class);
//                                    browserIntent.putExtra("pdf_url",newsLetterModelArrayList.get(position).getNewsPdfLink());
//                                    browserIntent.putExtra("title","News Letter");
//                                    System.out.println("URLDATAPDFTYPE");
//                                    browserIntent.putExtra("filename",newsLetterModelArrayList.get(position).getTitle());
//                                    startActivity(browserIntent);
//
//
//                                } else {
//                                    Intent intent = new Intent(mContext, LoadWebViewActivity.class);
//                                    System.out.println("URLDATAWEBTYPE");
//                                    intent.putExtra("url", newsLetterModelArrayList.get(position).getNewsWebLink());
//                                    intent.putExtra("tab_type", newsLetterModelArrayList.get(position).getTitle());
//                                    mContext.startActivity(intent);
//                                }

                            }
                            else
                            {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.nomoreinfo), R.drawable.exclamationicon, R.drawable.roundblue);

                            }
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsLetterModelArrayList.get(position).getNewsPdfLink()));
//                            startActivity(browserIntent);
                        }

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }

    private void getNewsList(final String urlGetEventsList) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(urlGetEventsList);
        String[] name = {"access_token", "boardId"};
        String boardId = "";
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            boardId = "1";
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            boardId = "2";
        } else {
            boardId = "1";
        }
        String[] value = {AppPreferenceManager.getAccessToken(mContext), boardId,
        };

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response of Newsletter is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);

                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        System.out.println("The response is" + response_code);

                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
                            //Glide.with(mContext).load(secobj.optString(JTAG_BANNER_IMG))
                            // .placeholder(R.drawable.aboutbanner).dontAnimate().into(banner_img);
                          /*  if (!secobj.optString(JTAG_BANNER_IMG).equals("")) {
                                Picasso.with(mContext).load(secobj.optString(JTAG_BANNER_IMG).replace(" ", "%20")).fit()
                                        .into(banner_img, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                pDialog.dismiss();
                                            }

                                            @Override
                                            public void onError() {
                                                pDialog.dismiss();
                                            }
                                        });
                            }*/
                            JSONArray staffArray = secobj.getJSONArray(JTAG_SPECIALEXAMS);
                            System.out.println("The response is" + staffArray);

                            newsLetterModelArrayList.clear();
                            if (staffArray.length() > 0) {

                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                    NewsLetterModel newsLetterModel = new NewsLetterModel();
                                    newsLetterModel.setNewsId(sObject.optString(JTAG_ID));
                                    newsLetterModel.setNewsTitle(sObject.optString(JTAG_TITLE));
                                    newsLetterModel.setNewsDescription(sObject.optString(JTAG_DESCRIPTION));
                                    newsLetterModel.setNewsImage(sObject.optString(JTAG_IMAGE));
                                    newsLetterModel.setNewsCreatedTime(sObject.optString(JTAG_CREATED_TIME));
                                    newsLetterModel.setNewsWebLink(sObject.optString(JTAG_WEBLINK));
                                    newsLetterModel.setNewsPdfLink(sObject.optString(JTAG_PDF_LINK));

                                    System.out.println("URLDATAWEB:+"+newsLetterModel.getNewsWebLink());
                                    System.out.println("URLDATAPDF:+"+newsLetterModel.getNewsPdfLink());

                                    newsLetterModelArrayList.add(newsLetterModel);
                                }
                                NewsLetterAdapter mNewsLetterAdapter = new NewsLetterAdapter(mContext, newsLetterModelArrayList);
                                listViewNews.setAdapter(mNewsLetterAdapter);
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon, R.drawable.roundblue);

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getNewsList(URL_GET_NEWSLETTERS);

                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                        , getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });


    }

    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(NewsLetterActivity.this, false);
            AppPreferenceManager.setUserId(NewsLetterActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(NewsLetterActivity.this, false);
            AppPreferenceManager.setSchoolSelection(NewsLetterActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(NewsLetterActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(NewsLetterActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
