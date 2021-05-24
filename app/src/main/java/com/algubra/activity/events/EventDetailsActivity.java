package com.algubra.activity.events;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.algubra.activity.events.model.EventModels;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PdfReader;
import com.algubra.activity.staffdirectory.model.StaffModel;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gayatri on 25/4/17.
 */
public class EventDetailsActivity extends Activity implements JsonTagConstants,StausCodes,URLConstants{
    private Context mContext=this;
    ArrayList<EventModels> mEventModels;
    Bundle extras;
    RelativeLayout relativeHeader;
    RelativeLayout registrationRelative;
    RelativeLayout parentConsernRelative;
    HeaderManager headermanager;
    ImageView back,bannerImage,pdfDownloadImgView,parentConsernpdfDownloadImgView,addToCalendar;
    int position;
    TextView eventTitle,eventAttendStatus,start_date,start_time,end_date,end_time,eventDesc;
    Button submitAttend;
    Button parentConsernForm,registrationForm;
    boolean check_register=false;
    Dialog dialog ;
     CustomProgressBar pDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        extras = getIntent().getExtras();
        if (extras != null) {
            mEventModels = (ArrayList<EventModels>) extras
                    .getSerializable("array");
            position=extras.getInt("position");

        }

        initUI();
        resetDisconnectTimer();

        setValues();

    }
private void initUI(){
    pDialog = new CustomProgressBar(mContext,
            R.drawable.spinner);
    pDialog.show();
    dialog = new Dialog(mContext);
    headermanager = new HeaderManager(EventDetailsActivity.this, getString(R.string.events));
    relativeHeader= (RelativeLayout) findViewById(R.id.relativeHeader);
    registrationRelative= (RelativeLayout) findViewById(R.id.registrationRelative);
    parentConsernRelative= (RelativeLayout) findViewById(R.id.parentConsernRelative);
    start_date= (TextView) findViewById(R.id.start_date);
    start_time= (TextView) findViewById(R.id.start_time);
    addToCalendar= (ImageView) findViewById(R.id.addToCalendar);
    pdfDownloadImgView= (ImageView) findViewById(R.id.pdfDownloadImgView);
    parentConsernpdfDownloadImgView= (ImageView) findViewById(R.id.parentConsernpdfDownloadImgView);
    end_date= (TextView) findViewById(R.id.end_date);
    end_time= (TextView) findViewById(R.id.end_time);
    eventAttendStatus= (TextView) findViewById(R.id.eventAttendStatus);
    eventTitle= (TextView) findViewById(R.id.eventTitle);
    eventDesc= (TextView) findViewById(R.id.eventDesc);
    submitAttend= (Button) findViewById(R.id.submitAttend);
    registrationForm= (Button) findViewById(R.id.registrationForm);
    parentConsernForm= (Button) findViewById(R.id.parentConsernForm);
    bannerImage= (ImageView) findViewById(R.id.bannerImage);
    pdfDownloadImgView.setImageResource(R.drawable.pdficonwhite);
    parentConsernpdfDownloadImgView.setImageResource(R.drawable.pdficonwhite);
    if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
        headermanager.getHeader(relativeHeader, 1);
        submitAttend.setBackgroundResource(R.drawable.event_attend_drwable_btn_green);
        registrationForm.setBackgroundResource(R.drawable.event_attend_drwable_btn_green);
        parentConsernForm.setBackgroundResource(R.drawable.event_attend_drwable_btn_green);
//        pdfDownloadImgView.setBackgroundResource(R.drawable.pdfgreen);
//        parentConsernpdfDownloadImgView.setBackgroundResource(R.drawable.pdfgreen);
    } else {
        headermanager.getHeader(relativeHeader, 3);
        submitAttend.setBackgroundResource(R.drawable.event_attend_drwable_btn_blue);
        registrationForm.setBackgroundResource(R.drawable.event_attend_drwable_btn_blue);
        parentConsernForm.setBackgroundResource(R.drawable.event_attend_drwable_btn_blue);

//        parentConsernpdfDownloadImgView.setBackgroundResource(R.drawable.pdfblue);
//        pdfDownloadImgView.setBackgroundResource(R.drawable.pdfblue);

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


    submitAttend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //check_register = true;
            if(mEventModels.get(position).getPay_type().equals("0")){
                if(submitAttend.getText().toString().equals("ATTENDING")) {
                    showDialogEventConfirmAlert((Activity) mContext, getString(R.string.alert_heading), "Do you want to register for the " + mEventModels.get(position).getName() +"?", R.drawable.question,  R.drawable.roundblue);
                }else{
                    showDialogEventUnJoinConfirmAlert((Activity) mContext, getString(R.string.alert_heading), "Do you want to unregister for " + mEventModels.get(position).getName()+"?", R.drawable.question,  R.drawable.roundblue);

                }
            }else if(mEventModels.get(position).getPay_type().equals("1")){
                if(submitAttend.getText().toString().equals("ATTENDING")) {
                    showDialogEventConfirmAlert((Activity) mContext, getString(R.string.alert_heading), "Do you want to register for the " + mEventModels.get(position).getName()+"?", R.drawable.question,  R.drawable.roundblue);
                }else{
                    showDialogEventUnJoinConfirmAlert((Activity) mContext, getString(R.string.alert_heading), "Do you want to unregister for " + mEventModels.get(position).getName()+"?", R.drawable.question,  R.drawable.roundblue);

                }
//                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.alert_event_reg_paid), R.drawable.infoicon,  R.drawable.roundblue);
            }
        }
    });

    pdfDownloadImgView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mEventModels.get(position).getPdf_link().equalsIgnoreCase("")) {
//                stopDisconnectTimer();
             /*   Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                intent.putExtra("pdf_url", mEventModels.get(position).getPdf_link());
                startActivity(intent);*/
                Intent browserIntent = new Intent(EventDetailsActivity.this, PDFViewActivity.class);
                browserIntent.putExtra("pdf_url",mEventModels.get(position).getPdf_link());
                browserIntent.putExtra("title","Events");
                browserIntent.putExtra("filename",mEventModels.get(position).getName());
                startActivity(browserIntent);
            }
            else
            {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.reg_form_not_available), R.drawable.infoicon,  R.drawable.roundblue);

            }
        }
    });
    registrationForm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mEventModels.get(position).getPdf_link().equalsIgnoreCase("")) {
//                stopDisconnectTimer();
             /*   Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                intent.putExtra("pdf_url", mEventModels.get(position).getPdf_link());
                startActivity(intent);*/
             /*   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEventModels.get(position).getPdf_link()));
                startActivity(browserIntent);*/
                Intent browserIntent = new Intent(EventDetailsActivity.this, PDFViewActivity.class);
                browserIntent.putExtra("pdf_url",(mEventModels.get(position).getPdf_link()));
                browserIntent.putExtra("title","Events");
                browserIntent.putExtra("filename",mEventModels.get(position).getName());
                startActivity(browserIntent);
            }  else
            {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.reg_form_not_available), R.drawable.infoicon,  R.drawable.roundblue);

            }
        }
    });
    parentConsernpdfDownloadImgView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mEventModels.get(position).getParent_consent_pdf_link().equalsIgnoreCase("")) {
//                stopDisconnectTimer();
      /*          Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                intent.putExtra("pdf_url", mEventModels.get(position).getParent_consent_pdf_link());
                startActivity(intent);*/
                Intent browserIntent = new Intent(EventDetailsActivity.this, PDFViewActivity.class);
                browserIntent.putExtra("pdf_url",mEventModels.get(position).getParent_consent_pdf_link());
                browserIntent.putExtra("title","Events");
                browserIntent.putExtra("filename",mEventModels.get(position).getName());
                startActivity(browserIntent);
            }  else
            {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.parent_form_not_available), R.drawable.infoicon,  R.drawable.roundblue);

            }
        }
    });
    parentConsernForm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mEventModels.get(position).getParent_consent_pdf_link().equalsIgnoreCase("")) {
//                stopDisconnectTimer();
              /*  Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                intent.putExtra("pdf_url", mEventModels.get(position).getParent_consent_pdf_link());
                startActivity(intent);*/
                Intent browserIntent = new Intent(EventDetailsActivity.this, PDFViewActivity.class);
                browserIntent.putExtra("pdf_url",mEventModels.get(position).getParent_consent_pdf_link());
                browserIntent.putExtra("title","Events");
                browserIntent.putExtra("filename",mEventModels.get(position).getName());
                startActivity(browserIntent);
            } else
            {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.parent_form_not_available), R.drawable.infoicon,  R.drawable.roundblue);

            }
        }
    });
    addToCalendar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long startTime = 0, endTime = 0;

            try {
                Date dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mEventModels.get(position).getStart_date());
                startTime = dateStart.getTime();
                Date dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mEventModels.get(position).getEnd_date());
                endTime = dateEnd.getTime();
            } catch (Exception e) {
            }

            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", startTime);
            intent.putExtra("allDay", false);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", endTime);
            intent.putExtra("title", mEventModels.get(position).getName());
            startActivity(intent);
        }
    });
}

private void setValues(){

    eventTitle.setText(mEventModels.get(position).getName());
    if(mEventModels.get(position).getPay_type().equalsIgnoreCase("1")) {
        if (mEventModels.get(position).getPdf_link().equalsIgnoreCase("")) {
            registrationRelative.setVisibility(View.GONE);
        } else {
            registrationRelative.setVisibility(View.VISIBLE);

        }
        if (mEventModels.get(position).getParent_consent_pdf_link().equalsIgnoreCase("")) {
            parentConsernRelative.setVisibility(View.GONE);
        } else {
            parentConsernRelative.setVisibility(View.VISIBLE);

        }
    }
    else
    {
        registrationRelative.setVisibility(View.GONE);

    }
    if (mEventModels.get(position).getParent_consent_pdf_link().equalsIgnoreCase("")) {
        parentConsernRelative.setVisibility(View.GONE);
    } else {
        parentConsernRelative.setVisibility(View.VISIBLE);

    }
    if(!mEventModels.get(position).getPdf_link().equals("")){
        pdfDownloadImgView.setVisibility(View.VISIBLE);
    }else{
        pdfDownloadImgView.setVisibility(View.GONE);
    }
if(mEventModels.get(position).getType().equals("0")){
    eventAttendStatus.setVisibility(View.GONE);
    submitAttend.setVisibility(View.GONE);
}
    if(mEventModels.get(position).getId().equals(AppUtilityMethods.stringFromBase64Encoded(AppPreferenceManager.getStudentId(mContext)))){
       /* submitAttend.setBackgroundResource(R.drawable.event_attend_drwable_btn);
        submitAttend.setTextColor(getResources().getColor(R.color.white));
        submitAttend.setText("Attending");*/
        eventAttendStatus.setText("Status: Attending");
        submitAttend.setText("NOT ATTENDING");
        //submitAttend.setVisibility(View.GONE);
    }else{
        eventAttendStatus.setText("Status: Not Attending");
        submitAttend.setText("ATTENDING");

    }

    start_date.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mEventModels.get(position).getStart_date()));
    start_time.setText(AppUtilityMethods.separateTime(mEventModels.get(position).getStart_date()).replace(".",""));
    end_date.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mEventModels.get(position).getEnd_date()));
    end_time.setText(AppUtilityMethods.separateTime(mEventModels.get(position).getEnd_date()).replace(".", ""));

    eventDesc.setText(Html.fromHtml("<html><body style=\"text-align:justify\">" + mEventModels.get(position).getDescription().replace("\n", "<br>").replace("\r", "&nbsp") + "</body></html>"));
    /*Glide.with(mContext).load(mEventModels.get(position).getImage().replace(" ", "%20")).listener(new RequestListener<String, GlideDrawable>() {
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
    if(!mEventModels.get(position).getImage().equals("")) {
        Picasso.with(mContext).load(mEventModels.get(position).getImage().replaceAll(" ", "%20")).fit()
                .into(bannerImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        pDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        pDialog.dismiss();
                    }
                });
    }else{
        pDialog.dismiss();

    }

}
    public  void showDialogEventConfirmAlert(final Activity activity, final String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                resetDisconnectTimer();
                dialog.dismiss();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    callventRegistrationAPI(URL_EVENT_REGISTRATION);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }

                //dialog.dismiss();
               /* showDialogRegSuccAlert((Activity)mContext,getString(R.string.alert_heading),
                        "Successfully registered for the event "+mEventModels.get(position).getName(),R.drawable.tick,  R.drawable.roundblue);*/
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDisconnectTimer();

                dialog.dismiss();
                eventAttendStatus.setText("Attending");
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
        ImageView icon =  dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead =  dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDisconnectTimer();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

private void callventRegistrationAPI(String apiUrl){
    VolleyAPIManager volleyWrapper=new VolleyAPIManager(apiUrl);
    String[] name={"access_token","eventId","studentId","parentId"};

    String[] value={AppPreferenceManager.getAccessToken(mContext), mEventModels.get(position).getEvent_id(),
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
                        if(mEventModels.get(position).getPay_type().equals("1")) {
                            showDialogRegSuccAlert((Activity) mContext, getString(R.string.alert_heading),"Successfully registered for "+ mEventModels.get(position).getName()+". Since this is a paid event ("+mEventModels.get(position).getAmount()+" OMR for registration), please confirm your attendance by making payment with the concerned teacher."
                                   , R.drawable.tick,  R.drawable.roundblue);

                        }
                        else {
                            showDialogRegSuccAlert((Activity) mContext, getString(R.string.alert_heading),
                                    "Successfully registered for the event " + mEventModels.get(position).getName(), R.drawable.tick, R.drawable.roundblue);
                        }
                        eventAttendStatus.setText("Status: Attending");
                        submitAttend.setText("NOT ATTENDING");
                        //submitAttend.setVisibility(View.GONE);
                    } else if (status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.already_requested), R.drawable.infoicon,  R.drawable.roundblue);

                    }  else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);
                    }
                } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon,  R.drawable.roundblue);

                } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                    AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                        @Override
                        public void tokenrenewed() {
                            callventRegistrationAPI(URL_EVENT_REGISTRATION);
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

            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);

        }
    });


}

    private void calleventUnRegistrationAPI(String apiUrl){
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(apiUrl);
        String[] name={"access_token","eventId","studentId","parentId"};

        String[] value={AppPreferenceManager.getAccessToken(mContext), mEventModels.get(position).getEvent_id(),
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
                                    "Successfully unregistered for the event " + mEventModels.get(position).getName(), R.drawable.tick,  R.drawable.roundblue);
                            eventAttendStatus.setText("Status: Not Attending the event");
                            submitAttend.setText("ATTENDING");
                            //submitAttend.setVisibility(View.GONE);
                        } else if (status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.already_requested), R.drawable.infoicon,  R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon,  R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                calleventUnRegistrationAPI(URL_CALLUNJOINEVENTS);
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

                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);

            }
        });


    }

    public  void showDialogEventUnJoinConfirmAlert(final Activity activity, final String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_switch_dialog);
        ImageView icon = dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = dialog.findViewById(R.id.text_dialog);
        TextView textHead = dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = dialog.findViewById(R.id.btn_signup);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDisconnectTimer();
                dialog.dismiss();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    //callUnJoinventRegistrationAPI(URL_CALLUNJOIN);
                    calleventUnRegistrationAPI(URL_CALLUNJOINEVENTS);

                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        Button dialogButtonCancel = dialog.findViewById(R.id.btn_maybelater);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDisconnectTimer();
                dialog.dismiss();
                // eventAttendStatus.setText("Attending");
            }
        });
        dialog.show();


    }

    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(EventDetailsActivity.this, false);
            AppPreferenceManager.setUserId(EventDetailsActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(EventDetailsActivity.this, false);
            AppPreferenceManager.setSchoolSelection(EventDetailsActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(EventDetailsActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(EventDetailsActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}

