package com.algubra.activity.aboutus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.aboutus.adapter.AboutusRecyclerviewAdapter;
import com.algubra.activity.aboutus.model.AboutUsModel;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/4/17.
 */
public class AboutusRecyclerActivity extends Activity implements URLConstants,JsonTagConstants,StausCodes{
RecyclerView recyclerview_about_us_list;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView backImgView,banner_img;
    Context mContext;
    ArrayList<AboutUsModel> dataArrayStrings=new ArrayList<AboutUsModel>();
    CustomProgressBar pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initUI();
        resetDisconnectTimer();

        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            pDialog = new CustomProgressBar(mContext,
                    R.drawable.spinner);
            pDialog.show();
            getAboutUsList(URL_ABOUTUS);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initUI() {

        mContext=this;

        recyclerview_about_us_list= (RecyclerView) findViewById(R.id.recyclerview_about_us_list);
        banner_img= (ImageView) findViewById(R.id.about_us_bannerImgView);

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        recyclerview_about_us_list.setHasFixedSize(true);
        headermanager = new HeaderManager(AboutusRecyclerActivity.this, "About Us");
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            headermanager.getHeader(relativeHeader, 1);
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            headermanager.getHeader(relativeHeader, 3);
        }
        //headermanager.getHeader(relativeHeader, 0);
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

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerview_about_us_list.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        recyclerview_about_us_list.addItemDecoration(itemDecoration);
        recyclerview_about_us_list.setLayoutManager(llm);
        recyclerview_about_us_list.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), recyclerview_about_us_list,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        stopDisconnectTimer();
                        Intent intent = new Intent(AboutusRecyclerActivity.this, AboutUsDetailActivity.class);
                        intent.putExtra("content", dataArrayStrings.get(position).getContent());
                        intent.putExtra("title", dataArrayStrings.get(position).getTitle());
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));

    }
    private void getAboutUsList(final String URL_ABOUTUS){
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(URL_ABOUTUS);
        String[] name={"access_token","boardId"};
        String boardId="";
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            boardId="1";
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            boardId="2";
        }else{
            boardId="1";
        }
        String[] value={AppPreferenceManager.getAccessToken(mContext),boardId};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
                            JSONArray staffArray = secobj.getJSONArray(JTAG_ABOUT);
                            if (!secobj.optString(JTAG_BANNER_IMG).equals("")) {
                                Picasso.with(mContext).load(secobj.optString(JTAG_BANNER_IMG).replaceAll(" ", "%20")).fit()
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
                            } else {
                                pDialog.dismiss();
                            }
                            System.out.println("Preference ---" + staffArray.toString());
                            AppPreferenceManager.setStudentsResponseFromLoginAPI(mContext, staffArray.toString());
                            if (staffArray.length() > 0) {
                                // studentModelsArrayList.clear();
                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                    AboutUsModel aboutUsModel = new AboutUsModel();
                                    aboutUsModel.setTitle(sObject.optString(JTAG_TITLE));
                                    aboutUsModel.setContent(sObject.optString(JTAG_CONTENT));
                                    dataArrayStrings.add(aboutUsModel);
                                }
                                AboutusRecyclerviewAdapter mAboutusRecyclerviewAdapter = new AboutusRecyclerviewAdapter(mContext, dataArrayStrings);
                                recyclerview_about_us_list.setAdapter(mAboutusRecyclerviewAdapter);
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon,  R.drawable.roundblue);

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon,  R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                getAboutUsList(URL_ABOUTUS);
                            }
                        });
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);

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
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);

            }
        });


    }
/*    public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(AboutusRecyclerActivity.this, false);
            AppPreferenceManager.setUserId(AboutusRecyclerActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(AboutusRecyclerActivity.this, false);
            AppPreferenceManager.setSchoolSelection(AboutusRecyclerActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {
                Intent mIntent = new Intent(AboutusRecyclerActivity.this, LoginActivity.class);
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
  @Override protected void onRestart() { super.onRestart();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(AboutusRecyclerActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}