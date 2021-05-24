package com.algubra.activity.login;

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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.adapter.StudentAdapter;
import com.algubra.activity.login.model.StudentModels;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.DividerItemDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 18/4/17.
 */
public class ConfirmLogin extends Activity implements JsonTagConstants,URLConstants,StausCodes{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext=this;
    TextView textViewResendPassword;
    EditText editTextPassword;
    Button btnSubmit;
    Bundle extras;
    String mobileno;
    String regStatus="";
    public ArrayList<StudentModels> studentModelsArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_login);
        initUI();
        if (regStatus.equalsIgnoreCase(STATUSCODE_REG_SUCC_EMAILNOTLINKED))
        {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.regstered_butnoemail), R.drawable.infoicon,  R.drawable.roundblue);

        }
        else
        {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.registration_successful), R.drawable.infoicon,  R.drawable.roundblue);

        }

    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
           mobileno=extras.getString("mobileno");
            regStatus=extras.getString("STATUSCODE_REG_SUCC_EMAILNOTLINKED");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        textViewResendPassword= (TextView) findViewById(R.id.textViewResendPassword);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        btnSubmit= (Button) findViewById(R.id.btnSubmit);
        textViewResendPassword.setPaintFlags(textViewResendPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewResendPassword.setText(getString(R.string.resend));
        headermanager = new HeaderManager(ConfirmLogin.this, getString(R.string.resendpassword_login));
        headermanager.getHeader(relativeHeader, 2);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.homeicon,
                R.drawable.homeicon);

//        editTextPassword.setText(mobileno);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPassword.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_password), R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    //showDialogRegSuccessAlert((Activity) mContext, getString(R.string.alert_heading), getString(R.string.req_success), R.drawable.tick,  R.drawable.roundblue);


                    if (AppUtilityMethods.isNetworkConnected(mContext)) {
                        sendLoginToServer(URL_PARENT_LOGIN);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                }
                /*Intent intent = new Intent(mContext, HomeActivity.class);
                AppPreferenceManager.setIsGuest(mContext,false);
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
                Intent intent = new Intent(mContext, HomeActivity.class);
                AppPreferenceManager.setIsGuest(mContext,false);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();

    }

    private void sendLoginToServer(String URL_LOGIN){
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(URL_LOGIN);
        String[] name={"access_token","phone","passwd","device_type","device_id"};
        String[] value={AppPreferenceManager.getAccessToken(mContext),mobileno,editTextPassword.getText().toString(),"2", FirebaseInstanceId.getInstance().getToken()};

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
                                    studentModels.setStudent_division_name(sObject.optString(JTAG_DIV));
                                    studentModels.setClass_name(sObject.optString(JTAG_CLASS));
                                    studentModels.setFather(sObject.optString(JTAG_FATHER));
                                    studentModels.setMother(sObject.optString(JTAG_MOTHER));
                                    studentModels.setMentor(sObject.optString(JTAG_MENTOR));
                                    studentModels.setElect1(sObject.optString(JTAG_ELECT1));
                                    studentModels.setElect2(sObject.optString(JTAG_ELECT2));
                                    studentModelsArrayList.add(studentModels);
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
                                    intent.putExtra("confirmlogin",true);
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
                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSERNAMEORPASWD)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_usr_pswd), R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_MISSING_PARAMETER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                         /*else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);

                        }*/
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                sendLoginToServer(URL_PARENT_LOGIN);
                            }
                        });
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
                        if(mStudentArray.get(position).getBoardid().equals("1")){
                            String selectedFromList="ISG";
                            AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                        }else {
                            String selectedFromList="ISG-INT";
                            AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                        }
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        AppPreferenceManager.setIsGuest(mContext,false);
                        AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, true);
                        AppPreferenceManager.setStudentId(mContext, studentModelsArrayList.get(position).getId());
                        AppPreferenceManager.setStudentName(mContext, studentModelsArrayList.get(position).getName());
                        AppPreferenceManager.setStudentClassName(mContext, studentModelsArrayList.get(position).getClass_name());
                        AppPreferenceManager.setStudentSectionName(mContext, studentModelsArrayList.get(position).getStudent_division_name());
                        intent.putExtra("confirmlogin",true);

                        startActivity(intent);
                        finish();
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

}
