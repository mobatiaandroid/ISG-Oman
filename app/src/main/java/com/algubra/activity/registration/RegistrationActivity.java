package com.algubra.activity.registration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.ConfirmLogin;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

/**
 * Created by gayatri on 17/4/17.
 */
public class RegistrationActivity extends Activity implements JsonTagConstants,URLConstants,StausCodes{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext=this;
    EditText editTextName,editPhoneNo,editTextStudentID,editTextEmailID,editTextPassword,editTextConfirmPassword;
    Button btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        editTextName= (EditText) findViewById(R.id.editTextName);
        editPhoneNo= (EditText) findViewById(R.id.editPhoneNo);
        editTextStudentID= (EditText) findViewById(R.id.editTextStudentID);
        editTextEmailID= (EditText) findViewById(R.id.editTextEmailID);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword= (EditText) findViewById(R.id.editTextConfirmPassword);
        btnContinue= (Button) findViewById(R.id.btnContinue);
        headermanager = new HeaderManager(RegistrationActivity.this, getString(R.string.registration));
        headermanager.getHeader(relativeHeader, 2);
        back = headermanager.getLeftButton();
//        headermanager.setButtonLeftSelector(R.drawable.homeicon,
//                R.drawable.homeicon);
        headermanager.setButtonLeftSelector(R.drawable.arrowblack,
                R.drawable.arrowblack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText().toString().trim().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_name), R.drawable.infoicon,  R.drawable.roundblue);

                } else if (editPhoneNo.getText().toString().trim().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phoneno), R.drawable.infoicon,  R.drawable.roundblue);

                } else if (editPhoneNo.getText().toString().trim().length()<8) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phone_eight_digitno), R.drawable.infoicon,  R.drawable.roundblue);

                }else if (editTextEmailID.getText().toString().trim().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.infoicon,  R.drawable.roundblue);

                } else if (!AppUtilityMethods.isValidEmail(editTextEmailID.getText().toString())) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.invalid_email), R.drawable.infoicon,  R.drawable.roundblue);

                } else if (editTextStudentID.getText().toString().trim().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_studentid), R.drawable.infoicon,  R.drawable.roundblue);

                } else {
                    if(AppUtilityMethods.isNetworkConnected(mContext)) {
                        callRegisterAPI(URL_PARENT_REGISTRATION);
                    }else{
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                    //showDialogRegSuccessAlert((Activity) mContext, getString(R.string.alert_heading), getString(R.string.req_success), R.drawable.tick,  R.drawable.roundblue);

                }
               /* Intent intent=new Intent(mContext, ConfirmLogin.class);
                startActivity(intent);
                finish();*/
            }
        });
    }

    public  void showDialogRegSuccessAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
                Intent intent=new Intent(mContext, ConfirmLogin.class);
                startActivity(intent);
                //finish();
            }
        });

        dialog.show();

    }



    private void callRegisterAPI(String URL_LOGIN){
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(URL_LOGIN);
        String[] name={"access_token","parent_name","parent_email","parent_phone","student_gr_no","device_type","device_id"};
        String[] value={AppPreferenceManager.getAccessToken(mContext),editTextName.getText().toString(),editTextEmailID.getText().toString(),editPhoneNo.getText().toString(),editTextStudentID.getText().toString(),"2", FirebaseInstanceId.getInstance().getToken()};

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
                            Intent intent=new Intent(RegistrationActivity.this,ConfirmLogin.class);
                            intent.putExtra("mobileno",editPhoneNo.getText().toString());
                            intent.putExtra("STATUSCODE_REG_SUCC_EMAILNOTLINKED",STATUSCODE_SUCCESS);

                            startActivity(intent);
                            AppPreferenceManager.setMobileNo(mContext,editPhoneNo.getText().toString());
                            finish();
                            //AppUtilityMethods .showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.signup_success_alert), R.drawable.tick,  R.drawable.roundblue);
                        }else if(status_code.equalsIgnoreCase(STATUSCODE_ALREADYREGISTERED)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.already_registered), R.drawable.infoicon,  R.drawable.roundblue);

                        }else if(status_code.equalsIgnoreCase(STATUSCODE_GRNO_NOTVALID)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.invalid_grno), R.drawable.infoicon,  R.drawable.roundblue);

                        }else if(status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSER)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.invalid_user), R.drawable.infoicon,  R.drawable.roundblue);

                        }else if(status_code.equals(STATUSCODE_INVALIDUSERNAMEORPASWD)){
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.invalid_usr_pswd), R.drawable.infoicon,  R.drawable.roundblue);
                        }else if(status_code.equalsIgnoreCase(STATUSCODE_REG_SUCC_EMAILNOTLINKED)){
                            Intent intent=new Intent(RegistrationActivity.this,ConfirmLogin.class);
                            intent.putExtra("mobileno",editPhoneNo.getText().toString());
                            intent.putExtra("STATUSCODE_REG_SUCC_EMAILNOTLINKED",STATUSCODE_REG_SUCC_EMAILNOTLINKED);
                            startActivity(intent);
                            AppPreferenceManager.setMobileNo(mContext,editPhoneNo.getText().toString());
                            finish();
//                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.regstered_butnoemail), R.drawable.infoicon,  R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon,  R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)||response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)||response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                callRegisterAPI(URL_PARENT_REGISTRATION);
                            }
                        });
                    }else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity)mContext,"Alert", mContext.getString(R.string.common_error),R.drawable.exclamationicon, R.drawable.roundblue);

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
                AppUtilityMethods.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });


    }

}
