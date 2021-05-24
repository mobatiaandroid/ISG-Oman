package com.algubra.activity.studentprofile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.login.model.StudentModels;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PdfReader;
import com.algubra.activity.studentprofile.adapter.StudentProfileAdapter;
import com.algubra.activity.timetable.TimeTableActivity;
import com.algubra.appcontroller.AppController;
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
 * Created by gayatri on 17/5/17.
 */
public class StudentProfileListActivity extends Activity implements URLConstants, JsonTagConstants, StausCodes {
    static RecyclerView messageList;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, about_us_bannerImgView, addstudent;
    static Context mContext;
    ArrayList<StudentModels> studentModelsArrayList = new ArrayList<>();
    static ArrayList<StudentModels> studentModelsArrayLists = new ArrayList<>();
    Dialog dialog;
    Dialog dialog1;
    static Dialog dialog2;
    EditText text_grno, text_phone;
    String mapId;
    static EditText verifyCode;
    static ImageView verifyInfoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        initUI();
        resetDisconnectTimer();

        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            getStudentList(URL_GET_STUDENTS);
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void getStudentList(String urlGetTimetableList) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(urlGetTimetableList);
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
                            System.out.println("Preference ---" + studentListArray.toString());
                            AppPreferenceManager.setStudentsResponseFromLoginAPI(mContext, studentListArray.toString());
                            if (studentListArray.length() > 0) {
                                studentModelsArrayList.clear();
                                //AppController.studentArrayList.clear();
                                for (int i = 0; i < studentListArray.length(); i++) {
                                    JSONObject sObject = studentListArray.getJSONObject(i);
                                    StudentModels studentModels = new StudentModels();
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
                                    studentModels.setMapId(sObject.optString(JTAG_MAP_ID));
                                    studentModels.setMapStatusVerify(sObject.optString(JTAG_MAP_STATUS));
                                    studentModelsArrayList.add(studentModels);
                                    //AppController.studentArrayList.add(studentModels);
                                }
                                /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
                                //StudentAdapter studentAdapter=new St
                                StudentProfileAdapter studentAwardsListAdapter = new StudentProfileAdapter(mContext, studentModelsArrayList);
                                messageList.setAdapter(studentAwardsListAdapter);
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon, R.drawable.roundblue);

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
                        getStudentList(URL_GET_STUDENTS);

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

    private void initUI() {
        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog1 = new Dialog(mContext, R.style.NewDialog);

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);

        messageList = (RecyclerView) findViewById(R.id.settingItemList);

        messageList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(llm);

        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        messageList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(StudentProfileListActivity.this, "Student's Profile");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        addstudent = headermanager.getRightButton();
        headermanager.setButtonRightSelector(R.drawable.add_student,
                R.drawable.add_student);
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });

        addstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordAlert();
            }
        });


      /*  messageList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), messageList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(StudentProfileListActivity.this, PDFViewActivity.class);
                        intent.putExtra("pdf_url",studentModelsArrayLists.get(position).getPdf_link());
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
*/

    }

    private void showChangePasswordAlert() {

        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_student);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        text_grno = (EditText) dialog.findViewById(R.id.text_grno);
        text_phone = (EditText) dialog.findViewById(R.id.text_phone);

        Button dialogSubmitButton = (Button) dialog
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      AppUtilityMethods.hideSoftKeyboard(StudentProfileListActivity.this);
                                                      if (text_grno.getText().toString().trim().length() == 0) {
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_studentid), R.drawable.infoicon, R.drawable.roundblue);

                                                      } else if (text_phone.getText().toString().trim().length() == 0) {
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phoneno), R.drawable.infoicon, R.drawable.roundblue);

                                                          //newpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (text_phone.getText().toString().trim().length() < 8) {
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phone_eight_digitno), R.drawable.infoicon, R.drawable.roundblue);

                                                          //newpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else {
                                                          if (AppUtilityMethods.isNetworkConnected(mContext)) {
                                                              addStudentAPI(URL_ADD_CHILD);
                                                          } else {
                                                              AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                          }
                                                      }
                                                  }
                                              }

        );

        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AppUtilityMethods.hideKeyBoard(mContext);

                                                dialog.dismiss();
                                            }
                                        }

        );
        dialog.show();
    }

    private void addStudentAPI(String URL) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL);
        String[] name = {"access_token", "parentId", "student_gr_no", "phone"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext),
                text_grno.getText().toString(),
                text_phone.getText().toString()};

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
                        if (status_code.equalsIgnoreCase(STATUSCODE_OTPSEND)) {
                            /*JSONArray responseArray = secobj.getJSONArray(JTAG_RESPONSE_ARRAY);
							for (int i = 0; i < responseArray.length(); i++) {
								JSONObject respObj = responseArray.getJSONObject(i);
								PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USERS_ID));
							}*/
//dialog.dismiss();mapId
                            mapId = secobj.optString(JTAG_MAPID);
                            AppController.mapId = mapId;
                            Log.d("TAG", "Inside response success---");
//                            showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.resend_otp), R.drawable.tick, R.drawable.roundblue);
                            showVerifyAlert();
                        } else if (status_code.equalsIgnoreCase(STATUSCODE_PASSWORD_MISMATCH)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_user), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_GRNO_NOTVALID)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_grno), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_ERROR_OCCURED)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.error_occurreds), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.student_exists), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {


                            }
                        });
                        addStudentAPI(URL_ADD_CHILD);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
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

    private void showVerifyDialog() {
        dialog1 = new Dialog(mContext, R.style.NewDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_verify);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCancelable(false);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        verifyCode = (EditText) dialog1.findViewById(R.id.text_phone);
        verifyInfoImageView = (ImageView) dialog1.findViewById(R.id.verifyInfoImageView);
        verifyInfoImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.verify_info), R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });
        //mMailEdtText.setOnTouchListener(this);
//
        Paint p = new Paint();
        p.setColor(Color.parseColor("#6E6E6E"));
        TextView textViewResendPassword = (TextView) dialog1.findViewById(R.id.textViewResendPassword);
        textViewResendPassword.setPaintFlags(p.getColor());
        textViewResendPassword.setPaintFlags(textViewResendPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewResendPassword.setText(mContext.getResources().getString(R.string.resend_verify));
        Button dialogSubmitButton = (Button) dialog1
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                if (verifyCode.getText().toString().trim().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Please enter the verification code.", R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    if (AppUtilityMethods.isNetworkConnected(mContext)) {
                        submitLeave(URL_VERIFY_OTP, verifyCode.getText().toString());
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
            }
        });

        Button dialogMayBelaterutton = (Button) dialog1.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                dialog1.dismiss();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    getStudentList(URL_GET_STUDENTS);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        textViewResendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp(URL_RESEND_OTP);
            }
        });
        dialog1.show();
    }

    private void showVerifyAlert() {
        dialog.dismiss();
        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_verify_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        //mMailEdtText.setOnTouchListener(this);
//
        Button dialogSubmitButton = (Button) dialog
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                dialog.dismiss();
                showVerifyDialog();
            }
        });

        Button dialogMayBelaterutton = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                dialog.dismiss();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    getStudentList(URL_GET_STUDENTS);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        dialog.show();
    }


    /*@Override
    public void testFunctionOne() {
        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getStudentList(URL_GET_STUDENTS);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }*/

    /*  public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

      private Handler disconnectHandler = new Handler() {
          public void handleMessage(Message msg) {
          }
      };

      private Runnable disconnectCallback = new Runnable() {
          @Override
          public void run() {
              // Perform any required operation on disconnect
              AppPreferenceManager.setIsGuest(StudentProfileListActivity.this, false);
              AppPreferenceManager.setUserId(StudentProfileListActivity.this, "");
              AppPreferenceManager.setIsUserAlreadyLoggedIn(StudentProfileListActivity.this, false);
              AppPreferenceManager.setSchoolSelection(StudentProfileListActivity.this, "ISG");
              if (AppUtilityMethods.isAppInForeground(mContext)) {

                  Intent mIntent = new Intent(StudentProfileListActivity.this, LoginActivity.class);
                  mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(mIntent);
                  finish();
              }
          }
      };
  */
    public void resetDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
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

    private void submitLeave(String URL_API, String verifyCode) {
        final String mVerifyCode = verifyCode;
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_API);
        String[] name = {"access_token", "parentId", "otp", "mapId"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext), verifyCode, mapId};

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
                            dialog1.dismiss();
                            showDialogSuccess((Activity) mContext, "Success", getString(R.string.succ_add_student), R.drawable.tick, R.drawable.roundblue);


                            // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick,  R.drawable.roundblue);
                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_ERROR_OCCURED)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.error_occurreds), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.student_exists), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_CODE_INVALID_OTP)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.invalid_otp), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.internal_server_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        submitLeave(URL_VERIFY_OTP, mVerifyCode);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
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

    public void showDialogSuccess(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    getStudentList(URL_GET_STUDENTS);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });

        dialog.show();

    }

    public static void showVerifyDialogAdapterClick() {
        dialog2 = new Dialog(mContext, R.style.NewDialog);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_verify);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setCancelable(false);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        verifyCode = (EditText) dialog2.findViewById(R.id.text_phone);
        //mMailEdtText.setOnTouchListener(this);
//
        verifyInfoImageView = (ImageView) dialog2.findViewById(R.id.verifyInfoImageView);
        verifyInfoImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.verify_info), R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });
        Paint p = new Paint();
        p.setColor(Color.parseColor("#6E6E6E"));
        TextView textViewResendPassword = (TextView) dialog2.findViewById(R.id.textViewResendPassword);
        textViewResendPassword.setPaintFlags(p.getColor());
        textViewResendPassword.setPaintFlags(textViewResendPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewResendPassword.setText(mContext.getResources().getString(R.string.resend_verify));
        Button dialogSubmitButton = (Button) dialog2
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                if (verifyCode.getText().toString().trim().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter the verification code.", R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    if (AppUtilityMethods.isNetworkConnected(mContext)) {
                        submitLeave2(URL_VERIFY_OTP, verifyCode.getText().toString());
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
            }
        });

        Button dialogMayBelaterutton = (Button) dialog2.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                dialog2.dismiss();
            }
        });
        textViewResendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp(URL_RESEND_OTP);
            }
        });
        dialog2.show();
    }

    private static void submitLeave2(String URL_API, String verifyCode) {
        final String mVerifyCode = verifyCode;
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_API);
        String[] name = {"access_token", "parentId", "otp", "mapId"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext), verifyCode, AppController.mapId};

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
                            dialog2.dismiss();
                            showDialogSuccess2((Activity) mContext, "Success", mContext.getString(R.string.succ_add_student), R.drawable.tick, R.drawable.roundblue);


                            // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick,  R.drawable.roundblue);
                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_ERROR_OCCURED)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.error_occurreds), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.student_exists), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_CODE_INVALID_OTP)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.invalid_otp), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.internal_server_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        submitLeave2(URL_VERIFY_OTP, mVerifyCode);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
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

    public static void getStudentListAdapter(String urlGetTimetableList) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(urlGetTimetableList);
        String[] name = {"access_token", "parentId"};

        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext)
        };
        studentModelsArrayLists = new ArrayList<>();
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
                            System.out.println("Preference ---" + studentListArray.toString());
                            AppPreferenceManager.setStudentsResponseFromLoginAPI(mContext, studentListArray.toString());
                            if (studentListArray.length() > 0) {
                                studentModelsArrayLists.clear();
                                //AppController.studentArrayList.clear();
                                for (int i = 0; i < studentListArray.length(); i++) {
                                    JSONObject sObject = studentListArray.getJSONObject(i);
                                    StudentModels studentModels = new StudentModels();
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
                                    studentModels.setMapId(sObject.optString(JTAG_MAP_ID));
                                    studentModels.setMapStatusVerify(sObject.optString(JTAG_MAP_STATUS));
                                    studentModelsArrayLists.add(studentModels);
                                    //AppController.studentArrayList.add(studentModels);
                                }
                                /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
                                //StudentAdapter studentAdapter=new St
                                StudentProfileAdapter studentAwardsListAdapter = new StudentProfileAdapter(mContext, studentModelsArrayLists);
                                messageList.setAdapter(studentAwardsListAdapter);
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.no_datafound), R.drawable.infoicon, R.drawable.roundblue);

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentListAdapter(URL_GET_STUDENTS);

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

    public static void showDialogSuccess2(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    getStudentListAdapter(URL_GET_STUDENTS);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });

        dialog.show();

    }

    private static void resendOtp(String URL_API) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_API);
        String[] name = {"access_token", "parentId", "mapId"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext), AppController.mapId};

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
                        if (status_code.equalsIgnoreCase(STATUSCODE_OTPSEND)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", mContext.getString(R.string.verifyAlert), R.drawable.tick, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_ERROR_OCCURED)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.error_occurreds), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_ALLREADYEXISTS)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.student_exists), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUS_CODE_INVALID_OTP)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.invalid_otp), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.internal_server_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        resendOtp(URL_RESEND_OTP);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(StudentProfileListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }

}
