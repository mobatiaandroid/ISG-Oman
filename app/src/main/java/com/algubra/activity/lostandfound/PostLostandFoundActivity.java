package com.algubra.activity.lostandfound;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONObject;

/**
 * Created by gayatri on 19/5/17.
 */
public class PostLostandFoundActivity  extends Activity implements JsonTagConstants,StausCodes,URLConstants {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, about_us_bannerImgView,addItem;
    Context mContext = this;
    EditText enterSubject,enterStratDate,contactEmail,enterEndDate,enterMessage;
    Button submitBtn;
    Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_lostandfound);
        initUI();
        resetDisconnectTimer();

       /* if(AppUtilityMethods.isNetworkConnected(mContext)){
            getList(URL_GETLOSTANDFOUND);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }*/
    }
    private void initUI() {

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        enterSubject = (EditText) findViewById(R.id.enterSubject);
        enterStratDate = (EditText) findViewById(R.id.enterStratDate);
        contactEmail = (EditText) findViewById(R.id.contactEmail);
        enterEndDate = (EditText) findViewById(R.id.enterEndDate);
        enterMessage = (EditText) findViewById(R.id.enterMessage);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        headermanager = new HeaderManager(PostLostandFoundActivity.this, "Add Lost & Found");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            submitBtn.setBackgroundColor(getResources().getColor(R.color.login_button_bg));

        } else {
            headermanager.getHeader(relativeHeader, 3);
            submitBtn.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));

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



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterSubject.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Enter title", R.drawable.infoicon,  R.drawable.roundblue);

                } else if (enterStratDate.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Enter contact name", R.drawable.infoicon,  R.drawable.roundblue);

                } else if (contactEmail.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Enter contact email", R.drawable.infoicon,  R.drawable.roundblue);

                } else if (enterEndDate.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Enter contact number", R.drawable.infoicon,  R.drawable.roundblue);

                } else if (enterMessage.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Enter description", R.drawable.infoicon,  R.drawable.roundblue);

                } else {
                    if (AppUtilityMethods.isNetworkConnected(mContext)) {
                        callAPI(URL_POST_LOSTANDFOUND);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                }
            }
        });
    }
    private void callAPI(String URL) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL);
        String[] name = {"access_token", "parentId", "title", "description","contact_person_name","contact_person_email","contact_person_phone"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext),
                enterSubject.getText().toString(),enterStratDate.getText().toString(),contactEmail.getText().toString(),enterEndDate.getText().toString(),
                enterMessage.getText().toString().trim()};

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
                            /*JSONArray responseArray = secobj.getJSONArray(JTAG_RESPONSE_ARRAY);
							for (int i = 0; i < responseArray.length(); i++) {
								JSONObject respObj = responseArray.getJSONObject(i);
								PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USERS_ID));
							}*/
//dialog.dismiss();

                            Log.d("TAG", "Inside response success---");
                            showDialogSignUpAlert((Activity) mContext, "Success", "Successfully posted", R.drawable.tick,  R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_PASSWORD_MISMATCH)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon,  R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_user), R.drawable.infoicon,  R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        callAPI(URL_POST_LOSTANDFOUND);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
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

    public  void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        Log.d("TAG","Inside response success---showDialogSignUpAlert");

        dialog1 = new Dialog(activity);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.alert_dialog_ok_layout);
        ImageView icon = (ImageView) dialog1.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog1.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog1.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);
        Button dialogButton = (Button) dialog1.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1.dismiss();
                Intent mIntent = new Intent(activity, LostandFoundListActivity.class);
                activity.startActivity(mIntent);
                finish();
            }
        });

        dialog1.show();

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
            AppPreferenceManager.setIsGuest(PostLostandFoundActivity.this, false);
            AppPreferenceManager.setUserId(PostLostandFoundActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(PostLostandFoundActivity.this, false);
            AppPreferenceManager.setSchoolSelection(PostLostandFoundActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(PostLostandFoundActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(PostLostandFoundActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
