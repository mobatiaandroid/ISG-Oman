package com.algubra.activity.studentprofile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.leaves.LeavesActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONObject;

/**
 * Created by gayatri on 17/5/17.
 */
public class VerifyOtp extends Activity implements JsonTagConstants,URLConstants,StausCodes{
    private Context mContext=this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    EditText editTextPassword;
    Button btnSubmit;
    TextView textViewResendPassword;
    Bundle extras;
    String mapId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_login);
        initUI();
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            mapId=extras.getString("mapid");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        textViewResendPassword= (TextView) findViewById(R.id.textViewResendPassword);
        textViewResendPassword.setVisibility(View.GONE);
        editTextPassword.setHint("Enter your otp");
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        //editTextPassword.setTransformationMethod(Tex.getInstance());
                btnSubmit= (Button) findViewById(R.id.btnSubmit);
        headermanager = new HeaderManager(VerifyOtp.this, "Verify Otp");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            btnSubmit.setBackgroundResource(R.drawable.rounded_corner_loginbtn_bg);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            btnSubmit.setBackgroundResource(R.drawable.rounded_button_bg_blue);

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(editTextPassword.getText().toString().equals("")){
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_otp), R.drawable.infoicon,  R.drawable.roundblue);

        }else{
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                submitLeave(URL_VERIFY_OTP);
            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
            }
        }
    }
});
    }

    private void submitLeave(String URL_API) {

        VolleyAPIManager volleyWrapper=new VolleyAPIManager(URL_API);
        String[] name={"access_token","parentId","otp","mapId"};
        String[] value={AppPreferenceManager.getAccessToken(mContext),AppPreferenceManager.getUserId(mContext),editTextPassword.getText().toString(),mapId};

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
                            showDialogSuccess((Activity) mContext, "Success", getString(R.string.succ_add_student), R.drawable.tick,  R.drawable.roundblue);


                            // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick,  R.drawable.roundblue);
                        } else if(status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon,  R.drawable.roundblue);

                        }else if(status_code.equalsIgnoreCase(STATUS_ERROR_OCCURED)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.error_occurreds), R.drawable.infoicon,  R.drawable.roundblue);

                        }else if(status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.student_exists), R.drawable.infoicon,  R.drawable.roundblue);

                        }else{
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), mContext.getString(R.string.internal_server_error), R.drawable.exclamationicon,  R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        submitLeave(URL_VERIFY_OTP);

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

    public  void showDialogSuccess(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, StudentProfileListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();

    }

}
