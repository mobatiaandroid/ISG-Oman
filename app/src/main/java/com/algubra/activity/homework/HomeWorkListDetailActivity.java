package com.algubra.activity.homework;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.homework.adapter.HomeWorkHistoryListAdapter;
import com.algubra.activity.homework.adapter.HomeWorkHistoryListAdapterNew;
import com.algubra.activity.homework.adapter.HomeWorkListAdapter;
import com.algubra.activity.homework.model.HistoryModel;
import com.algubra.activity.homework.model.HomeWorkListModel;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFTest;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PDFViewActivityWH;
import com.algubra.activity.pdf.PdfReader;
import com.algubra.activity.pdf.PdfReaderActivity;
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
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

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
import java.util.List;

import static com.algubra.activity.homework.ChooserFileUtils.getDataColumn;

public class HomeWorkListDetailActivity extends Activity implements URLConstants, JsonTagConstants, StausCodes {
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,previewImg;
    Context mContext = this;
    Activity mActivity;
    ArrayList<HomeWorkListModel> subjectList;
    String subjectId="";
    Bundle extras;
    String typeExtention="";
    String url="";
    String name="";
    String filenameshare="";
    String filePath="";
    String titleUpload="";

    String staff_id="";
    String homework_id="";
    String staffname="";
    LinearLayout downloadLinear,shareLinear,historyLinear,uploadHw,clickLinear;
    TextView staff_name,titleName,fileNameTxt,headHistory,description,setDate;
    ImageView downloadImg,shareImg,staff_img,fileLogo,uploadImg;
    ArrayList<HistoryModel>history_list;
    File file;
    boolean isUploadVisible=true;
    private String responsecode="",status_code="";
    private static final int FILE_SELECT_CODE = 0;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private final static int FILE_REQUEST_CODE = 1;
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    String Event = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_other_type);
        mActivity = this;
        initUI();
        resetDisconnectTimer();

    }
    @SuppressLint("WrongConstant")
    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getString("subjectId");
            typeExtention = extras.getString("typeExtention");
            name = extras.getString("name");
            filenameshare = extras.getString("filenameshare");
            titleUpload = extras.getString("titleUpload");
            staff_id = extras.getString("staff_id");
            homework_id = extras.getString("homework_id");
            staffname = extras.getString("staff_name");
            url = extras.getString("url");
            history_list=(ArrayList<HistoryModel>) getIntent().getSerializableExtra("history_list");
            System.out.println("history_list size"+history_list.size()+" URL "+url+" EXT "+typeExtention);
        }
        subjectList=new ArrayList<>();

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        messageList = (RecyclerView) findViewById(R.id.settingItemList);
        downloadLinear = (LinearLayout) findViewById(R.id.downloadLinear);
        shareLinear = (LinearLayout) findViewById(R.id.shareLinear);
        historyLinear = (LinearLayout) findViewById(R.id.historyLinear);
        staff_name = (TextView) findViewById(R.id.staff_name);
        headHistory = (TextView) findViewById(R.id.headHistory);
        titleName = (TextView) findViewById(R.id.titleName);
        fileNameTxt = (TextView) findViewById(R.id.fileNameTxt);
        description = (TextView) findViewById(R.id.description);
        setDate = (TextView) findViewById(R.id.setDate);
        downloadImg = (ImageView) findViewById(R.id.downloadImg);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        staff_img = (ImageView) findViewById(R.id.staff_img);
        uploadHw = (LinearLayout) findViewById(R.id.uploadHw);
        clickLinear = (LinearLayout) findViewById(R.id.clickLinear);
        fileLogo = (ImageView) findViewById(R.id.fileLogo);
        uploadImg = (ImageView) findViewById(R.id.uploadImg);
        previewImg = (ImageView) findViewById(R.id.previewImg);
        messageList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(llm);

        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        messageList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(HomeWorkListDetailActivity.this, "Homework");
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
        previewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeExtention.equalsIgnoreCase("pdf"))
                {
                         Intent browserIntent = new Intent(HomeWorkListDetailActivity.this, PDFTest.class);
                            browserIntent.putExtra("pdf_url", url); // url
                            System.out.println("url print"+url);
                            browserIntent.putExtra("title", name);  // name
                            browserIntent.putExtra("filename", name); //name
                            startActivity(browserIntent);
                }else {
                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                    intent.putExtra("pdf_url",url);
                    intent.putExtra("title",name);
                    intent.putExtra("type",typeExtention);
                    startActivity(intent);
                }
            }
        });
        clickLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeExtention.equalsIgnoreCase("pdf"))
                {
                         Intent browserIntent = new Intent(HomeWorkListDetailActivity.this, PDFTest.class);
                            browserIntent.putExtra("pdf_url", url); // url
                            System.out.println("url print"+url);
                            browserIntent.putExtra("title", name);  // name
                            browserIntent.putExtra("filename", name); //name
                            startActivity(browserIntent);
                }else {
                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                    intent.putExtra("pdf_url",url);
                    intent.putExtra("title",name);
                    intent.putExtra("type",typeExtention);
                    startActivity(intent);
                }
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        if (staffname.equalsIgnoreCase(""))
        {
            staff_name.setVisibility(View.GONE);
        }
        else
        {
            staff_name.setVisibility(View.VISIBLE);
            staff_name.setText(staffname);
        }
        if (name.equalsIgnoreCase(""))
        {
            titleName.setVisibility(View.GONE);
        }
        else
        {
            titleName.setVisibility(View.VISIBLE);
            titleName.setText(name);
        }
        fileNameTxt.setText(titleUpload+"."+typeExtention);
        if (typeExtention.equalsIgnoreCase("ppt")||typeExtention.equalsIgnoreCase("pptx"))
        {
            fileLogo.setImageResource(R.drawable.powerpoint);
            previewImg.setVisibility(View.GONE);
        }
        else if (typeExtention.equalsIgnoreCase("xlxs")||typeExtention.equalsIgnoreCase("xlx"))

        {
            fileLogo.setImageResource(R.drawable.excel);
            previewImg.setVisibility(View.GONE);
        }
        else if (typeExtention.equalsIgnoreCase("jpg")||typeExtention.equalsIgnoreCase("png")||typeExtention.equalsIgnoreCase("jpeg")||typeExtention.equalsIgnoreCase("JPG")||typeExtention.equalsIgnoreCase("JPEG")||typeExtention.equalsIgnoreCase("PNG"))
        {
            fileLogo.setImageResource(R.drawable.image);
            previewImg.setVisibility(View.GONE);
        }
        else if (typeExtention.equalsIgnoreCase("doc")||typeExtention.equalsIgnoreCase("docx"))
        {
            fileLogo.setImageResource(R.drawable.word);
            previewImg.setVisibility(View.GONE);
        }
        else if (typeExtention.equalsIgnoreCase("pdf"))
        {
            fileLogo.setImageResource(R.drawable.pdfimg);
            previewImg.setVisibility(View.GONE);
        }
        if (history_list.size()>0)
        {

            historyLinear.setVisibility(View.VISIBLE);
            HomeWorkHistoryListAdapterNew mSubjectAdapter=new HomeWorkHistoryListAdapterNew(mContext,history_list);
            messageList.setAdapter(mSubjectAdapter);
            for (int i=0;i<history_list.size();i++)
            {
                if (history_list.get(i).getStatus().equalsIgnoreCase("3"))
                {
                    isUploadVisible=false;
                }
            }
            if (isUploadVisible)
            {
                uploadHw.setVisibility(View.VISIBLE);
                uploadImg.setVisibility(View.VISIBLE);
            }
            else
            {
                uploadHw.setVisibility(View.GONE);
                uploadImg.setVisibility(View.GONE);
            }
        }
        else
        {
            historyLinear.setVisibility(View.GONE);
            uploadHw.setVisibility(View.VISIBLE);
            uploadImg.setVisibility(View.VISIBLE);
        }

        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
          //green
            staff_img.setImageResource(R.drawable.teacher);
            downloadImg.setImageResource(R.drawable.download_new);
            shareImg.setImageResource(R.drawable.share_new);
            uploadHw.setBackgroundColor(getResources().getColor(R.color.login_button_bg));
            previewImg.setImageResource(R.drawable.preview);
            headHistory.setBackgroundColor(getResources().getColor(R.color.login_button_bg));

        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
           //blue
            staff_img.setImageResource(R.drawable.teacher_blue);
            downloadImg.setImageResource(R.drawable.download_blue);
            shareImg.setImageResource(R.drawable.share_blue);
            uploadHw.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));
            previewImg.setImageResource(R.drawable.preview_blue);
            headHistory.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));
        }

        messageList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), messageList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {


                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission())
                {
                    Event  = "Down";
                    new HomeWorkListDetailActivity.DownloadPDF().execute();
                }

            }
        });
        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission())
                {
                    Event = "SHARE";
//                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    new HomeWorkListDetailActivity.DownloadPDF().execute();
                }

//                    System.out.println("Working click!!!!!!!!");
//                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                    File fileWithinMyDir = new File(getFilepath(filenameshare));
//                    if(fileWithinMyDir.exists())
//                    {
//                        System.out.println("Working click@@@@@@@@@");
//                        intentShareFile.setType("application/pdf");
//                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
//                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
//                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                    }
//                    else
//                    {
//                        if (AppUtilityMethods.isNetworkConnected(mContext)) {
//
//                            // new WorksheetListActivity.loadPDF().execute();
//                            if(fileWithinMyDir.exists())
//                            {
//                                System.out.println("Working click@@@@@@@@@");
//                                intentShareFile.setType("application/pdf");
//                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
//                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
//                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                            }
//                        } else {
//                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//                        }
//                        System.out.println("Working click!D");
//                    }
//                } else {
//                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//                }
            }
        });
        uploadHw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission())
                {
                  //  ChooseFile();
                    mediaFiles.clear();
                    NewMethod();
                }
            }
        });
    }

    private void NewMethod() {
        Intent intent = new Intent(mContext, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setSelectedMediaFiles(mediaFiles)
                .setShowFiles(true)
                .setShowImages(true)
                .setShowAudios(false)
                .setShowVideos(false)
                .setIgnoreNoMedia(false)
                .enableVideoCapture(false)
                .enableImageCapture(false)
                .setIgnoreHiddenFile(false)
                .setMaxSelection(1)
                .build());
        startActivityForResult(intent, FILE_REQUEST_CODE);
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
        //System.out.println("TestRestart:hshhs");
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(HomeWorkListDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
//    public  void showEditDialogue(String typeExtention,String url,String name)
//    {
//        final Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialogue_options_worklist_homework);
//        ImageView cancelTxt = (ImageView) dialog.findViewById(R.id.close);
//        ImageView shareImg = (ImageView) dialog.findViewById(R.id.shareImg);
//        ImageView downloadImg = (ImageView) dialog.findViewById(R.id.downloadImg);
//        ImageView uploadImg = (ImageView) dialog.findViewById(R.id.uploadImg);
//        ImageView historyImg = (ImageView) dialog.findViewById(R.id.historyImg);
//        LinearLayout downloadLinear = (LinearLayout) dialog.findViewById(R.id.downloadLinear);
//        LinearLayout shareLinear = (LinearLayout) dialog.findViewById(R.id.shareLinear);
//        LinearLayout headLinear = (LinearLayout) dialog.findViewById(R.id.headLinear);
//        LinearLayout uploadLinear = (LinearLayout) dialog.findViewById(R.id.uploadLinear);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
//            headLinear.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green));
//            shareImg.setImageResource(R.drawable.share_new);
//            downloadImg.setImageResource(R.drawable.download_new);
//            uploadImg.setImageResource(R.drawable.upload);
//            historyImg.setImageResource(R.drawable.history);
//        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
//            headLinear.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
//            shareImg.setImageResource(R.drawable.share_blue);
//            downloadImg.setImageResource(R.drawable.download_blue);
//            uploadImg.setImageResource(R.drawable.upload_blue);
//            historyImg.setImageResource(R.drawable.history_blue);
//        }
//        dialog.show();
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
//        new HomeWorkListDetailActivity.loadPDF().execute();
//        cancelTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        downloadLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // dialog.dismiss();
//                new HomeWorkListDetailActivity.DownloadPDF().execute();
//
//            }
//        });
//        uploadLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // dialog.dismiss();
//                if (CheckPermission())
//                {
//                    ChooseFile();
//                }
//
//            }
//        });
//
//        shareLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (AppUtilityMethods.isNetworkConnected(mContext))
//                {
//                    System.out.println("Working click!!!!!!!!");
//                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                    File fileWithinMyDir = new File(getFilepath(filenameshare));
//                    if(fileWithinMyDir.exists())
//                    {
//                        System.out.println("Working click@@@@@@@@@");
//                        intentShareFile.setType("application/pdf");
//                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
//                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
//                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                    }
//                    else
//                    {
//                        if (AppUtilityMethods.isNetworkConnected(mContext)) {
//
//                            // new WorksheetListActivity.loadPDF().execute();
//                            if(fileWithinMyDir.exists())
//                            {
//                                System.out.println("Working click@@@@@@@@@");
//                                intentShareFile.setType("application/pdf");
//                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
//                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
//                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
//                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
//                            }
//                        } else {
//                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//                        }
//                        System.out.println("Working click!D");
//                    }
//                } else {
//                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//                }
//
//            }
//        });
//
//
//    }

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
            if (Event.equalsIgnoreCase("Down")){
                if (file.exists()) {
                    Toast.makeText(mContext, "File Downloaded to download/"+fileName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
                }
            }else if (Event.equalsIgnoreCase("SHARE")){
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                   // new HomeWorkListDetailActivity.DownloadPDF().execute();
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
            ActivityCompat.requestPermissions(HomeWorkListDetailActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        switch (requestCode) {
//            case FILE_SELECT_CODE:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    Uri uri = data.getData();
//                    Log.d("Chooser", "File Uri: " + uri.toString());
//                    Log.d("Chooser", "File Uri path: " + data.getData().getPath());
//                    // Get the path
//
//                    String path = null;
//                    try {
//                        path = ChooserFileUtils.getPath(this, uri);
//                        filePath = ChooserFileUtils.getPath(this, uri);
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                    Log.d("Chooser", "File Path: " + path);
//                    Log.d("Chooser", "File Path: " + path);
//                        HomeWorkListDetailActivity.UploadFileToServer uploadFileToServer = new HomeWorkListDetailActivity.UploadFileToServer();
//                        uploadFileToServer.execute();
//
//                    //Log.d("Chooser", "File Path: " + path);
//                  //  PathString.setText(path);
//                    // Get the file instance
//                    // File file = new File(path);
//                    // Initiate the upload
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
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
//                    Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
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
                for (int i = 0;i<mediaFiles.size();i++){
                    MediaFile mediaFile = mediaFiles.get(i);
                    System.out.println("Datas: "+mediaFile.getPath());
                    filePath = mediaFile.getPath();
                }

                if(!filePath.equals("")) {
                    file = new File(filePath);
                }else{

                    file=new File("");
                }
                //}

                System.out.println("File path---"+filePath);


                FileBody bin1 = new FileBody(new File(filePath));

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
                            history_list.clear();
                            status_code = jsonObject.optString("statuscode");
                            JSONArray historyArray=jsonObject.getJSONArray("student_hw_list");
                            for (int j=0;j<historyArray.length();j++)
                            {
                                JSONObject hObject = historyArray.getJSONObject(j);
                                HistoryModel historyModel= new HistoryModel();
                                historyModel.setTitle(hObject.optString("title"));
                                historyModel.setFile(hObject.optString("file"));
                                historyModel.setType(hObject.optString("type"));
                                historyModel.setStatus(hObject.optString("status"));
                                history_list.add(historyModel);
                            }

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

                Toast.makeText(mContext, "Your file has been successfully uploaded", Toast.LENGTH_SHORT).show();
                if (history_list.size()>0)
                {

                    historyLinear.setVisibility(View.VISIBLE);
                    HomeWorkHistoryListAdapter mSubjectAdapter=new HomeWorkHistoryListAdapter(mContext,history_list);
                    messageList.setAdapter(mSubjectAdapter);
                    for (int i=0;i<history_list.size();i++)
                    {
                        if (history_list.get(i).getStatus().equalsIgnoreCase("3"))
                        {
                            isUploadVisible=false;
                        }
                    }
                    if (isUploadVisible)
                    {
                        uploadHw.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        uploadHw.setVisibility(View.GONE);
                    }
                }
                else
                {
                    historyLinear.setVisibility(View.GONE);
                    uploadHw.setVisibility(View.VISIBLE);
                }
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
//    private void NewMethod() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        // special intent for Samsung file manager
//        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//        // if you want any file type, you can skip next line
//        sIntent.putExtra("CONTENT_TYPE", "*/*");
//        sIntent.addCategory(Intent.CATEGORY_DEFAULT);
//
//        Intent chooserIntent;
//        if (getPackageManager().resolveActivity(sIntent, 0) != null){
//            // it is device with Samsung file manager
//            chooserIntent = Intent.createChooser(sIntent, "Open file");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
//        } else {
//            chooserIntent = Intent.createChooser(intent, "Open file");
//        }
//
//        try {
//            startActivityForResult(chooserIntent, FILE_SELECT_CODE);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_OK) return;
//        String path     = "";
//        if(requestCode == FILE_SELECT_CODE)
//        {
//            Uri uri = data.getData();
//           // String FilePath = null; // should the path be here in this string
//            try {
//                filePath = getFilePathI(mContext,uri);
//                System.out.print("Path  = " + filePath);
//                //PathString.setText(FilePath);
//                titleName.setText(filePath);
//
//                       HomeWorkListDetailActivity.UploadFileToServer uploadFileToServer = new HomeWorkListDetailActivity.UploadFileToServer();
//                        uploadFileToServer.execute();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            mediaFiles.clear();
            mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
//            fileListAdapter.notifyDataSetChanged();

            String DocumentName = "";
            for (int i = 0;i<mediaFiles.size();i++){
                MediaFile mediaFile = mediaFiles.get(i);
                System.out.println("Datas: "+mediaFile.getPath());
                DocumentName = mediaFile.getName();
            }

            if (mediaFiles.isEmpty()){
                Toast.makeText(mContext, "No file Selected", Toast.LENGTH_SHORT).show();
            }else {

//                final Dialog dialog = new Dialog(mContext);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.dialog_switch_dialog);
//                ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
//                icon.setBackgroundResource(R.drawable.question);
//                icon.setImageResource(R.drawable.roundblue);
//                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//                TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
//                text.setText("Upload "+DocumentName+"?");
//                textHead.setText("Confirm?");
//
//                Button dialogButton = (Button) dialog.findViewById(R.id.btn_signup);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        HomeWorkListDetailActivity.UploadFileToServer uploadFileToServer = new HomeWorkListDetailActivity.UploadFileToServer();
//                        uploadFileToServer.execute();
//                    }
//                });
//                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
//                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();

                HomeWorkListDetailActivity.UploadFileToServer uploadFileToServer = new HomeWorkListDetailActivity.UploadFileToServer();
                uploadFileToServer.execute();

            }





        }
    }

    public static String getFilePathI(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 21 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if(id != null) {
                    if (id.startsWith("raw:")) {
                        return id.substring(4);
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (Exception e) {
                        return null;
                    }
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }



    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}

