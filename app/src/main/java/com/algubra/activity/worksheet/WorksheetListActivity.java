package com.algubra.activity.worksheet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.homework.OnBottomReachedListener;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PDFViewActivityWH;
import com.algubra.activity.worksheet.adapter.WorksheetListAdapter;
import com.algubra.activity.worksheet.model.WorksheetListModel;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WorksheetListActivity extends Activity implements URLConstants, JsonTagConstants, StausCodes {
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    ArrayList<WorksheetListModel> subjectList;
    String subjectId="";
    Bundle extras;
    String typeExtention="";
    String url="";
    String name="";
    String filenameshare="";
    String pageFrom="";
    boolean isEmpty = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUI();
        resetDisconnectTimer();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        if(AppUtilityMethods.isNetworkConnected(mContext)) {
            getList(URL_GET_WORKSHEET_LIST);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
        PermissionListener permissionListenerGallery = new PermissionListener() {
            @Override
            public void onPermissionGranted() {


            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(mContext, "Permission Denied\n", Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(mContext)
                .setPermissionListener(permissionListenerGallery)
                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }


    private void getList(String urlGetTimetableList) {
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
                AppPreferenceManager.getStudentId(mContext),AppPreferenceManager.getUserId(mContext),subjectId, pageFrom};
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

                            JSONArray subjectArray = secobj.getJSONArray(JTAG_WORKSHEET_ARRAY);
                            System.out.println("The response is" + subjectArray);
                            if (subjectArray.length() > 0) {
                                isEmpty=false;

                                for (int i = 0; i < subjectArray.length(); i++) {
                                    JSONObject sObject = subjectArray.getJSONObject(i);
                                    WorksheetListModel subjectModel=new WorksheetListModel();
                                    if (subjectList.size()==0)
                                    {
                                        subjectModel.setTitle(sObject.optString(JTAG_WORKSHEET_TITLE));
                                        subjectModel.setFile(sObject.optString(JTAG_WORKSHEET_FILE));
                                        subjectModel.setTeacher(sObject.optString(JTAG_WORKSHEET_TEACHER));
                                        subjectModel.setType(sObject.optString(JTAG_WORKSHEET_TYPE));
                                        subjectModel.setId(sObject.optString("id"));
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
                                        if (!isFound)
                                        {
                                            subjectModel.setTitle(sObject.optString(JTAG_WORKSHEET_TITLE));
                                            subjectModel.setFile(sObject.optString(JTAG_WORKSHEET_FILE));
                                            subjectModel.setTeacher(sObject.optString(JTAG_WORKSHEET_TEACHER));
                                            subjectModel.setType(sObject.optString(JTAG_WORKSHEET_TYPE));
                                            subjectModel.setId(sObject.optString("id"));
                                            subjectList.add(subjectModel);
                                        }
                                    }

                                }
                                /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
                                WorksheetListAdapter mSubjectAdapter=new WorksheetListAdapter(mContext,subjectList);
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
                                                    getList(URL_GET_WORKSHEET_LIST);
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
                        getList(URL_GET_WORKSHEET_LIST);

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

    @SuppressLint("WrongConstant")
    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getString("subjectId");
        }
        pageFrom="0";
        subjectList=new ArrayList<>();
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);

        messageList = (RecyclerView) findViewById(R.id.settingItemList);

        messageList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(llm);

        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        messageList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(WorksheetListActivity.this, "Worksheet");
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


        messageList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), messageList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {

                        String title = subjectList.get(position).getTitle();

                        title=title.replace("/", "_");

                        if (subjectList.get(position).getType().equalsIgnoreCase("pdf")) {
                            Intent browserIntent = new Intent(WorksheetListActivity.this, PDFViewActivityWH.class);
                            browserIntent.putExtra("pdf_url", subjectList.get(position).getFile());
                            browserIntent.putExtra("title",title);
                            browserIntent.putExtra("filename", title);
                            startActivity(browserIntent);
                        } else
                        {
                             typeExtention=subjectList.get(position).getType();
                             url=subjectList.get(position).getFile();
                             name=title;
                             String filename = name.replace(" ", "_");
                             filenameshare = filename + "."+typeExtention;
                            showEditDialogue(typeExtention,url,name);

                        }


                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));


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
            AppPreferenceManager.setIsGuest(CircularActivity.this, false);
            AppPreferenceManager.setUserId(CircularActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(CircularActivity.this, false);
            AppPreferenceManager.setSchoolSelection(CircularActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {
                Intent mIntent = new Intent(CircularActivity.this, LoginActivity.class);
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
    @Override protected void onRestart() { super.onRestart();
        System.out.println("TestRestart:hshhs");
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(WorksheetListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
    public  void showEditDialogue(String typeExtention,String url,String name)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_options_worklist);
        ImageView cancelTxt = (ImageView) dialog.findViewById(R.id.close);
        ImageView shareImg = (ImageView) dialog.findViewById(R.id.shareImg);
        ImageView downloadImg = (ImageView) dialog.findViewById(R.id.downloadImg);
        LinearLayout downloadLinear = (LinearLayout) dialog.findViewById(R.id.downloadLinear);
        LinearLayout shareLinear = (LinearLayout) dialog.findViewById(R.id.shareLinear);
        LinearLayout headLinear = (LinearLayout) dialog.findViewById(R.id.headLinear);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            headLinear.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            shareImg.setImageResource(R.drawable.share_new);
            downloadImg.setImageResource(R.drawable.download_new);
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            headLinear.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            shareImg.setImageResource(R.drawable.share_blue);
            downloadImg.setImageResource(R.drawable.download_blue);
        }
        dialog.show();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        new WorksheetListActivity.loadPDF().execute();
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // dialog.dismiss();
                new WorksheetListActivity.DownloadPDF().execute();

            }
        });
        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppUtilityMethods.isNetworkConnected(mContext))
                {
                    System.out.println("Working click!!!!!!!!");
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(getFilepath(filenameshare));
                    if(fileWithinMyDir.exists())
                    {
                        System.out.println("Working click@@@@@@@@@");
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                    else
                    {
                        if (AppUtilityMethods.isNetworkConnected(mContext)) {

                           // new WorksheetListActivity.loadPDF().execute();
                            if(fileWithinMyDir.exists())
                            {
                                System.out.println("Working click@@@@@@@@@");
                                intentShareFile.setType("application/pdf");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                        }
                        System.out.println("Working click!D");
                    }
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }

            }
        });


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
            if (file.exists())
            {
                Toast.makeText(mContext, "File Downloaded to download/"+fileName, Toast.LENGTH_SHORT).show();

            }
            else
                {
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
}
