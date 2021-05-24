package com.algubra.activity.login;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.algubra.BuildConfig;
import com.algubra.HttpsTrustManager;
import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.adapter.StudentAdapter;
import com.algubra.activity.login.model.StudentModels;
import com.algubra.activity.registration.RegistrationActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.recyclerviewmanager.DividerItemDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 17/4/17.
 */
public class LoginActivity extends Activity implements URLConstants, JsonTagConstants, StausCodes {
    TextView forGotpasswordText,app_version;
    EditText edtUserName, edtPassword;
    EditText forgotEmail, forgotPhone;
    //LinearLayout relButtonsHolder,relSignUp,relGuestUser;
    Context mContext = this;
    Button btnLogn, btnSignUp, btnGuestUser;
    Dialog dialog;
    String versionName;
    public ArrayList<StudentModels> studentModelsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            initUI();

            versionName = BuildConfig.VERSION_NAME;
            app_version.setText("Version"+" "+versionName);
            AppPreferenceManager.setIsGuest(LoginActivity.this, true);
            String versionFromPreference = AppPreferenceManager.getVersionFromApi(mContext).replace(".", "");
            int versionNumberAsInteger = Integer.parseInt(versionFromPreference);
            String replaceVersion = AppUtilityMethods.getVersionInfo(mContext).replace(".", "");
            int replaceCurrentVersion = Integer.parseInt(replaceVersion);
            if (!(AppPreferenceManager.getVersionFromApi(mContext).equalsIgnoreCase(""))) {
                if (versionNumberAsInteger > replaceCurrentVersion) {
                    AppUtilityMethods.showDialogAlertUpdate(mContext);
                }

            }
            Log.d("VERSION FROM API", AppPreferenceManager.getVersionFromApi(mContext));
            Log.d("CURRENT VERSION", String.valueOf(Integer.parseInt(replaceVersion)));
        } catch (Exception e) {
          /*  Log.e("onCreate: ", e.getMessage());*/
        }
    }

    private void initUI() {

        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        //relButtonsHolder= (LinearLayout) findViewById(R.id.relButtonsHolder);
        btnLogn = (Button) findViewById(R.id.btnLogin);
        btnGuestUser = (Button) findViewById(R.id.btnGuestUser);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        app_version = findViewById(R.id.app_version);
        /// relSignUp= (LinearLayout) findViewById(R.id.relSignUp);
        // relGuestUser= (LinearLayout) findViewById(R.id.relGuestUser);
        forGotpasswordText = (TextView) findViewById(R.id.txtPassword);
       // edtUserName.setText("9946063677");
        /*set underline for forgot password text*/
        forGotpasswordText.setPaintFlags(forGotpasswordText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forGotpasswordText.setText(getString(R.string.forgot_password));
        dialog = new Dialog(mContext, R.style.NewDialog);
        btnLogn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                if (edtUserName.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phoneno), R.drawable.infoicon, R.drawable.roundblue);
                } else if (edtPassword.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_password), R.drawable.infoicon, R.drawable.roundblue);

                }/*else if(!AppUtilityMethods.isValidEmail(edtUserName.getText().toString())){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.infoicon,  R.drawable.roundblue);
                }*/ else {
                    //call api
                    // showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.login_success_alert), R.drawable.tick,  R.drawable.roundblue);
                    if (AppUtilityMethods.isNetworkConnected(mContext))
                    {
                        sendLoginToServer(URL_PARENT_LOGIN);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
               /* AppPreferenceManager.setIsGuest(mContext,false);
                Intent homeIntent = new Intent(mContext, HomeActivity.class);
                startActivity(homeIntent);*/
                //finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                showDialogSignUpAlert();
                //finish();
            }
        });
        btnGuestUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferenceManager.setIsGuest(mContext, true);

                AppUtilityMethods.hideKeyBoard(mContext);
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            }
        });

        forGotpasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });
    }

    public void showDialogSignUpAlert() {
        final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        //mMailEdtText.setOnTouchListener(this);
        TextView alertHead = (TextView) dialog.findViewById(R.id.alertHead);
//
        Button dialogSubmitButton = (Button) dialog
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button dialogMayBelaterutton = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                dialog.dismiss();
            }
        });
        dialog.show();

//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.alert_dialog_ok_layout);
//        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
//        icon.setBackgroundResource(bgIcon);
//        icon.setImageResource(ico);
//        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
//        text.setText(msg);
//        textHead.setText(msgHead);
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent intent = new Intent(mContext, HomeActivity.class);
//                AppPreferenceManager.setIsGuest(mContext,false);
//                AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, true);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        dialog.show();

    }

    private void sendLoginToServer(String URL_LOGIN) {

        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_LOGIN);
        String[] name = {"access_token", "phone", "passwd", "device_type", "device_id"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), edtUserName.getText().toString(), edtPassword.getText().toString(), "2", FirebaseInstanceId.getInstance().getToken()};
//        System.out.println("The response is : " + FirebaseInstanceId.getInstance().getToken());
//        System.out.println("The response is : " + AppPreferenceManager.getAccessToken(mContext));
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
                            AppPreferenceManager.setUserId(mContext, secobj.optString(JTAG_PARENT_ID));
                            JSONArray studentListArray = secobj.getJSONArray(JTAG_STUDENTS);
                            System.out.println("Preference ---" + studentListArray.toString());
                            AppPreferenceManager.setStudentsResponseFromLoginAPI(mContext, studentListArray.toString());
                            if (studentListArray.length() > 0) {
                                studentModelsArrayList.clear();
                                //AppController.studentArrayList.clear();
                                for (int i = 0; i < studentListArray.length(); i++) {
                                    JSONObject sObject = studentListArray.getJSONObject(i);
                                    if (sObject.optString(JTAG_MAP_STATUS).equalsIgnoreCase("1")) {
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
                                        studentModels.setStudent_division_name(sObject.optString(JTAG_DIV));
                                        studentModels.setClass_name(sObject.optString(JTAG_CLASS));
                                        studentModels.setFather(sObject.optString(JTAG_FATHER));
                                        studentModels.setMother(sObject.optString(JTAG_MOTHER));
                                        studentModels.setMentor(sObject.optString(JTAG_MENTOR));
                                        studentModels.setElect1(sObject.optString(JTAG_ELECT1));
                                        studentModels.setElect2(sObject.optString(JTAG_ELECT2));

                                        studentModelsArrayList.add(studentModels);
                                    }
                                    //AppController.studentArrayList.add(studentModels);
                                }


                                if (studentModelsArrayList.size() == 1) {
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
                                    AppPreferenceManager.setStudentId(mContext, studentModelsArrayList.get(0).getId());
                                    AppPreferenceManager.setStudentName(mContext, studentModelsArrayList.get(0).getName());
                                    AppPreferenceManager.setStudentClassName(mContext, studentModelsArrayList.get(0).getClass_name());
                                    AppPreferenceManager.setStudentSectionName(mContext, studentModelsArrayList.get(0).getStudent_division_name());

                                } else {
                                    showStudentList(studentModelsArrayList);

                                }
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.no_student_available), R.drawable.infoicon, R.drawable.roundred);

                            }
                            //showDialogSignUpAlert((Activity) mContext, getString(R.string.success), getString(R.string.login_success_alert), R.drawable.tick,  R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSERNAMEORPASWD)) {
                            AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, false);

                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_usr_pswd), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, false);
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, false);
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.common_error), getString(R.string.invalid_usr_pswd), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                              //  sendLoginToServer(URL_PARENT_LOGIN);
                            }
                        });
                        sendLoginToServer(URL_PARENT_LOGIN);

                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });


    }

    private void showForgotPasswordDialog() {
        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        forgotEmail = (EditText) dialog.findViewById(R.id.text_email);
        forgotPhone = (EditText) dialog.findViewById(R.id.text_phone);
        //mMailEdtText.setOnTouchListener(this);
        TextView alertHead = (TextView) dialog.findViewById(R.id.alertHead);
//
        Button dialogSubmitButton = (Button) dialog
                .findViewById(R.id.btn_signup);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);

                if (forgotPhone.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phoneno), R.drawable.infoicon, R.drawable.roundblue);

                } else if (forgotPhone.getText().toString().trim().length() < 8) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_phone_eight_digitno), R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick,  R.drawable.roundblue);
                    if (AppUtilityMethods.isNetworkConnected(mContext)) {
                        sendForGotpassWord(URL_FORGOTPASSWORD);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
            }
        });

        Button dialogMayBelaterutton = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogMayBelaterutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendForGotpassWord(String URL_API) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_API);
        String[] name = {"access_token", "phone"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), forgotPhone.getText().toString()};

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
                            dialog.dismiss();

/*JSONArray responseArray = secobj.getJSONArray(JTAG_RESPONSE_ARRAY);
for (int i = 0; i < responseArray.length(); i++) {
JSONObject respObj = responseArray.getJSONObject(i);
PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USERS_ID));
}*/
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick, R.drawable.roundblue);
                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_user), R.drawable.infoicon, R.drawable.roundblue);

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
                        sendForGotpassWord(URL_FORGOTPASSWORD);
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

    @SuppressLint("WrongConstant")
    public void showStudentList(final ArrayList<StudentModels> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView studentList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
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

        StudentAdapter studentAdapter = new StudentAdapter(mContext, mStudentArray);
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
                        if (mStudentArray.get(position).getBoardid().equals("1")) {
                            String selectedFromList = "ISG";
                            AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                        } else {
                            String selectedFromList = "ISG-INT";
                            AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                        }
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        AppPreferenceManager.setIsGuest(mContext, false);
                        AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, true);
                        AppPreferenceManager.setStudentId(mContext, studentModelsArrayList.get(position).getId());
                        AppPreferenceManager.setStudentName(mContext, studentModelsArrayList.get(position).getName());
                        AppPreferenceManager.setStudentClassName(mContext, studentModelsArrayList.get(position).getClass_name());
                        AppPreferenceManager.setStudentSectionName(mContext, studentModelsArrayList.get(position).getStudent_division_name());

                        startActivity(intent);
                        finish();
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        /*Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLAR_TASK);
        AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, false);
        startActivity(homeIntent);
        finish();*/
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //AppPreferenceManager.
        startActivity(homeIntent);
    }
}