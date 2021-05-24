package com.algubra.activity.splash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.service.OnClearFromRecentService;
import com.algubra.volleymanager.VolleyAPIManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

/**
 * Created by gayatri on 17/4/17.
 */
public class SplashActivity extends Activity implements URLConstants,JsonTagConstants,StausCodes {
Context mContext=this;
Activity mActivity;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mActivity=this;
      /*  if (Build.VERSION.SDK_INT < 23) {
            if(AppUtilityMethods.isNetworkConnected(mContext)) {
                getAccessToken();
System.out.println("Device id---"+ FirebaseInstanceId.getInstance().getToken());
                loadSplash();
            }else{
                showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
            }

        } else {
			*//*if (checkAndRequestPermissions()) {
				//If you have already permitted the permission
				goToNextView();
			}*//*
            if (hasPermissions(mContext, permissions)) {

                if(AppUtilityMethods.isNetworkConnected(mContext)) {
                    getAccessToken();
//                    permissionTo=false;
                    loadSplash();
                }else{
                    showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }

            } else {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }*/
        startService(new Intent(SplashActivity.this, OnClearFromRecentService.class));
        if(AppUtilityMethods.isNetworkConnected(mContext))
        {
            forceUpdate(URL_GET_FORCE_UPDATE);
        }

        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getAccessToken();
            System.out.println("Device id---"+ FirebaseInstanceId.getInstance().getToken());
            loadSplash();
        }else{
            showDialogAlertDismiss(mActivity, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
       // loadSplash();

    }

    private void forceUpdate(String URL_FORCE) {

        VolleyAPIManager volleyWrapper=new VolleyAPIManager(URL_FORCE);
        String[] name={};
        String[] value={};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponseGET(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String status_code="";
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        status_code = secobj.getString(JTAG_STATUSCODE);
                        AppPreferenceManager.setVersionFromApi(mContext, secobj.optString("android_version"));
                        Log.e( "responseSplash: ",secobj.optString("android_version") );
                    }
                     else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext,  getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon,  R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.common_error), getString(R.string.invalid_usr_pswd), R.drawable.infoicon,  R.drawable.roundblue);
                        }
                } catch (Exception ex) {
                    Log.e( "The Exception : ", ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
, getResources().getString(R.string.ok));
dialog.show();*/
             /*   AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);*/

            }
        });


    }
    /**************************************************************************/
    /*function name : loadSplash*/
    /*Showing splash screen with a timer. This will be useful when you
	  want to show case your app logo / company */
    /**************************************************************************/

    private void loadSplash() {

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
               /* if(AppPreferenceManager.getIsGuest(mContext)){

                }*/

                if (AppPreferenceManager.getIsUserAlreadyLoggedIn(mContext)) {
                    Intent i = new Intent(SplashActivity.this,
                            HomeActivity.class);
                    AppPreferenceManager.setIsGuest(mContext, false);
         /*       i.putExtra("TITLE_NAME",mTabTitleModelArrayList);
                i.putExtra("TAB_ID",mTabArrangeModelArrayList);*/

                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this,
                            LoginActivity.class);

         /*       i.putExtra("TITLE_NAME",mTabTitleModelArrayList);
                i.putExtra("TAB_ID",mTabArrangeModelArrayList);*/

                    startActivity(i);
                    finish();
                }
                // }
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void getAccessToken(){
        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
            @Override
            public void tokenrenewed() {

            }
        });
    }

    private  void showDialogAlertDismiss(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                loadSplash();
            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }
    private  void showDialogAlertDismissFinish(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                activity.finish();
            }
        });

        dialog.show();

    }


}
