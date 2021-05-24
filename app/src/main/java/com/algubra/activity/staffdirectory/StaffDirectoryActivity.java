package com.algubra.activity.staffdirectory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.staffdirectory.adapter.StaffDirectoryAdapter;
import com.algubra.activity.staffdirectory.model.StaffModel;
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
 * Created by gayatri on 2/5/17.
 */
public class StaffDirectoryActivity extends Activity implements JsonTagConstants, URLConstants, StausCodes {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, banner_img;
     Context mContext;
     Activity mActivity;
    private ArrayList<StaffModel> mStaffModelArrayList = new ArrayList<>();
    private ArrayList<StaffModel> mStaffModelListArrayList;

    RecyclerView staffDirectoryList;
    CustomProgressBar pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_directory);
        mContext = this;
        mActivity = this;
        initUI();
        resetDisconnectTimer();

        //getStaffList(URL_GET_STAFFLIST);
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            getStaffList(URL_GET_STAFFLIST);
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void initUI() {
        pDialog = new CustomProgressBar(mContext,
                R.drawable.spinner);
        pDialog.show();
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        banner_img = (ImageView) findViewById(R.id.bannerImage);
        headermanager = new HeaderManager(StaffDirectoryActivity.this, getString(R.string.staff_directory));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        staffDirectoryList = (RecyclerView) findViewById(R.id.staffDirectoryList);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerview_about_us_list.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        staffDirectoryList.addItemDecoration(itemDecoration);
        staffDirectoryList.setLayoutManager(llm);
        staffDirectoryList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), staffDirectoryList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        stopDisconnectTimer();
                        Intent intent = new Intent(StaffDirectoryActivity.this, StaffListActivity.class);
                        intent.putExtra("title", mStaffModelArrayList.get(position).getStaff_dept());
                        intent.putExtra("array", mStaffModelArrayList.get(position).getStaffModelArrayList());
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }

    private void getStaffList(String URL_LOGIN) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_LOGIN);
        String[] name = {"access_token", "boardId"};
        String boardId = "";
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            boardId = "1";
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            boardId = "2";
        } else {
            boardId = "1";
        }
        String[] value = {AppPreferenceManager.getAccessToken(mContext), boardId};

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
                            JSONArray staffArray = secobj.getJSONArray(JTAG_STAFF);
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
                            }
                            System.out.println("Preference ---" + staffArray.toString());
                            AppPreferenceManager.setStudentsResponseFromLoginAPI(mContext, staffArray.toString());
                            if (staffArray.length() > 0) {
                                // studentModelsArrayList.clear();
                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                    StaffModel staffModel = new StaffModel();
                                    staffModel.setStaff_dept(sObject.optString(JTAG_CATEGORY));
                                    JSONArray stffListArray = staffArray.getJSONObject(i).getJSONArray(JTAG_STAFFLIST);
                                    mStaffModelListArrayList = new ArrayList<>();

                                    //mStaffModelListArrayList.clear();

                                    for (int j = 0; j < stffListArray.length(); j++) {
                                        JSONObject staffObj = stffListArray.getJSONObject(j);
                                        StaffModel stModels = new StaffModel();
                                        stModels.setStaff_id(staffObj.optString(JTAG_ID));
                                        stModels.setStaff_name(staffObj.optString(JTAG_NAME));
                                        stModels.setStaff_phone(staffObj.optString(JTAG_PHONE));
                                        stModels.setStaff_email(staffObj.optString(JTAG_EMAIL));
                                        stModels.setStaff_about(staffObj.optString(JTAG_ABOUT));
                                        stModels.setStaff_subject(staffObj.optString(JTAG_SUBJECT));
                                        stModels.setStaff_staff_photo(staffObj.optString(JTAG_STAFFPHOTO));
                                        stModels.setStaff_class(staffObj.optString(JTAG_STAFFCLASS));
                                        stModels.setStaff_section(staffObj.optString(JTAG_STAFFSECTION));
                                        mStaffModelListArrayList.add(stModels);
                                    }
                                    staffModel.setStaffModelArrayList(mStaffModelListArrayList);
                                    mStaffModelArrayList.add(staffModel);
                                }

                                StaffDirectoryAdapter staffListAdapter = new StaffDirectoryAdapter(mContext, mStaffModelArrayList);
                                staffDirectoryList.setAdapter(staffListAdapter);

                            }

                            /*if (studentModelsArrayList.size() == 1) {
                                if (studentModelsArrayList.get(0).getBoardid().equals("1")) {
                                    String selectedFromList = "ISG";
                                    AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                                } else {
                                    String selectedFromList = "ISG-INT";
                                    AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                                }
                                Intent intent = new Intent(mContext, HomeActivity.class);
                                AppPreferenceManager.setIsGuest(mContext, false);
                                AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, true);
                                startActivity(intent);
                                finish();
                            } else {
                                showStudentList(studentModelsArrayList);

                            }*/
                            //showDialogSignUpAlert((Activity) mContext, getString(R.string.success), getString(R.string.login_success_alert), R.drawable.tick,  R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSERNAMEORPASWD)) {

                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_usr_pswd), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext,  getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.common_error), getString(R.string.invalid_usr_pswd), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                // sendLoginToServer(URL_PARENT_LOGIN);

                            }
                        });
                        getStaffList(URL_GET_STAFFLIST);

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

   /* public static final long DISCONNECT_TIMEOUT = 300000; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(StaffDirectoryActivity.this, false);
            AppPreferenceManager.setUserId(StaffDirectoryActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(StaffDirectoryActivity.this, false);
            AppPreferenceManager.setSchoolSelection(StaffDirectoryActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(StaffDirectoryActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };
*/
    public void resetDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity. disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
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
            Intent mIntent = new Intent(StaffDirectoryActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
