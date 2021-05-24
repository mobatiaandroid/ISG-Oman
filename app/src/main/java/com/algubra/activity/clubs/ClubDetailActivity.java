package com.algubra.activity.clubs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.clubs.model.ClubModels;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.CustomProgressBar;
import com.algubra.volleymanager.VolleyAPIManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 4/5/17.
 */
public class ClubDetailActivity extends Activity implements JsonTagConstants,StausCodes,URLConstants{
    TextView clubTitle,clubDesc;
    Button clubJoinBtn;
    ImageView bannerImage;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,pdfImg;
    Context mContext = this;
    Bundle extras;
    int position;
     CustomProgressBar pDialog;
    ArrayList<ClubModels> clubModelsArrayList=new ArrayList<>();
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        extras = getIntent().getExtras();
        if (extras != null) {
            clubModelsArrayList = (ArrayList<ClubModels>) extras
                    .getSerializable("array");
            position=extras.getInt("position");

        }
        initUI();
        resetDisconnectTimer();
        setValues();
    }

    private void initUI() {
        pDialog = new CustomProgressBar(mContext,
                R.drawable.spinner);
        pDialog.show();
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        clubTitle= (TextView) findViewById(R.id.clubTitle);
        clubDesc= (TextView) findViewById(R.id.clubDesc);
        clubJoinBtn= (Button) findViewById(R.id.clubJoinBtn);
        layout= (LinearLayout) findViewById(R.id.layout);
        bannerImage= (ImageView) findViewById(R.id.bannerImage);
        pdfImg= (ImageView) findViewById(R.id.pdfImg);
        headermanager = new HeaderManager(ClubDetailActivity.this,"Sports & Activities" );//"Club Details");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            pdfImg.setBackgroundResource(R.drawable.pdfgreen);
            clubJoinBtn.setBackgroundResource(R.drawable.event_attend_drwable_green_btn);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            pdfImg.setBackgroundResource(R.drawable.pdfblue);
            clubJoinBtn.setBackgroundResource(R.drawable.event_attend_drwable_blue_btn);

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

        clubJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Club ---"+clubJoinBtn.getText().toString());
                if (clubJoinBtn.getText().toString().equals("JOINED")) {
                    showDialogEventUnJoinConfirmAlert((Activity) mContext, getString(R.string.alert_heading), "Do you want to unjoin for " + clubModelsArrayList.get(position).getClub_name(), R.drawable.question,  R.drawable.roundblue);

                    // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "You are already joined to the club", R.drawable.exclamationicon,  R.drawable.roundblue);
                } else if(clubJoinBtn.getText().toString().equals("JOIN")){
                    showDialogEventConfirmAlert((Activity) mContext, getString(R.string.alert_heading), "Do you want to join for " + clubModelsArrayList.get(position).getClub_name(), R.drawable.question,  R.drawable.roundblue);
                }
            }
        });

        pdfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopDisconnectTimer();
              /*  Intent intent = new Intent(ClubDetailActivity.this, PdfReader.class);
                intent.putExtra("pdf_url", clubModelsArrayList.get(position).getPdf());
                startActivity(intent);*/
                if (!(clubModelsArrayList.get(position).getPdf().equalsIgnoreCase(""))) {
                      /*      Intent intent = new Intent(CircularActivity.this, PdfReader.class);
                            intent.putExtra("pdf_url", listTimetable.get(position).getPdf_link());
                            startActivity(intent);*/
                    Intent browserIntent = new Intent(ClubDetailActivity.this, PDFViewActivity.class);
                    browserIntent.putExtra("pdf_url",clubModelsArrayList.get(position).getPdf());
                    browserIntent.putExtra("title","Club detail");
                    browserIntent.putExtra("filename",clubModelsArrayList.get(position).getClub_name());
                    startActivity(browserIntent);
                }
                else
                {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.nomoreinfo), R.drawable.exclamationicon, R.drawable.roundblue);

                }
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clubModelsArrayList.get(position).getPdf()));
//                startActivity(browserIntent);
            }
        });
    }

    private void setValues(){

        clubTitle.setText(clubModelsArrayList.get(position).getClub_name());
        clubDesc.setText(Html.fromHtml("<html><body style=\"text-align:justify\">" + clubModelsArrayList.get(position).getClub_description().replace("\r", "").replace("\n", "") + "</body></Html>"));
    if(clubModelsArrayList.get(position).getClub_student_id().equals(AppUtilityMethods.stringFromBase64Encoded(AppPreferenceManager.getStudentId(mContext)))){
        clubJoinBtn.setText("JOINED");
    }else{
        clubJoinBtn.setText("JOIN");

    }

        if(clubModelsArrayList.get(position).getPdf().equals("")){
            layout.setVisibility(View.GONE);
            pdfImg.setVisibility(View.GONE);
        }else{
            layout.setVisibility(View.VISIBLE);
            pdfImg.setVisibility(View.VISIBLE);

        }
if(!clubModelsArrayList.get(position).getImage().equals("")){
    Picasso.with(mContext).load(clubModelsArrayList.get(position).getImage().replaceAll(" ", "%20")).fit()
            .into(bannerImage, new Callback() {
                @Override
                public void onSuccess() {
                    pDialog.dismiss();                }

                @Override
                public void onError() {
                    pDialog.dismiss();
                }
            });
}else{
    pDialog.dismiss();
}


        /*Glide.with(mContext).load(clubModelsArrayList.get(position).getImage().replace(" ", "%20")).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                pDialog.dismiss();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                pDialog.dismiss();
                return false;
            }
        }).centerCrop().placeholder(R.drawable.noimage).dontAnimate().into(bannerImage);*/

    }

    public  void showDialogEventConfirmAlert(final Activity activity, final String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_switch_dialog);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_signup);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    callventRegistrationAPI(URL_CLUB_ENROLLMENT);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               // eventAttendStatus.setText("Attending");
            }
        });
        dialog.show();


    }

    public  void showDialogRegSuccAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
            }
        });

        dialog.show();

    }
    private void callventRegistrationAPI(String apiUrl){
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(apiUrl);
        String[] name={"access_token","clubId","studentId","parentId"};

        String[] value={AppPreferenceManager.getAccessToken(mContext), clubModelsArrayList.get(position).getClub_id(),
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
                            showDialogRegSuccAlert((Activity) mContext, getString(R.string.alert_heading),
                                    "Successfully joined to  " + clubModelsArrayList.get(position).getClub_name(), R.drawable.tick,  R.drawable.roundblue);
                            clubJoinBtn.setText("JOINED");
                            //submitAttend.setVisibility(View.GONE);
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
                        callventRegistrationAPI(URL_EVENT_REGISTRATION);

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

    public  void showDialogEventUnJoinConfirmAlert(final Activity activity, final String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_switch_dialog);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_signup);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    callUnJoinventRegistrationAPI(URL_CALLUNJOIN);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // eventAttendStatus.setText("Attending");
            }
        });
        dialog.show();


    }
    private void callUnJoinventRegistrationAPI(String apiUrl){
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(apiUrl);
        String[] name={"access_token","clubId","studentId","parentId"};

        String[] value={AppPreferenceManager.getAccessToken(mContext), clubModelsArrayList.get(position).getClub_id(),
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
                            showDialogRegUnJoinSuccAlert((Activity) mContext, getString(R.string.alert_heading),
                                    "Successfully unjoined from  " + clubModelsArrayList.get(position).getClub_name(), R.drawable.tick,  R.drawable.roundblue);
                            clubJoinBtn.setText("JOIN");
                           // clubModelsArrayList.get(position).setClub_student_id("");
                            //submitAttend.setVisibility(View.GONE);
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
                        callUnJoinventRegistrationAPI(URL_CALLUNJOIN);

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
    public  void showDialogRegUnJoinSuccAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
            }
        });

        dialog.show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(ClubDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
        else {
            setValues();

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
            AppPreferenceManager.setIsGuest(ClubDetailActivity.this, false);
            AppPreferenceManager.setUserId(ClubDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(ClubDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(ClubDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(ClubDetailActivity.this, LoginActivity.class);
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
