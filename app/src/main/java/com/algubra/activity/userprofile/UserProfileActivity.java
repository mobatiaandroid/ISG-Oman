package com.algubra.activity.userprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.circulars.adapter.CircularAdapter;
import com.algubra.activity.circulars.model.CircularModel;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by gayatri on 17/5/17.
 */
public class UserProfileActivity extends Activity implements JsonTagConstants,URLConstants,StausCodes{
    private Context mContext=this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,editProf,Img;
    TextView userName,addressName,phno,email,postcode,city,country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initUI();
        resetDisconnectTimer();

        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getList(URL_GETMYPROFILE);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(UserProfileActivity.this, "User Profile");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            //btnSubmit.setBackgroundResource(R.color.login_button_bg);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            //btnSubmit.setBackgroundResource(R.color.isg_int_blue);

        }
        back = headermanager.getLeftButton();

        editProf=headermanager.getRightButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        headermanager.setButtonRightSelector(R.drawable.pencil, R.drawable.pencil);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                Intent intent=new Intent(UserProfileActivity.this,EditProfileActivity_New.class);
               // intent.putExtra("name",)
                startActivity(intent);
                //finish();

            }
        });
        Img= (ImageView) findViewById(R.id.Img);
        userName= (TextView) findViewById(R.id.userName);
        addressName= (TextView) findViewById(R.id.addressName);
        phno= (TextView) findViewById(R.id.phno);
                email= (TextView) findViewById(R.id.email);
        postcode= (TextView) findViewById(R.id.postcode);
        city= (TextView) findViewById(R.id.city);
        country= (TextView) findViewById(R.id.country);
    }

    private void getList(String urlGetTimetableList) {
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(urlGetTimetableList);
        String[] name={"access_token","parentId"};

        String[] value={AppPreferenceManager.getAccessToken(mContext),AppPreferenceManager.getUserId(mContext)
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

                            JSONArray staffArray = secobj.getJSONArray(JTAG_PROFILE);
                            System.out.println("The response is" + staffArray);
                            AppPreferenceManager.setUserRespFromLoginAPI(mContext, staffArray.toString());
                            if (staffArray.length() > 0) {

                                JSONObject sObject = staffArray.getJSONObject(0);
                                //set values

                                userName.setText(sObject.optString(JTAG_NAME));
                                addressName.setText(sObject.optString(JTAG_ADDRESS));
                                email.setText(sObject.optString(JTAG_EMAIL));
                                postcode.setText(sObject.optString(JTAG_PINCODE));
                                phno.setText(sObject.optString(JTAG_MOBILE));
                                AppPreferenceManager.setPhno(mContext, phno.getText().toString());
                                city.setText(sObject.optString(JTAG_CITY));
                                country.setText(sObject.optString(JTAG_COUNTRYID));
                                if (!sObject.optString(JTAG_PHOTO).equals("")) {
//                                    Picasso.with(mContext).load(sObject.optString(JTAG_PHOTO).replace("","%20")).fit()
//                                            .into(Img);
                                    loadImage(mContext, sObject.optString(JTAG_PHOTO).replaceAll(" ", "%20"), Img);

                                }
                                //Glide.with(mContext).load(sObject.optString(JTAG_PHOTO)).placeholder(R.drawable.user_profile).dontAnimate().into(Img);

                               /* Glide.with(mContext).load(sObject.optString(JTAG_PHOTO))
                                        .placeholder(R.drawable.noimage)
                                        .into(Img);*/
                                /*for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                   //set values
                                    Glide.with(mContext).load(sObject.optString(JTAG_PHOTO))
                                            .placeholder(R.drawable.noimage)
                                            .into(Img);
                                    userName.setText(sObject.optString(JTAG_NAME));
                                    addressName.setText(sObject.optString(JTAG_ADDRESS));
                                    email.setText(sObject.optString(JTAG_EMAIL));
                                    postcode.setText(sObject.optString(JTAG_PINCODE));
                                    phno.setText(sObject.optString(JTAG_MOBILE));
                                    city.setText(sObject.optString(JTAG_CITY));
                                    country.setText(sObject.optString(JTAG_COUNTRYID));
                                    //s.setText(sObject.optString(JTAG_STATE));



                                }*/

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
                            }
                        });
                        getList(URL_GETMYPROFILE);

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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(UserProfileActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
        else {
            getList(URL_GETMYPROFILE);
        }

    }

    private Target mTarget;
    void loadImage(Context context, String url, final ImageView img) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                //Do something

                img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url).error(R.drawable.user_profile)
                .into(mTarget);
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
            AppPreferenceManager.setIsGuest(UserProfileActivity.this, false);
            AppPreferenceManager.setUserId(UserProfileActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(UserProfileActivity.this, false);
            AppPreferenceManager.setSchoolSelection(UserProfileActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(UserProfileActivity.this, LoginActivity.class);
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
    @Override
    protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(UserProfileActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
