package com.algubra.activity.homework;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.homework.adapter.HomeWorkHistoryListAdapter;
import com.algubra.activity.homework.model.HistoryModel;
import com.algubra.activity.homework.model.HomeWorkListModel;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFTest;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
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

public class HomeWorkListDetailActivityNew extends Activity implements URLConstants, JsonTagConstants, StausCodes {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    Activity mActivity;
    ArrayList<HomeWorkListModel> subjectList;
    String subjectId = "";
    Bundle extras;
    String typeExtention = "";
    String url = "";
    String name = "";
    String correctedfile = "";
    String filenameshare = "";
    String filenameshareHistory = "";
    String filePath = "";
    String titleUpload = "";
    String staff_id = "";
    String homework_id = "";
    String staffname = "";
    String description = "";
    String due_date = "";
    String status = "";
    String set_date = "";
    LinearLayout historyLinear, uploadHw, clickLinear, mainViewHistoryLinear, clickHistoryLinear, corrected_filelinear,correctedclickHistoryLinear;
    TextView staff_name, titleName, fileNameTxt, headHistory, descriptionTxt, setDateTxt, dueDateTxt, statusTxt, descriptionHistory, correctedfileNameHistoryTxt;
    ImageView downloadImg, shareImg, staff_img, fileLogo, uploadImg, downloadLinear, shareLinear, indicationImg, correctedfileLogoHistory;
    ImageView fileLogoHistory, downloadHistoryLinear, shareHistoryLinear, indicationHistoryImg,correcteddownload,correctedshare;
    TextView fileNameHistoryTxt, statusHistoryTxt;
    ArrayList<HistoryModel> history_list;
    File file;
    boolean isUploadVisible = true;
    private String responsecode = "", status_code = "";
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
        setContentView(R.layout.activity_homework_other_type_new);
        mActivity = this;
        initUI();
        resetDisconnectTimer();

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

    @SuppressLint("WrongConstant")
    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getString("subjectId");
            correctedfile = extras.getString("corrected_file");
            typeExtention = extras.getString("typeExtention");
            name = extras.getString("name");
            filenameshare = extras.getString("filenameshare");
            titleUpload = extras.getString("titleUpload");
            staff_id = extras.getString("staff_id");
            homework_id = extras.getString("homework_id");
            staffname = extras.getString("staff_name");
            url = extras.getString("url");
            description = extras.getString("description");
            due_date = extras.getString("due_date");
            status = extras.getString("status");
            set_date = extras.getString("set_date");
            history_list = (ArrayList<HistoryModel>) getIntent().getSerializableExtra("history_list");




        }
        subjectList = new ArrayList<>();
//        if (history_list.size() > 0) {
//            String fileShareData = history_list.get(0).getTitle().replace(" ", "_");
//            filenameshareHistory = fileShareData + "." + history_list.get(0).getType();
//        }
        if(history_list.size()>0)
        {
            String fileShareData = history_list.get(0).getTitle().replace(" ", "_");
            filenameshareHistory = fileShareData + "."+history_list.get(0).getType();
            correctedfile=history_list.get(0).getCorrected_file();
            System.out.println("corrected_file"+correctedfile);
        }
        // filenameshareHistory=history_list.get(0).getTitle()+"."+history_list.get(0).getType();
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        downloadLinear = (ImageView) findViewById(R.id.downloadLinear);
        shareLinear = (ImageView) findViewById(R.id.shareLinear);
        historyLinear = (LinearLayout) findViewById(R.id.historyLinear);
        staff_name = (TextView) findViewById(R.id.staff_name);
        headHistory = (TextView) findViewById(R.id.headHistory);
        titleName = (TextView) findViewById(R.id.titleName);
        fileNameTxt = (TextView) findViewById(R.id.fileNameTxt);
        descriptionTxt = (TextView) findViewById(R.id.descriptionTxt);
        setDateTxt = (TextView) findViewById(R.id.setDateTxt);
        dueDateTxt = (TextView) findViewById(R.id.dueDateTxt);
        statusTxt = (TextView) findViewById(R.id.statusTxt);
        statusHistoryTxt = (TextView) findViewById(R.id.statusHistoryTxt);
        fileNameHistoryTxt = (TextView) findViewById(R.id.fileNameHistoryTxt);
        descriptionHistory = (TextView) findViewById(R.id.descriptionHistory);
        indicationImg = (ImageView) findViewById(R.id.indicationImg);
        downloadImg = (ImageView) findViewById(R.id.downloadImg);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        staff_img = (ImageView) findViewById(R.id.staff_img);
        uploadHw = (LinearLayout) findViewById(R.id.uploadHw);
        clickLinear = (LinearLayout) findViewById(R.id.clickLinear);
        mainViewHistoryLinear = (LinearLayout) findViewById(R.id.mainViewHistoryLinear);
        clickHistoryLinear = (LinearLayout) findViewById(R.id.clickHistoryLinear);
        corrected_filelinear = findViewById(R.id.corrected_filelinear);
        correctedclickHistoryLinear = findViewById(R.id.correctedclickHistoryLinear);
        correctedfileNameHistoryTxt = findViewById(R.id.correctedfileNameHistoryTxt);
        correctedfileLogoHistory = findViewById(R.id.correctedfileLogoHistory);
        fileLogo = (ImageView) findViewById(R.id.fileLogo);
        uploadImg = (ImageView) findViewById(R.id.uploadImg);
        fileLogoHistory = (ImageView) findViewById(R.id.fileLogoHistory);
        downloadHistoryLinear = (ImageView) findViewById(R.id.downloadHistoryLinear);
        indicationHistoryImg = (ImageView) findViewById(R.id.indicationHistoryImg);
        correcteddownload = findViewById(R.id.correcteddownload);
        correctedshare = findViewById(R.id.correctedshare);
        shareHistoryLinear = (ImageView) findViewById(R.id.shareHistoryLinear);
        headermanager = new HeaderManager(HomeWorkListDetailActivityNew.this, "Homework");
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
        setDateTxt.setText(set_date);
        if (description.equalsIgnoreCase("")) {
            descriptionTxt.setVisibility(View.GONE);
        } else {
            descriptionTxt.setVisibility(View.VISIBLE);
            descriptionTxt.setText(description);
        }

        dueDateTxt.setText("Due Date - " + due_date);
        if (status.equalsIgnoreCase("4") && history_list.size() == 0) {
            System.out.println("working indication if");
            indicationImg.setBackgroundColor(getResources().getColor(R.color.red));
            statusTxt.setText("Assigned");
        } else {
            System.out.println("working indication else");
            indicationImg.setBackgroundColor(getResources().getColor(R.color.grey));
            statusTxt.setText("");
        }


        clickLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeExtention.equalsIgnoreCase("pdf")) {
                    Intent browserIntent = new Intent(HomeWorkListDetailActivityNew.this, PDFTest.class);
                    browserIntent.putExtra("pdf_url", url); // url
                    System.out.println("url print" + url);
                    browserIntent.putExtra("title", name);  // name
                    browserIntent.putExtra("filename", name); //name
                    startActivity(browserIntent);
                } else {
                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                    intent.putExtra("pdf_url", url);
                    intent.putExtra("title", name);
                    intent.putExtra("type", typeExtention);
                    startActivity(intent);
                }
            }
        });
        correctedclickHistoryLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (correctedfile.endsWith(".pdf")) {
                    Intent browserIntent = new Intent(HomeWorkListDetailActivityNew.this, PDFTest.class);
                    browserIntent.putExtra("pdf_url", history_list.get(0).getCorrected_file()); // url
                    System.out.println("url print" + url);
                    browserIntent.putExtra("title", "Corrected File");  // name
                    browserIntent.putExtra("filename", history_list.get(0).getTitle()); //name
                    startActivity(browserIntent);
                } else {
                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                    intent.putExtra("pdf_url", history_list.get(0).getCorrected_file());
                    intent.putExtra("title", "Corrected File");
                    intent.putExtra("type", history_list.get(0).getType());
                    startActivity(intent);
                }

            }
        });
        clickHistoryLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (history_list.get(0).getType().equalsIgnoreCase("pdf")) {
                    Intent browserIntent = new Intent(HomeWorkListDetailActivityNew.this, PDFTest.class);
                    browserIntent.putExtra("pdf_url", history_list.get(0).getFile()); // url
                    System.out.println("url print" + url);
                    browserIntent.putExtra("title", history_list.get(0).getTitle());  // name
                    browserIntent.putExtra("filename", history_list.get(0).getTitle()); //name
                    startActivity(browserIntent);
                } else {
                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                    intent.putExtra("pdf_url", history_list.get(0).getFile());
                    intent.putExtra("title", history_list.get(0).getTitle());
                    intent.putExtra("type", history_list.get(0).getType());
                    startActivity(intent);
                }
            }
        });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (staffname.equalsIgnoreCase("")) {
            staff_name.setVisibility(View.GONE);
        } else {
            staff_name.setVisibility(View.VISIBLE);
            staff_name.setText(staffname);
        }
        if (name.equalsIgnoreCase("")) {
            titleName.setVisibility(View.GONE);
        } else {
            titleName.setVisibility(View.VISIBLE);
            titleName.setText(name);
        }
        fileNameTxt.setText(titleUpload);
        if (typeExtention.equalsIgnoreCase("ppt") || typeExtention.equalsIgnoreCase("pptx")) {
            fileLogo.setImageResource(R.drawable.powerpoint);
        } else if (typeExtention.equalsIgnoreCase("xlxs") || typeExtention.equalsIgnoreCase("xlx")) {
            fileLogo.setImageResource(R.drawable.excel);
        } else if (typeExtention.equalsIgnoreCase("jpg") || typeExtention.equalsIgnoreCase("png") || typeExtention.equalsIgnoreCase("jpeg") || typeExtention.equalsIgnoreCase("JPG") || typeExtention.equalsIgnoreCase("JPEG") || typeExtention.equalsIgnoreCase("PNG")) {
            fileLogo.setImageResource(R.drawable.image);
        } else if (typeExtention.equalsIgnoreCase("doc") || typeExtention.equalsIgnoreCase("docx")) {
            fileLogo.setImageResource(R.drawable.word);
        } else if (typeExtention.equalsIgnoreCase("pdf")) {
            fileLogo.setImageResource(R.drawable.pdfimg);
        }
        if (history_list.size() > 0) {

            if (!(correctedfile.equalsIgnoreCase("")) && !(correctedfile.equals("null"))) {

                corrected_filelinear.setVisibility(View.VISIBLE);
                correctedfileNameHistoryTxt.setText(history_list.get(0).getTitle());

                if (correctedfile.endsWith("ppt")) {
                    correctedfileLogoHistory.setImageResource(R.drawable.powerpoint);
                } else if (correctedfile.endsWith("xlxs")) {
                    correctedfileLogoHistory.setImageResource(R.drawable.excel);
                } else if (correctedfile.endsWith("jpg")) {
                    correctedfileLogoHistory.setImageResource(R.drawable.image);

                } else if (correctedfile.endsWith("doc")) {
                    correctedfileLogoHistory.setImageResource(R.drawable.word);

                } else if (correctedfile.endsWith("pdf")) {
                    correctedfileLogoHistory.setImageResource(R.drawable.pdfimg);

                }
            } else {
                corrected_filelinear.setVisibility(View.GONE);

            }
            mainViewHistoryLinear.setVisibility(View.VISIBLE);
            headHistory.setVisibility(View.VISIBLE);
            if (history_list.get(0).getReason().equalsIgnoreCase("") || history_list.get(0).getReason().equalsIgnoreCase("null")) {
                descriptionHistory.setVisibility(View.GONE);
            } else {
                descriptionHistory.setVisibility(View.VISIBLE);
                descriptionHistory.setText(history_list.get(0).getReason());
            }
            if (history_list.get(0).getType().equalsIgnoreCase("ppt") || history_list.get(0).getType().equalsIgnoreCase("pptx")) {
                fileLogoHistory.setImageResource(R.drawable.powerpoint);
            } else if (history_list.get(0).getType().equalsIgnoreCase("xlxs") || history_list.get(0).getType().equalsIgnoreCase("xlx")) {
                fileLogoHistory.setImageResource(R.drawable.excel);
            } else if (history_list.get(0).getType().equalsIgnoreCase("jpg") || history_list.get(0).getType().equalsIgnoreCase("png") || history_list.get(0).getType().equalsIgnoreCase("jpeg") || history_list.get(0).getType().equalsIgnoreCase("JPG") || history_list.get(0).getType().equalsIgnoreCase("JPEG") || history_list.get(0).getType().equalsIgnoreCase("PNG")) {
                fileLogoHistory.setImageResource(R.drawable.image);
            } else if (history_list.get(0).getType().equalsIgnoreCase("doc") || history_list.get(0).getType().equalsIgnoreCase("docx")) {
                fileLogoHistory.setImageResource(R.drawable.word);
            } else if (history_list.get(0).getType().equalsIgnoreCase("pdf")) {
                fileLogoHistory.setImageResource(R.drawable.pdfimg);
            }
            fileNameHistoryTxt.setText(history_list.get(0).getTitle());
            System.out.println("history title" + history_list.get(0).getTitle());
            if (history_list.get(0).getStatus().equalsIgnoreCase("1")) {
                statusHistoryTxt.setText("Submitted");
                indicationHistoryImg.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (history_list.get(0).getStatus().equalsIgnoreCase("2")) {
                statusHistoryTxt.setText("Redo");
                indicationHistoryImg.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (history_list.get(0).getStatus().equalsIgnoreCase("3")) {
                statusHistoryTxt.setText("Approved");
                indicationHistoryImg.setBackgroundColor(getResources().getColor(R.color.green));
            }
            for (int i = 0; i < history_list.size(); i++) {
                if (history_list.get(i).getStatus().equalsIgnoreCase("3")) {
                    isUploadVisible = false;
                }
            }
            if (isUploadVisible) {
                uploadHw.setVisibility(View.VISIBLE);
                uploadImg.setVisibility(View.VISIBLE);
            } else {
                uploadHw.setVisibility(View.GONE);
                uploadImg.setVisibility(View.GONE);
            }
        } else {
            mainViewHistoryLinear.setVisibility(View.GONE);
            descriptionHistory.setVisibility(View.GONE);
            headHistory.setVisibility(View.GONE);
            uploadHw.setVisibility(View.VISIBLE);
            uploadImg.setVisibility(View.VISIBLE);
        }


        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            //green
            staff_img.setImageResource(R.drawable.teacher);
            uploadHw.setBackgroundColor(getResources().getColor(R.color.login_button_bg));
            headHistory.setBackgroundColor(getResources().getColor(R.color.login_button_bg));

        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            //blue
            staff_img.setImageResource(R.drawable.teacher_blue);
            uploadHw.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));
            headHistory.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));
        }

        downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    Event = "Down";
                    new HomeWorkListDetailActivityNew.DownloadPDF().execute();
                }

            }
        });
        downloadHistoryLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    Event = "DownHistory";
                    new HomeWorkListDetailActivityNew.DownloadPDFHistory().execute();
                }

            }
        });
        correcteddownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    Event = "CorrectedDownload";
                    new HomeWorkListDetailActivityNew.DownloadPDFCorrected().execute();
                }
            }
        });
        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    Event = "SHARE";
                    new HomeWorkListDetailActivityNew.DownloadPDF().execute();
                }


            }
        });
        shareHistoryLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    Event = "SHAREHISTORY";
                    new HomeWorkListDetailActivityNew.DownloadPDFHistory().execute();
                }


            }
        });
        correctedshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
                    Event = "CorrectedShare";
                    new HomeWorkListDetailActivityNew.DownloadPDFCorrected().execute();
                }
            }
        });
        uploadHw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission()) {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        //System.out.println("TestRestart:hshhs");
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(HomeWorkListDetailActivityNew.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }


    public class DownloadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;
        String filename = name.replace(" ", "_");
        String fileName = filename + "." + typeExtention;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            System.out.println("filename" + fileName);
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
            if (Event.equalsIgnoreCase("Down")) {
                if (file.exists()) {
                    Toast.makeText(mContext, "File Downloaded to download/" + fileName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
                }
            } else if (Event.equalsIgnoreCase("SHARE")) {
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    // new HomeWorkListDetailActivity.DownloadPDF().execute();
                    System.out.println("Working click!!!!!!!!");
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(getFilepath(filenameshare));
                    if (fileWithinMyDir.exists()) {
                        System.out.println("Working click@@@@@@@@@");
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath(filenameshare)));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    } else {
                        if (AppUtilityMethods.isNetworkConnected(mContext)) {

                            // new WorksheetListActivity.loadPDF().execute();
                            if (fileWithinMyDir.exists()) {
                                System.out.println("Working click@@@@@@@@@");
                                intentShareFile.setType("application/pdf");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath(filenameshare)));
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

    public class DownloadPDFHistory extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;
        String filenamenew = history_list.get(0).getTitle().replace(" ", "_");
        String fileNamenew = filenamenew + "." + history_list.get(0).getType();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            System.out.println("filename" + fileNamenew);
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
                File outputFile = new File(file, fileNamenew);
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

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileNamenew);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileNamenew);
            System.out.println("file.exists() = " + file.exists());
            if (Event.equalsIgnoreCase("DownHistory")) {
                if (file.exists()) {
                    Toast.makeText(mContext, "File Downloaded to download/" + fileNamenew, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
                }
            } else if (Event.equalsIgnoreCase("SHAREHISTORY")) {

                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    //   new HomeWorkListDetailActivityNew.loadPDFHistory().execute();
                    System.out.println("Working click!!!!!!!!");
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(getFilepath(fileNamenew));
                    System.out.println("Working click@@@@@@@@@    " + fileNamenew);
                    if (fileWithinMyDir.exists()) {
                        System.out.println("Working click@@@@@@@@@");
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath(fileNamenew)));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    } else {
                        System.out.println("Working clickfsfdsfdsfds");
                        if (AppUtilityMethods.isNetworkConnected(mContext)) {


                            if (fileWithinMyDir.exists()) {
                                System.out.println("Working click@@@@@@@@@");
                                intentShareFile.setType("application/pdf");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath(fileNamenew)));
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

    public class DownloadPDFCorrected  extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;
        String fileNamenew = history_list.get(0).getTitle().replace(" ", "_")+"_corrected";
        String filenamenewcorrected = fileNamenew + "." + history_list.get(0).getType();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            System.out.println("filename" + filenamenewcorrected);
            try {

                u = new URL(correctedfile);
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
                File outputFile = new File(file, filenamenewcorrected);
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

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + filenamenewcorrected);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + filenamenewcorrected);
            System.out.println("file.exists() = " + file.exists());
            if (Event.equalsIgnoreCase("CorrectedDownload")) {
                if (file.exists()) {
                    Toast.makeText(mContext, "File Downloaded to download/" + filenamenewcorrected, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
                }
            } else if (Event.equalsIgnoreCase("CorrectedShare")) {

                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    //   new HomeWorkListDetailActivityNew.loadPDFHistory().execute();
                    System.out.println("Working click!!!!!!!!");
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(getFilepath(filenamenewcorrected));
                    System.out.println("Working click@@@@@@@@@    " + filenamenewcorrected);
                    if (fileWithinMyDir.exists()) {
                        System.out.println("Working click@@@@@@@@@");
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath(filenamenewcorrected)));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    } else {
                        System.out.println("Working clickfsfdsfdsfds");
                        if (AppUtilityMethods.isNetworkConnected(mContext)) {


                            if (fileWithinMyDir.exists()) {
                                System.out.println("Working click@@@@@@@@@");
                                intentShareFile.setType("application/pdf");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath(filenamenewcorrected)));
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

    public class loadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(PDFViewActivity.this);
//            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
//            dialog.show();
        }

        protected Void doInBackground(String... urls) {
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


            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
        }

    }

    public class loadPDFHistory extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(PDFViewActivity.this);
//            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
//            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {
                String fileName = filenameshareHistory;
                u = new URL(history_list.get(0).getFile());
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


            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
        }

    }

    private String getFilepath(String filename) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + filename).getPath();

    }

    private boolean CheckPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(mContext, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(HomeWorkListDetailActivityNew.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                        }

                    }
                }
                return;
            }
        }
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
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
                for (int i = 0; i < mediaFiles.size(); i++) {
                    MediaFile mediaFile = mediaFiles.get(i);
                    System.out.println("Datas: " + mediaFile.getPath());
                    filePath = mediaFile.getPath();
                }

                if (!filePath.equals("")) {
                    file = new File(filePath);
                } else {

                    file = new File("");
                }
                //}

                System.out.println("File path---" + filePath);


                FileBody bin1 = new FileBody(new File(filePath));

                AndroidMultiPartEntity entity;
                entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            }
                        });

                //for (int i = 0; i < mFileBody.size(); i++) {
                System.out.println("File path---" + bin1);

                entity.addPart("uploaded", bin1);

                // }
                String boardId = "";
                if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
                    boardId = "1";
                } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
                    boardId = "2";
                } else {
                    boardId = "1";
                }
                Charset chars = Charset.forName("UTF-8");
                entity.addPart("boardId", new StringBody(boardId));
                entity.addPart("studentId", new StringBody(AppPreferenceManager.getStudentId(mContext)));
                entity.addPart("parentId", new StringBody(AppPreferenceManager.getUserId(mContext)));
                entity.addPart("subjectId", new StringBody(subjectId));
                entity.addPart("title", new StringBody("uploaded" + titleUpload));
                entity.addPart("staffId", new StringBody(staff_id));
                entity.addPart("homeworkId", new StringBody(homework_id));
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Stat code---" + statusCode);
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                    System.out.println("Stat code---" + responseString);
                    try {
                        obj = new JSONObject(responseString);
                        responsecode = obj.optString("responsecode");
                        JSONObject jsonObject = obj.getJSONObject("response");
                        if (responsecode.equals("200")) {
                            history_list.clear();
                            status_code = jsonObject.optString("statuscode");
                            JSONArray historyArray = jsonObject.getJSONArray("student_hw_list");
                            for (int j = 0; j < historyArray.length(); j++) {
                                JSONObject hObject = historyArray.getJSONObject(j);
                                HistoryModel historyModel = new HistoryModel();
                                historyModel.setTitle(hObject.optString("title"));
                                historyModel.setFile(hObject.optString("file"));
                                historyModel.setType(hObject.optString("type"));
                                historyModel.setStatus(hObject.optString("status"));
                                historyModel.setReason(hObject.optString("reason"));
                                historyModel.setCorrected_file(hObject.optString("corrected_file"));
                                history_list.add(historyModel);
                            }

                        } else {
                            // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon,  R.drawable.roundblue);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

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
            if (status_code.equals("201")) {

                Toast.makeText(mContext, "Your file has been successfully uploaded", Toast.LENGTH_SHORT).show();
                if (history_list.size() > 0) {
                    mainViewHistoryLinear.setVisibility(View.VISIBLE);
                    headHistory.setVisibility(View.VISIBLE);
                    if (history_list.get(0).getReason().equalsIgnoreCase("") || history_list.get(0).getReason().equalsIgnoreCase("null")) {
                        descriptionHistory.setVisibility(View.GONE);
                    } else {
                        descriptionHistory.setVisibility(View.VISIBLE);
                        descriptionHistory.setText(history_list.get(0).getReason());
                    }
                    if (history_list.get(0).getType().equalsIgnoreCase("ppt") || history_list.get(0).getType().equalsIgnoreCase("pptx")) {
                        fileLogoHistory.setImageResource(R.drawable.powerpoint);
                    } else if (history_list.get(0).getType().equalsIgnoreCase("xlxs") || history_list.get(0).getType().equalsIgnoreCase("xlx")) {
                        fileLogoHistory.setImageResource(R.drawable.excel);
                    } else if (history_list.get(0).getType().equalsIgnoreCase("jpg") || history_list.get(0).getType().equalsIgnoreCase("png") || history_list.get(0).getType().equalsIgnoreCase("jpeg") || history_list.get(0).getType().equalsIgnoreCase("JPG") || history_list.get(0).getType().equalsIgnoreCase("JPEG") || history_list.get(0).getType().equalsIgnoreCase("PNG")) {
                        fileLogoHistory.setImageResource(R.drawable.image);
                    } else if (history_list.get(0).getType().equalsIgnoreCase("doc") || history_list.get(0).getType().equalsIgnoreCase("docx")) {
                        fileLogoHistory.setImageResource(R.drawable.word);
                    } else if (history_list.get(0).getType().equalsIgnoreCase("pdf")) {
                        fileLogoHistory.setImageResource(R.drawable.pdfimg);
                    }
                    fileNameHistoryTxt.setText(history_list.get(0).getTitle());
                    if (history_list.get(0).getStatus().equalsIgnoreCase("1")) {
                        statusHistoryTxt.setText("Submitted");
                    } else if (history_list.get(0).getStatus().equalsIgnoreCase("2")) {
                        statusHistoryTxt.setText("Redo");
                    } else if (history_list.get(0).getStatus().equalsIgnoreCase("3")) {
                        statusHistoryTxt.setText("Approved");
                    }
                    for (int i = 0; i < history_list.size(); i++) {
                        if (history_list.get(i).getStatus().equalsIgnoreCase("3")) {
                            isUploadVisible = false;
                        }
                    }
                    if (isUploadVisible) {
                        uploadHw.setVisibility(View.VISIBLE);
                        uploadImg.setVisibility(View.VISIBLE);
                    } else {
                        uploadHw.setVisibility(View.GONE);
                        uploadImg.setVisibility(View.GONE);
                    }
                } else {
                    mainViewHistoryLinear.setVisibility(View.GONE);
                    headHistory.setVisibility(View.GONE);
                    uploadHw.setVisibility(View.VISIBLE);
                    uploadImg.setVisibility(View.VISIBLE);
                }

            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "No Changes Detected", R.drawable.exclamationicon, R.drawable.roundblue);

            }
        }

    }


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
            for (int i = 0; i < mediaFiles.size(); i++) {
                MediaFile mediaFile = mediaFiles.get(i);
                System.out.println("Datas: " + mediaFile.getPath());
                DocumentName = mediaFile.getName();
            }

            if (mediaFiles.isEmpty()) {
                Toast.makeText(mContext, "No file Selected", Toast.LENGTH_SHORT).show();
            } else {
                HomeWorkListDetailActivityNew.UploadFileToServer uploadFileToServer = new HomeWorkListDetailActivityNew.UploadFileToServer();
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
                if (id != null) {
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

