package com.algubra.activity.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.events.model.EventModels;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 25/4/17.
 */
public class EventListActivity extends Activity implements URLConstants,JsonTagConstants,StausCodes{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    RecyclerView eventsList;
    public ArrayList<EventModels> eventModelsArrayList=new ArrayList<>();
    LinearLayout eventLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        initUI();
        resetDisconnectTimer();

        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            callEventListAPI(URL_GET_EVENTS_LIST);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void callEventListAPI(final String urlGetEventsList) {
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(urlGetEventsList);
        String[] name={"access_token","boardId","studentId","parentId"};
        String boardId="";
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            boardId="1";
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            boardId="2";
        }else{
            boardId="1";
        }
        String[] value={AppPreferenceManager.getAccessToken(mContext),boardId,
                AppPreferenceManager.getStudentId(mContext),AppPreferenceManager.getUserId(mContext)};

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
                            eventModelsArrayList.clear();
                            if (staffArray.length() > 0) {

                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                    EventModels eventModels=new EventModels();
                                    eventModels.setId(sObject.optString(JTAG_STUDENT_ID));
                                    eventModels.setName(sObject.optString(JTAG_NAME));
                                    eventModels.setDescription(sObject.optString(JTAG_DESCRIPTION));
                                    eventModels.setType(sObject.optString(JTAG_TYPE));
                                    eventModels.setPay_type(sObject.optString(JTAG_PAY_TYPE));
                                    eventModels.setAmount(sObject.optString(JTAG_AMOUNT));
                                    eventModels.setStart_date(sObject.optString(JTAG_START_DATE));
                                    eventModels.setEnd_date(sObject.optString(JTAG_END_DATE));
                                    eventModels.setEvent_id(sObject.optString(JTAG_ID));
                                    eventModels.setImage(sObject.optString(JTAG_IMAGE));
                                    eventModels.setPdf_link(sObject.optString(JTAG_REG_FORM));
                                    eventModels.setParent_consent_pdf_link(sObject.optString("parent_consent"));
                                    eventModelsArrayList.add(eventModels);
                                }
                                EventListAdapter mAboutusRecyclerviewAdapter=new EventListAdapter(mContext,eventModelsArrayList);
                                eventsList.setAdapter(mAboutusRecyclerviewAdapter);
                            }else{
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_eventfound), R.drawable.infoicon,  R.drawable.roundblue);

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
                            }
                        });
                        callEventListAPI(urlGetEventsList);

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



    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        eventsList = (RecyclerView) findViewById(R.id.mSporsEventListView);
        eventLayout= (LinearLayout) findViewById(R.id.eventLayout);
        eventsList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        eventsList.setLayoutManager(llm);
        int spacing = 10; // 50px
        /*ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        eventsList.addItemDecoration(itemDecoration);*/
        headermanager = new HeaderManager(EventListActivity.this, getString(R.string.events));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            eventLayout.setBackgroundResource(R.color.events_bg);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            eventLayout.setBackgroundResource(R.color.calendar_blue_selector);

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

        eventsList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), eventsList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        stopDisconnectTimer();
                        Intent intent = new Intent(EventListActivity.this, EventDetailsActivity.class);
                        intent.putExtra("position",position);
                        intent.putExtra("array", eventModelsArrayList);
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(EventListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
        else {
            eventModelsArrayList.clear();
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                callEventListAPI(URL_GET_EVENTS_LIST);
            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

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
            AppPreferenceManager.setIsGuest(EventListActivity.this, false);
            AppPreferenceManager.setUserId(EventListActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(EventListActivity.this, false);
            AppPreferenceManager.setSchoolSelection(EventListActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(EventListActivity.this, LoginActivity.class);
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
