package com.algubra.activity.leaves;

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
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.leaves.adapter.LeavesAdapter;
import com.algubra.activity.leaves.model.LeavesModel;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 5/5/17.
 */
public class LeavesActivity  extends Activity implements StausCodes,JsonTagConstants,URLConstants{
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, addLeaves;
    Context mContext = this;
    ArrayList<LeavesModel> listLeaves=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUI();
        resetDisconnectTimer();

        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getLeaveList(URL_GET_LEAVEREQUESTS);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void getLeaveList(String urlGetTimetableList) {
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(urlGetTimetableList);
        String[] name={"access_token","studentId","parentId"};

        String[] value={AppPreferenceManager.getAccessToken(mContext),
                AppPreferenceManager.getStudentId(mContext),AppPreferenceManager.getUserId(mContext)
        };

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);

                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        System.out.println("The response is" + response_code);

                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {

                            JSONArray staffArray = secobj.getJSONArray(JTAG_REQUESTS);
                            System.out.println("The response is" + staffArray);

                            listLeaves.clear();
                            if (staffArray.length() > 0) {

                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                    LeavesModel leavesModel=new LeavesModel();
                                    leavesModel.setId(sObject.optString(JTAG_ID));
                                    leavesModel.setTo_date(sObject.optString(JTAG_TODATE));
                                    leavesModel.setFrom_date(sObject.optString(JTAG_FROMDATE));
                                    leavesModel.setReason(sObject.optString(JTAG_REASON));
                                    leavesModel.setStatus(sObject.optString(JTAG_SSTATUs));
                                    leavesModel.setCreated_time(sObject.optString(JTAG_CREATED_TIME));
                                    listLeaves.add(leavesModel);
                                }
                                /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
                                LeavesAdapter leavesAdapter=new LeavesAdapter(mContext,listLeaves);
                                messageList.setAdapter(leavesAdapter);
                            }else{
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
                            }
                        });
                        getLeaveList(URL_GET_LEAVEREQUESTS);

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

        messageList = (RecyclerView) findViewById(R.id.settingItemList);

        messageList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(llm);

        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        messageList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(LeavesActivity.this, "Leaves");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        addLeaves=headermanager.getRightButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        headermanager.setButtonRightSelector(R.drawable.addrectangle,
                R.drawable.addrectangle);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });
        addLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                Intent intent = new Intent(LeavesActivity.this, LeaveRequestSubmissionActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        messageList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), messageList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(LeavesActivity.this, LeavesDetailActivity.class);
                        intent.putExtra("array",listLeaves);
                        intent.putExtra("position",position);
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

        System.out.println("TestRestart:hshhs");
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(LeavesActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        } else {
            listLeaves.clear();
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                getLeaveList(URL_GET_LEAVEREQUESTS);
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
            AppPreferenceManager.setIsGuest(LeavesActivity.this, false);
            AppPreferenceManager.setUserId(LeavesActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(LeavesActivity.this, false);
            AppPreferenceManager.setSchoolSelection(LeavesActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(LeavesActivity.this, LoginActivity.class);
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
}
