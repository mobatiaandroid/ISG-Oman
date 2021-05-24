package com.algubra.activity.homework;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.homework.adapter.HomeWorkListAdapter;
import com.algubra.activity.homework.model.HistoryModel;
import com.algubra.activity.homework.model.HomeWorkListModel;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PDFViewActivityHomeWork;
import com.algubra.activity.userprofile.EditProfileActivity;
import com.algubra.activity.worksheet.WorksheetListActivity;
import com.algubra.activity.worksheet.adapter.WorksheetListAdapter;
import com.algubra.activity.worksheet.model.WorksheetListModel;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AndroidMultiPartEntity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.CustomProgressBar;
import com.algubra.volleymanager.VolleyAPIManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeWorkListActivity extends Activity implements URLConstants, JsonTagConstants, StausCodes {
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    ArrayList<HomeWorkListModel> subjectList;
    ArrayList<HistoryModel> historyList;
    String subjectId="";
    Bundle extras;
    String typeExtention="";
    String url="";
    String name="";
    String filenameshare="";
    String  filePath="";
    String studentIdUpload="";
    String titleUpload="";
    private String responsecode="",status_code="";
    String staff_id="";
    String homework_id="";
    boolean isEmpty = false;
    File file;
    String pageFrom="";
    private static final int FILE_SELECT_CODE = 0;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUI();
        resetDisconnectTimer();
        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getList(URL_GET_HOMEWORK_LIST);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }
    private void getList(String urlGetTimetableList) {
        subjectList=new ArrayList<>();

        VolleyAPIManager volleyWrapper=new VolleyAPIManager(urlGetTimetableList);
        String[] name={"access_token","boardId","studentId","parentId","subjectId","page_frm"};
        String boardId="";
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            boardId="1";
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            boardId="2";
        }else{
            boardId="1";
        }
        String[] value={AppPreferenceManager.getAccessToken(mContext),boardId,
                AppPreferenceManager.getStudentId(mContext),AppPreferenceManager.getUserId(mContext),subjectId,pageFrom};
        System.out.print(" AccessToken : "+AppPreferenceManager.getAccessToken(mContext));
        System.out.print(" boardId : "+boardId);
        System.out.print(" studentId : "+ AppPreferenceManager.getStudentId(mContext));
        System.out.print(" parentId : "+ AppPreferenceManager.getUserId(mContext));
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

                            final JSONArray subjectArray = secobj.getJSONArray("homework_list");
                            if (subjectArray.length() > 0) {
                                isEmpty=false;
                                for (int i = 0; i < subjectArray.length(); i++) {
                                    JSONObject sObject = subjectArray.getJSONObject(i);
                                    HomeWorkListModel subjectModel=new HomeWorkListModel();
                                    subjectModel.setId(sObject.optString("id"));
                                    subjectModel.setTitle(sObject.optString(JTAG_WORKSHEET_TITLE));
                                    subjectModel.setFile(sObject.optString(JTAG_WORKSHEET_FILE));
                                    subjectModel.setTeacher(sObject.optString(JTAG_WORKSHEET_TEACHER));
                                    subjectModel.setType(sObject.optString(JTAG_WORKSHEET_TYPE));
                                    subjectModel.setTeacherId(sObject.optString("teacherId"));
                                    subjectModel.setDescription(sObject.optString("description"));
                                    subjectModel.setDue_date(sObject.optString("due_date"));
                                    subjectModel.setStatus(sObject.optString("status"));
                                    subjectModel.setSet_date(sObject.optString("set_date"));
                                    subjectModel.setOpen(false);
                                    historyList=new ArrayList<>();
                                    JSONArray historyArray=sObject.getJSONArray("student_hw_list");
                                    if (historyArray.length()>0)
                                    { for (int j=0;j<historyArray.length();j++)

                                    {
                                        JSONObject hObject = historyArray.getJSONObject(j);
                                        HistoryModel historyModel= new HistoryModel();
                                        historyModel.setTitle(hObject.optString("title"));
                                        historyModel.setFile(hObject.optString("file"));
                                        historyModel.setType(hObject.optString("type"));
                                        historyModel.setStatus(hObject.optString("status"));
                                        historyModel.setReason(hObject.optString("reason"));
                                        historyModel.setCorrected_file(hObject.optString("corrected_file"));
                                        historyList.add(historyModel);
                                    }
                                    }
                                    subjectModel.setmHistoryList(historyList);

                                    if(subjectList.size()==0)
                                    {
                                        subjectList.add(subjectModel);
                                    }
                                    else
                                    {
                                        String pushId=sObject.optString("id");
                                        boolean isFound=false;
                                        for (int q = 0; q<subjectList.size(); q++)
                                        {
                                            if (pushId.equalsIgnoreCase(subjectList.get(q).getId()))
                                            {
                                                isFound=true;
                                            }

                                        }
                                        if(!isFound)
                                        {
                                            subjectList.add(subjectModel);
                                        }
                                    }
                                }
                            /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                            messageList.setAdapter(mAboutusRecyclerviewAdapter);*/

                                HomeWorkListAdapter mSubjectAdapter=new HomeWorkListAdapter(mContext,subjectList);
                                messageList.setAdapter(mSubjectAdapter);
                                mSubjectAdapter.setOnBottomReachedListener(new OnBottomReachedListener()
                                {
                                    @Override
                                    public void onBottomReached(int position) {
                                        //your code goes here


                                        if (subjectList.size()>=20)
                                        {
                                            if (!isEmpty)
                                            {
                                                int listSize= subjectList.size();
                                                pageFrom=subjectList.get(listSize-1).getId();
                                                if(AppUtilityMethods.isNetworkConnected(mContext)) {
                                                    getList(URL_GET_HOMEWORK_LIST);
                                                }else{
                                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                                }
                                            }
                                        }


                                    }
                                });
                            }else{
                                isEmpty=true;
                                if (subjectList.size()==0)
                                {
                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon,  R.drawable.roundblue);

                                }
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
                        getList(URL_GET_HOMEWORK_LIST);

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

//    private void getList(String urlGetTimetableList) {
//        VolleyAPIManager volleyWrapper=new VolleyAPIManager(urlGetTimetableList);
//        String[] name={"access_token","boardId","studentId","parentId","subjectId","page_frm"};
//        String boardId="";
//        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
//            boardId="1";
//        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
//            boardId="2";
//        }else{
//            boardId="1";
//        }
//        String[] value={AppPreferenceManager.getAccessToken(mContext),boardId,
//                AppPreferenceManager.getStudentId(mContext),AppPreferenceManager.getUserId(mContext),subjectId,pageFrom};
//        System.out.print(" AccessToken : "+AppPreferenceManager.getAccessToken(mContext));
//        System.out.print(" boardId : "+boardId);
//        System.out.print(" studentId : "+ AppPreferenceManager.getStudentId(mContext));
//        System.out.print(" parentId : "+ AppPreferenceManager.getUserId(mContext));
//        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
//            @Override
//            public void responseSuccess(String successResponse) {
//                System.out.println("The Homework response is" + successResponse);
//                try {
//                    JSONObject obj = new JSONObject(successResponse);
//
//                    String response_code = obj.getString(JTAG_RESPONSECODE);
//                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
//                        System.out.println("The response is" + response_code);
//
//                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
//                        String status_code = secobj.getString(JTAG_STATUSCODE);
//                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
//
//                            final JSONArray subjectArray = secobj.getJSONArray("homework_list");
//                            if (subjectArray.length() > 0) {
//                                isEmpty=false;
//                                for (int i = 0; i < subjectArray.length(); i++) {
//                                    JSONObject sObject = subjectArray.getJSONObject(i);
//                                    HomeWorkListModel subjectModel=new HomeWorkListModel();
//                                    if (subjectList.size()==0)
//                                    {
//                                        subjectModel.setId(sObject.optString("id"));
//                                        subjectModel.setTitle(sObject.optString(JTAG_WORKSHEET_TITLE));
//                                        subjectModel.setFile(sObject.optString(JTAG_WORKSHEET_FILE));
//                                        subjectModel.setTeacher(sObject.optString(JTAG_WORKSHEET_TEACHER));
//                                        subjectModel.setType(sObject.optString(JTAG_WORKSHEET_TYPE));
//                                        subjectModel.setTeacherId(sObject.optString("teacherId"));
//                                        subjectModel.setDescription(sObject.optString("description"));
//                                        subjectModel.setDue_date(sObject.optString("due_date"));
//                                        subjectModel.setStatus(sObject.optString("status"));
//                                        subjectModel.setSet_date(sObject.optString("set_date"));
//                                        subjectModel.setOpen(false);
//                                        JSONArray historyArray=sObject.getJSONArray("student_hw_list");
//                                        historyList.clear();
//                                        if (historyArray.length()>0)
//                                        {
//
//                                            for (int j=0;j<historyArray.length();j++)
//                                            {
//                                                JSONObject hObject = historyArray.getJSONObject(j);
//                                                HistoryModel historyModel= new HistoryModel();
//                                                historyModel.setTitle(hObject.optString("title"));
//                                                historyModel.setFile(hObject.optString("file"));
//                                                historyModel.setType(hObject.optString("type"));
//                                                historyModel.setStatus(hObject.optString("status"));
//                                                historyModel.setReason(hObject.optString("reason"));
//                                                historyModel.setCorrected_file(hObject.optString("corrected_file"));
//                                                System.out.println("CORRECTEDFILEDATA1:"+historyModel.getCorrected_file());
//
//                                                historyList.add(historyModel);
//                                            }
//                                        }
//                                        subjectModel.setmHistoryList(historyList);
//                                        subjectList.add(subjectModel);
//                                    }
//                                    else {
//
//                                            String pushId=sObject.optString("id");
//                                            boolean isFound=false;
//                                        for (int q = 0; q<subjectList.size(); q++)
//                                        {
//                                            if (pushId.equalsIgnoreCase(subjectList.get(q).getId()))
//                                            {
//                                                isFound=true;
//                                            }
//
//                                        }
//                                        if (!isFound)
//                                        {
//                                            subjectModel.setId(sObject.optString("id"));
//                                            subjectModel.setTitle(sObject.optString(JTAG_WORKSHEET_TITLE));
//                                            subjectModel.setFile(sObject.optString(JTAG_WORKSHEET_FILE));
//                                            subjectModel.setTeacher(sObject.optString(JTAG_WORKSHEET_TEACHER));
//                                            subjectModel.setType(sObject.optString(JTAG_WORKSHEET_TYPE));
//                                            subjectModel.setTeacherId(sObject.optString("teacherId"));
//                                            subjectModel.setDescription(sObject.optString("description"));
//                                            subjectModel.setDue_date(sObject.optString("due_date"));
//                                            subjectModel.setStatus(sObject.optString("status"));
//                                            subjectModel.setSet_date(sObject.optString("set_date"));
//                                            subjectModel.setOpen(false);
//                                            JSONArray historyArray=sObject.getJSONArray("student_hw_list");
//                                            historyList.clear();
//                                            if (historyArray.length()>0)
//                                            {
//                                                for (int j=0;j<historyArray.length();j++)
//                                                {
//                                                    JSONObject hObject = historyArray.getJSONObject(j);
//                                                    HistoryModel historyModel= new HistoryModel();
//                                                    historyModel.setTitle(hObject.optString("title"));
//                                                    historyModel.setReason(hObject.optString("reason"));
//                                                    historyModel.setFile(hObject.optString("file"));
//                                                    historyModel.setType(hObject.optString("type"));
//                                                    historyModel.setStatus(hObject.optString("status"));
//                                                    historyModel.setCorrected_file(hObject.optString("corrected_file"));
//                                                    System.out.println("CORRECTEDFILEDATA2:"+historyModel.getCorrected_file());
//
//                                                    historyList.add(historyModel);
//                                                }
//                                            }
//                                            subjectModel.setmHistoryList(historyList);
//                                            subjectList.add(subjectModel);
//                                        }
//                                    }
//                                    }
//                                /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
//                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
//
//                                HomeWorkListAdapter mSubjectAdapter=new HomeWorkListAdapter(mContext,subjectList);
//                                messageList.setAdapter(mSubjectAdapter);
//                                mSubjectAdapter.setOnBottomReachedListener(new OnBottomReachedListener()
//                                {
//                                    @Override
//                                    public void onBottomReached(int position) {
//                                        //your code goes here
//
//
//                                        if (subjectList.size()>=20)
//                                        {
//                                            if (!isEmpty)
//                                            {
//                                                int listSize= subjectList.size();
//                                                pageFrom=subjectList.get(listSize-1).getId();
//                                                if(AppUtilityMethods.isNetworkConnected(mContext)) {
//                                                    getList(URL_GET_HOMEWORK_LIST);
//                                                }else{
//                                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//                                                }
//                                            }
//                                        }
//
//
//                                    }
//                                });
//                            }else{
//                                isEmpty=true;
//                                if (subjectList.size()==0)
//                                {
//                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon,  R.drawable.roundblue);
//
//                                }
//                            }
//
//                        } else {
//                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon,  R.drawable.roundblue);
//                        }
//                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
//
//                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon,  R.drawable.roundblue);
//
//                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
//                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        getList(URL_GET_HOMEWORK_LIST);
//
//                    } else {
//                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);
//
//                    }
//                } catch (Exception ex) {
//                    System.out.println("The Exception in edit profile is" + ex.toString());
//                }
//
//            }
//
//            @Override
//            public void responseFailure(String failureResponse) {
//				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
//						, getResources().getString(R.string.ok));
//				dialog.show();*/
//                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);
//
//            }
//        });
//
//    }

    @SuppressLint("WrongConstant")
    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getString("subjectId");
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);

        messageList = (RecyclerView) findViewById(R.id.settingItemList);

        messageList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(llm);

        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        messageList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(HomeWorkListActivity.this, "Homework");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
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

        pageFrom="0";
        messageList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), messageList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        System.out.println("History list size"+subjectList.get(position).getmHistoryList().size());
                        String title = subjectList.get(position).getTitle();

                        title=title.replace("/", "_");
                        if(subjectList.get(position).getType().equalsIgnoreCase("pdf"))
                        {
                            typeExtention=subjectList.get(position).getType();
                            url=subjectList.get(position).getFile();
                            name=title;
                            String filename = name.replace(" ", "_");
                            filenameshare = filename + "."+typeExtention;
                            titleUpload=title;
                            staff_id=subjectList.get(position).getTeacherId();
                            homework_id=subjectList.get(position).getId();
                            //  showEditDialogue(typeExtention,url,name);

                            // System.out.println("history List details"+subjectList.get(position).getmHistoryList().get(0).getTitle());
                            Intent intent = new Intent(HomeWorkListActivity.this,HomeWorkListDetailActivityNew.class);
                            intent.putExtra("typeExtention",typeExtention);
                            intent.putExtra("name",name);
                            intent.putExtra("filenameshare",filenameshare);
                            intent.putExtra("titleUpload",titleUpload);
                            intent.putExtra("staff_id",staff_id);
                            intent.putExtra("homework_id",homework_id);
                            intent.putExtra("subjectId",subjectId);
                            intent.putExtra("url",url);
                            intent.putExtra("staff_name",subjectList.get(position).getTeacher());
                            intent.putExtra("history_list",subjectList.get(position).getmHistoryList());
                            intent.putExtra("description",subjectList.get(position).getDescription());
                            intent.putExtra("due_date",subjectList.get(position).getDue_date());
                            intent.putExtra("status",subjectList.get(position).getStatus());
                            intent.putExtra("set_date",subjectList.get(position).getSet_date());
                            startActivity(intent);
                        }

                        else
                        {
                            typeExtention=subjectList.get(position).getType();
                            url=subjectList.get(position).getFile();
                            name=title;
                            String filename = name.replace(" ", "_");
                            filenameshare = filename + "."+typeExtention;
                            titleUpload=title;
                            staff_id=subjectList.get(position).getTeacherId();
                            homework_id=subjectList.get(position).getId();
                            //  showEditDialogue(typeExtention,url,name);
                            Intent intent = new Intent(HomeWorkListActivity.this,HomeWorkListDetailActivityNew.class);
                            intent.putExtra("typeExtention",typeExtention);
                            intent.putExtra("name",name);
                            intent.putExtra("filenameshare",filenameshare);
                            intent.putExtra("titleUpload",titleUpload);
                            intent.putExtra("staff_id",staff_id);
                            intent.putExtra("homework_id",homework_id);
                            intent.putExtra("subjectId",subjectId);
                            intent.putExtra("url",url);
                            intent.putExtra("staff_name",subjectList.get(position).getTeacher());
                            intent.putExtra("history_list",subjectList.get(position).getmHistoryList());
                            intent.putExtra("description",subjectList.get(position).getDescription());
                            intent.putExtra("due_date",subjectList.get(position).getDue_date());
                            intent.putExtra("status",subjectList.get(position).getStatus());
                            intent.putExtra("set_date",subjectList.get(position).getSet_date());
                            startActivity(intent);

                        }

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }



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
        subjectList=new ArrayList<>();
        pageFrom="0";
        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getList(URL_GET_HOMEWORK_LIST);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
        resetDisconnectTimer();
    }

    @Override
    public void onStop()
      {
        super.onStop();
//        stopDisconnectTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDisconnectTimer();
    }
    @Override protected void onRestart() { super.onRestart();
        System.out.println("TestRestart:hshhs");
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(HomeWorkListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }

    private boolean CheckPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(mContext,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(HomeWorkListActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    public class DownloadPDF extends AsyncTask<String, Void, Void>
    {

        private Exception exception;
        private ProgressDialog dialog;
        String filename = name.replace(" ", "_");
        String fileName = filename + "."+typeExtention;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            System.out.println("filename"+fileName);
            try {

                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            System.out.println("file.exists() = " + file.exists());
            if (file.exists()) {
                Toast.makeText(mContext, "File Downloaded to download/"+fileName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public class loadPDF extends AsyncTask<String, Void, Void>
    {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(PDFViewActivity.this);
//            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
//            dialog.show();
        }

        protected Void doInBackground(String... urls)
        {
            URL u = null;
            try {
                String fileName = filenameshare;
                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            // System.out.println("file.exists() = " + file.exists());
            // pdf.fromUri(uri);

//            LoadingPdf(file);

            //pdf.fromFile(file).defaultPage(1).enableSwipe(true).load();


            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }

    }
    private String getFilepath(String filename) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + filename).getPath();

    }

    private void ChooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("Chooser", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = ChooserFileUtils.getPath(this, uri);
                        filePath = ChooserFileUtils.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Log.d("Chooser", "File Path: " + path);
                    if (filePath.equalsIgnoreCase(""))
                    {

                    }
                    else
                    {
                        HomeWorkListActivity.UploadFileToServer uploadFileToServer = new HomeWorkListActivity.UploadFileToServer();
                        uploadFileToServer.execute();
                    }
                    //Log.d("Chooser", "File Path: " + path);
                  //  PathString.setText(path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
                    Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private class UploadFileToServer extends AsyncTask<Void, Integer, String>
    {
        final CustomProgressBar pDialog = new CustomProgressBar(mContext,
                R.drawable.spinner);
        private JSONObject obj;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();


                HttpPost httppost = new HttpPost(URL_UPLOAD_HOMEWORK);

                //for (int i = 0; i < mHashMap.size(); i++) {
                //String path = String.valueOf(mImageCaptureUriFile);
                // if()
                if(!filePath.equals("")) {
                    file = new File(filePath);
                }else{

                    file=new File("");
                }
                //}

                System.out.println("File path---"+filePath);


                FileBody bin1 = new FileBody(file);

                AndroidMultiPartEntity entity;
                entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            }
                        });

                //for (int i = 0; i < mFileBody.size(); i++) {
                System.out.println("File path---"+bin1);

                entity.addPart("uploaded", bin1);

                // }
                String boardId="";
                if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
                    boardId="1";
                }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
                    boardId="2";
                }else{
                    boardId="1";
                }
                Charset chars = Charset.forName("UTF-8");
                entity.addPart("boardId", new StringBody(boardId));
                entity.addPart("studentId", new StringBody(AppPreferenceManager.getStudentId(mContext)));
                entity.addPart("parentId", new StringBody(AppPreferenceManager.getUserId(mContext)));
                entity.addPart("subjectId", new StringBody(subjectId));
                entity.addPart("title", new StringBody(titleUpload));
                entity.addPart("staffId", new StringBody(staff_id));
                entity.addPart("homeworkId", new StringBody(homework_id));
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Stat code---"+statusCode);
                if (statusCode == 200) {

                    responseString = EntityUtils.toString(r_entity);
                    System.out.println("Stat code---"+responseString);

                    try {
                        obj = new JSONObject(responseString);
                        responsecode=obj.optString("responsecode");
                        JSONObject jsonObject=obj.getJSONObject("response");
                        if(responsecode.equals("200")){
                            status_code = jsonObject.optString("statuscode");

                        }else{
                            // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);

                    }
                } else {

                    responseString = EntityUtils.toString(r_entity);
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (status_code.equals("201"))
            {


               // showDialogSignUpAlert((Activity) mContext, getString(R.string.success), getString(R.string.succ_prof), R.drawable.tick,  R.drawable.roundblue);
                //JSONObject objresponse = obj.optJSONObject("response");
                //String errorCode = objresponse.optString("statuscode");
                //CustomStatusDialog(errorCode);
            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "No Changes Detected", R.drawable.exclamationicon,  R.drawable.roundblue);

                // CustomStatusDialog(responsecode);
            }
        }

    }

}

