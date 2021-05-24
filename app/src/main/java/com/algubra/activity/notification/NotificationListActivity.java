package com.algubra.activity.notification;

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.login.model.StudentModels;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.activity.notification.types.Audio;
import com.algubra.activity.notification.types.Image;
import com.algubra.activity.notification.types.OpenYouTubePlayerActivity;
import com.algubra.activity.notification.types.Text;
import com.algubra.activity.pdf.PdfReaderActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.DividerItemDecoration;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by gayatri on 5/5/17.
 */
public class NotificationListActivity extends Activity implements URLConstants, StausCodes, JsonTagConstants {
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    RelativeLayout relativeStudentSelectList;
    TextView selectedStudentTextView;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    String studentId = "0";
    private ArrayList<NotificationModel> specialMessageModelArrayList = new ArrayList<>();
    ArrayList<StudentModels> studentModelsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationlist);
        initUI();
        //setValues();
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            getStudentList();
            getList(URL_GET_ALERTS_LIST);
            clearBadge();
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
        relativeStudentSelectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentModelsArrayList.size()>0) {
                    showStudentList(studentModelsArrayList);
                }

            }
        });
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        relativeStudentSelectList = (RelativeLayout) findViewById(R.id.relativeStudentSelectList);
        selectedStudentTextView = (TextView) findViewById(R.id.selectedStudentTextView);
        messageList = (RecyclerView) findViewById(R.id.settingItemList);

        messageList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(llm);
        studentId = "0";
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        messageList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(NotificationListActivity.this, "Notifications");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            relativeStudentSelectList.setBackgroundResource(R.drawable.roundedcurvedgreenborder);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            relativeStudentSelectList.setBackgroundResource(R.drawable.roundedcurvedblueborder);

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
        ShortcutBadger.applyCount(mContext, 0);//badge
        AppPreferenceManager.setBadgecount(this, "0");
        Intent mIntent = new Intent("badgenotify");
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
//        AppController.textBadge.setText(AppPreferenceManager.getBadgecount(this));
//        AppController.textBadge.setVisibility(View.INVISIBLE);
        messageList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), messageList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        stopDisconnectTimer();
                        if (specialMessageModelArrayList.get(position).getAlert_type().equals("t")) {
                            Intent intent = new Intent(NotificationListActivity.this, Text.class);
                            intent.putExtra("position", position);
                            intent.putExtra("array", specialMessageModelArrayList);
                            startActivity(intent);
                        } else if (specialMessageModelArrayList.get(position).getAlert_type().equals("i")) {
                            Intent intent = new Intent(NotificationListActivity.this, Image.class);
                            intent.putExtra("position", position);
                            intent.putExtra("array", specialMessageModelArrayList);
                            startActivity(intent);
                        } else if (specialMessageModelArrayList.get(position).getAlert_type().equals("v")) {

                            if (specialMessageModelArrayList.get(position).getUrl().contains("youtube.com"))
                            {
                                Intent intent = new Intent(NotificationListActivity.this, OpenYouTubePlayerActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("array", specialMessageModelArrayList);
                                startActivity(intent);

                            }
                            else if (specialMessageModelArrayList.get(position).getUrl().contains("photos.app") || specialMessageModelArrayList.get(position).getUrl().contains("photos.google") )
                            {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(specialMessageModelArrayList.get(position).getUrl()));
                                boolean canOpen = browserIntent.resolveActivity(getPackageManager()) != null;
                                if (canOpen) {
                                    startActivity(browserIntent);
                                } else {
                                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                    intent.putExtra("pdf_url",specialMessageModelArrayList.get(position).getUrl());
                                    intent.putExtra("title","Notification Details");
                                    intent.putExtra("type","drive");
                                    startActivity(intent);
                                }

                            }
                            else
                            {

                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url",specialMessageModelArrayList.get(position).getUrl());
                                intent.putExtra("title","Notification Details");
                                intent.putExtra("type","drive");
                                startActivity(intent);
                            }

                        } else if (specialMessageModelArrayList.get(position).getAlert_type().equals("a")) {
                            Intent intent = new Intent(NotificationListActivity.this, Audio.class);
                            intent.putExtra("position", position);
                            intent.putExtra("array", specialMessageModelArrayList);
                            startActivity(intent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));



    }


    private void getList(String urlGetTimetableList) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(urlGetTimetableList);
        String[] name = {"access_token", "parentId","studentId"};
        /*String boardId="";
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            boardId="1";
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            boardId="2";
        }else{
            boardId="1";
        }*/
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext), studentId};

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

                            JSONArray alertArray = secobj.getJSONArray(JTAG_ALERT_LIST);

                            specialMessageModelArrayList.clear();
                            if (alertArray.length() > 0) {

                                for (int i = 0; i < alertArray.length(); i++) {
                                    JSONObject sObject = alertArray.getJSONObject(i);
                                    NotificationModel notificationModel = new NotificationModel();
                                    notificationModel.setId(sObject.optString(JTAG_ID));
                                    notificationModel.setMessage(sObject.optString(JTAG_MESSAGE));
                                    notificationModel.setUrl(sObject.optString(JTAG_URL));
                                    notificationModel.setAlert_type(sObject.optString(JTAG_ALERT_TYPE));
                                    notificationModel.setCreated_time(AppUtilityMethods.dateParsingToDdMmmYyyy(sObject.optString(JTAG_CREATED_TIME)));
                                    notificationModel.setSection(sObject.optString(JTAG_SECTION));
                                    specialMessageModelArrayList.add(notificationModel);
                                }

                                NotificationListAdapter notificationListAdapter = new NotificationListAdapter(mContext, specialMessageModelArrayList);
                                messageList.setAdapter(notificationListAdapter);

                            } else {
                                NotificationListAdapter notificationListAdapter = new NotificationListAdapter(mContext, specialMessageModelArrayList);
                                messageList.setAdapter(notificationListAdapter);
                                if (selectedStudentTextView.getText().toString().equalsIgnoreCase("Common"))
                                {
                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.alertnotfound), R.drawable.infoicon, R.drawable.roundblue);

                                }
                                else
                                {
                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.alertnotfound), R.drawable.infoicon, R.drawable.roundblue);

                                }

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getList(URL_GET_ALERTS_LIST);

                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex)
                {

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

    private void clearBadge() {

        try {
            VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_CLEAR_BADGE);

            String[] name = {JTAG_ACCESSTOKEN, "parentId"};
            String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext)};
            volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("NofifyRes:" + successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                            if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                if (statusCode.equalsIgnoreCase(STATUSCODE_SUCCESS)) {

                                } else if (statusCode.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                                    AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                                        @Override
                                        public void tokenrenewed() {
                                        }
                                    });
                                    clearBadge();

                                }
                            } else if (responseCode.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                                AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                                    @Override
                                    public void tokenrenewed() {
                                    }
                                });
                                clearBadge();

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(NotificationListActivity.this, false);
            AppPreferenceManager.setUserId(NotificationListActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(NotificationListActivity.this, false);
            AppPreferenceManager.setSchoolSelection(NotificationListActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(NotificationListActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
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

    public void showStudentList(final ArrayList<StudentModels> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list_notify);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        LinearLayout btnLinear = (LinearLayout) dialog.findViewById(R.id.btnLinear);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView studentList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            btnLinear.setBackgroundResource(R.color.login_button_bg);
        }
        else
        {
            btnLinear.setBackgroundResource(R.color.isg_int_blue);

        }
        final int sdk = android.os.Build.VERSION.SDK_INT;
       /* if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.l));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));

        }*/
        studentList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.line)));

        studentList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        studentList.setLayoutManager(llm);

        StudentAdapterListNotifictation studentAdapter = new StudentAdapterListNotifictation(mContext, mStudentArray);
        studentList.setAdapter(studentAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        studentList.addOnItemTouchListener(new RecyclerItemListener(mContext, studentList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        dialog.dismiss();
                        studentId = mStudentArray.get(position).getId();
                        selectedStudentTextView.setText(mStudentArray.get(position).getName());
                        getList(URL_GET_ALERTS_LIST);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    private void getStudentList() {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_GET_STUDENTS);
        String[] name = {"access_token", "parentId"};

        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext)
        };
        studentModelsArrayList = new ArrayList<>();
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

                            JSONArray studentListArray = secobj.getJSONArray(JTAG_STUDENTS);
//                            AppPreferenceManager.setStudentsResponseFromLoginAPI(mContext, studentListArray.toString());
                            if (studentListArray.length() > 0) {
                                studentModelsArrayList.clear();
                                StudentModels studentModels;
                                //AppController.studentArrayList.clear();
                                for (int i = 0; i <= studentListArray.length(); i++) {
                                    if (i!=0) {
                                        studentModels = new StudentModels();
                                        JSONObject sObject = studentListArray.getJSONObject(i - 1);
                                        if (sObject.optString(JTAG_MAP_STATUS).equalsIgnoreCase("1")) {

                                            studentModels.setId(sObject.optString(JTAG_ID));
                                            studentModels.setName(sObject.optString(JTAG_NAME));
                                            studentModels.setDob(sObject.optString(JTAG_DOB));
                                            studentModels.setBlood_group(sObject.optString(JTAG_BLOOD_GROUP));
                                            studentModels.setBoardid(sObject.optString(JTAG_BOARD_ID));
                                            studentModels.setGender(sObject.optString(JTAG_GENDER));
                                            studentModels.setStudent_gr_no(sObject.optString(JTAG_STUDENT_GRNO));
                                            studentModels.setStudent_photo(sObject.optString(JTAG_STUDENT_PHOTO));
                                            studentModels.setHouse(sObject.optString(JTAG_HOUSE));
                                            studentModels.setStudent_gr_no(sObject.optString(JTAG_STUDENT_GRNO));
                                            studentModels.setStudent_division_name(sObject.optString(JTAG_DIV));
                                            studentModels.setClass_name(sObject.optString(JTAG_CLASS));
                                            studentModels.setFather(sObject.optString(JTAG_FATHER));
                                            studentModels.setMother(sObject.optString(JTAG_MOTHER));
                                            studentModels.setMentor(sObject.optString(JTAG_MENTOR));
                                            studentModels.setElect1(sObject.optString(JTAG_ELECT1));
                                            studentModels.setElect2(sObject.optString(JTAG_ELECT2));
                                            studentModels.setTeacher(sObject.optString(JTAG_TEACHER));
                                            studentModels.setTeacher_email(sObject.optString(JTAG_TEACHER_EMAIL));
                                            studentModelsArrayList.add(studentModels);

                                        }
                                    }
                                    else
                                    {
                                        studentModels = new StudentModels();
                                        studentModels.setId("0");
                                        studentModels.setName("Common");
                                        studentModelsArrayList.add(studentModels);

                                    }

                                    //AppController.studentArrayList.add(studentModels);
                                }
                                /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
                                //StudentAdapter studentAdapter=new St
                                /*StudentProfileAdapter studentAwardsListAdapter = new StudentProfileAdapter(mContext, studentModelsArrayList);
                                messageList.setAdapter(studentAwardsListAdapter);*/
//                                showStudentList(studentModelsArrayList);
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.alertnotfound), R.drawable.infoicon, R.drawable.roundblue);

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentList();

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
  @Override protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(NotificationListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
