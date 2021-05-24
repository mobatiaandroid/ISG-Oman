package com.algubra.activity.userprofile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.appcontroller.AppController;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AndroidMultiPartEntity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.FileCache;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.CustomProgressBar;
import com.algubra.volleymanager.VolleyAPIManager;
import com.squareup.picasso.Picasso;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gayatri on 17/5/17.
 */
public class EditProfileActivity extends Activity implements JsonTagConstants,StausCodes,URLConstants{
    private Context mContext=this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,takephoto;
    EditText edtName,edtAddress,editTextPhoneNo,editTextEmail,editTextPostcode,editTextCity,editTextCountry;
    Button updateButton;
    FileCache CameraDirectory;
    private File      mFileTemp;
    private static final int PICK_FROM_CAMERA = 101;
    private static final int CROP_FROM_CAMERA = 202;
    private static final int PICK_FROM_GALLERY = 102;
    private Uri mImageCaptureUri;
    private Uri mImageCaptureUriFile;
    public String CACHE_CAMERA_PATH =getResources().getString(R.string.app_name)+"/AlghubraImage/";
    //    public String CACHE_CAMERA_PATH ="AlGubra/ADTCimage/";
    private AlertDialog alertDialog;
    String filePath="",filePath1="";
    Date d;
    private String responsecode="",status_code="";
    File file;
    CharSequence s1;
    String img_url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        initUI();
        resetDisconnectTimer();
        setValues();
        DownloadImgFromUrl downloadImgFromUrl=new DownloadImgFromUrl();
        downloadImgFromUrl.execute();
    }

    private void setValues() {

        JSONArray staffArray = null;
        try {
            staffArray = new JSONArray(AppPreferenceManager.getUserRespFromLoginAPI(mContext));
            if (staffArray.length() > 0) {

                JSONObject sObject = staffArray.getJSONObject(0);
                //set values

                edtName.setText(sObject.optString(JTAG_NAME));
                edtAddress.setText(sObject.optString(JTAG_ADDRESS));
                editTextEmail.setText(sObject.optString(JTAG_EMAIL));
                editTextPostcode.setText(sObject.optString(JTAG_PINCODE));
               // phno.setText(sObject.optString(JTAG_MOBILE));
                //AppPreferenceManager.setPhno(mContext,phno.getText().toString());
                editTextCity.setText(sObject.optString(JTAG_CITY));
                editTextCountry.setText(sObject.optString(JTAG_COUNTRYID));
                if(!sObject.optString(JTAG_PHOTO).equals("")) {
                    img_url=sObject.optString(JTAG_PHOTO).replaceAll("","%20");
                    Picasso.with(mContext).load(sObject.optString(JTAG_PHOTO)).fit()
                            .into(takephoto);
                }
                //Glide.with(mContext).load(sObject.optString(JTAG_PHOTO)).placeholder(R.drawable.user_profile).dontAnimate().into(takephoto);

                               /* Glide.with(mContext).load(sObject.optString(JTAG_PHOTO))
                                        .placeholder(R.drawable.noimage)
                                        .into(Img);*/
                                /*for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject sObject = staffArray.getJSONObject(i);
                                   //set values
                                    Glide.with(mContext).load(sObject.optString(JTAG_PHOTO))
                                            .placeholder(R.drawable.noimage)
                                            .into(Img);
                                    userName.setText(sObject.optString(JTAG_NAME));
                                    addressName.setText(sObject.optString(JTAG_ADDRESS));
                                    email.setText(sObject.optString(JTAG_EMAIL));
                                    postcode.setText(sObject.optString(JTAG_PINCODE));
                                    phno.setText(sObject.optString(JTAG_MOBILE));
                                    city.setText(sObject.optString(JTAG_CITY));
                                    country.setText(sObject.optString(JTAG_COUNTRYID));
                                    //s.setText(sObject.optString(JTAG_STATE));



                                }*/

            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.no_datafound), R.drawable.infoicon,  R.drawable.roundblue);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("The response is" + staffArray);
        AppPreferenceManager.setUserRespFromLoginAPI(mContext,staffArray.toString());

    }

    private void initUI() {

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        CameraDirectory = new FileCache(mContext, CACHE_CAMERA_PATH, true);
        updateButton= (Button) findViewById(R.id.updateButton);

        headermanager = new HeaderManager(EditProfileActivity.this, "Edit Profile");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            updateButton.setBackgroundResource(R.drawable.rounded_corner_loginbtn_bg);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            updateButton.setBackgroundResource(R.drawable.rounded_button_bg_blue);

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

        edtName= (EditText) findViewById(R.id.editTextName);
        edtAddress= (EditText) findViewById(R.id.editTextAddress);
        editTextPhoneNo= (EditText) findViewById(R.id.editTextPhoneNo);
        editTextPhoneNo.setText(AppPreferenceManager.getPhno(mContext));
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPostcode= (EditText) findViewById(R.id.editTextPostcode);
        editTextCity= (EditText) findViewById(R.id.editTextCity);
        editTextCountry= (EditText) findViewById(R.id.editTextCountry);
        takephoto= (ImageView) findViewById(R.id.Img);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraGalleryChoice();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtName.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.enter_name), R.drawable.exclamationicon,  R.drawable.roundblue);

                }else if(edtAddress.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "Enter address", R.drawable.exclamationicon,  R.drawable.roundblue);

                }/*else if(editTextPhoneNo.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.enter_phoneno), R.drawable.exclamationicon,  R.drawable.roundblue);

                }*/else if(editTextEmail.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.enter_email), R.drawable.exclamationicon,  R.drawable.roundblue);

                }else if(!AppUtilityMethods.isValidEmail(editTextEmail.getText().toString())){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.invalid_email), R.drawable.exclamationicon,  R.drawable.roundblue);

                }
                else if(editTextPostcode.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "Enter pincode", R.drawable.exclamationicon,  R.drawable.roundblue);

                }else if(editTextCity.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "Enter city", R.drawable.exclamationicon,  R.drawable.roundblue);

                }else  if(editTextCountry.getText().toString().equals("")){
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "Enter country", R.drawable.exclamationicon,  R.drawable.roundblue);

                }else{
                    if(AppUtilityMethods.isNetworkConnected(mContext)) {
                        try {
                           // if(filePath.equals(nul))
                           if(!filePath.equals("")) {
                                UploadFileToServer uploadFileToServer = new UploadFileToServer();
                                uploadFileToServer.execute();
                           }else{
                              //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "No Changes Detected", R.drawable.exclamationicon,  R.drawable.roundblue);
                               UploadFileToServerWithoutImage uploadFileToServerWithoutImage = new UploadFileToServerWithoutImage();
                               uploadFileToServerWithoutImage.execute();
                           }

                        }catch (Exception e){
                            e.printStackTrace();

                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "No Changes Detected", R.drawable.exclamationicon,  R.drawable.roundblue);

                        }
                    }else{
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
            }
        });
    }

    private void getList(String urlGetTimetableList) {
        VolleyAPIManager volleyWrapper=new VolleyAPIManager(urlGetTimetableList);
        String[] name={"access_token","parentId"};

        String[] value={AppPreferenceManager.getAccessToken(mContext),AppPreferenceManager.getUserId(mContext)
        };

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

                            //showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.succ_pswd), R.drawable.tick,  R.drawable.roundblue);




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
                        getList(URL_GETMYPROFILE);

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

    /*public  void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        Log.d("TAG", "Inside response success---showDialogSignUpAlert");

        dialog1 = new Dialog(activity);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.alert_dialog_ok_layout);
        ImageView icon = (ImageView) dialog1.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog1.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog1.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);
        dialog.dismiss();
        Button dialogButton = (Button) dialog1.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, false);
                AppPreferenceManager.setIsGuest(mContext, false);
                AppPreferenceManager.setUserId(mContext, "");
                AppPreferenceManager.setSchoolSelection(mContext, "ISG");
                dialog1.dismiss();
                Intent mIntent = new Intent(activity, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(mIntent);
                finish();
            }
        });

        dialog1.show();

    }*/

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


                HttpPost httppost = new HttpPost(URL_EDIT_PROFILE + "?" + JTAG_ACCESS_TOKEN + "=" +AppPreferenceManager.getAccessToken(mContext));

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

                entity.addPart("photo", bin1);

               // }

                Charset chars = Charset.forName("UTF-8");
                entity.addPart(JTAG_NAME, new StringBody(edtName.getText().toString()));
                entity.addPart(JTAG_ADDRESS, new StringBody(edtAddress.getText().toString().trim()));
                entity.addPart("parentId", new StringBody(AppPreferenceManager.getUserId(mContext)));
                entity.addPart(JTAG_CITY, new StringBody(editTextCity.getText().toString().trim()));
                entity.addPart("country", new StringBody(editTextCountry.getText().toString()));
                entity.addPart("pincode", new StringBody(editTextPostcode.getText().toString().trim()));
                entity.addPart("city", new StringBody(editTextCity.getText().toString()));
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
            if (status_code.equals("201")) {
                showDialogSignUpAlert((Activity) mContext, getString(R.string.success), getString(R.string.succ_prof), R.drawable.tick,  R.drawable.roundblue);
                //JSONObject objresponse = obj.optJSONObject("response");
                //String errorCode = objresponse.optString("statuscode");
                //CustomStatusDialog(errorCode);
            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "No Changes Detected", R.drawable.exclamationicon,  R.drawable.roundblue);

                // CustomStatusDialog(responsecode);
            }
        }

    }



    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void showCameraGalleryChoice() {
        final AlertDialog.Builder getImageFrom = new AlertDialog.Builder(mContext);
        getImageFrom.setTitle("Select");
        final CharSequence[] opsChars = {"Take Picture",
                "Gallery"};
        getImageFrom.setItems(opsChars, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
                    myDir.mkdirs();
                    File file = new File(myDir, "tmp_avatar_"
                            + String.valueOf(System.currentTimeMillis()) + ".JPEG");
                    mImageCaptureUri = Uri.fromFile(file);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    try {
                        cameraIntent.putExtra("return-data", true);
                        startActivityForResult(cameraIntent, 0);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (which == 1) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(
                                intent,
                               "Select"), 1);
                    } else {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(
                                intent,
                                "Select"), 1);
                    }
                }
            }
        });
        alertDialog = getImageFrom.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 700) {
            if (resultCode == 701) {
                finish();
            }
        } else {
            Bitmap bitmap = null;
            Uri imageUri = null;
            if (resultCode != Activity.RESULT_OK)
                return;
            switch (requestCode) {
                case 0:
                    imageUri = mImageCaptureUri;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mContext.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        bitmap = BitmapFactory.decodeFile(picturePath);
                        imageUri = Uri.parse(picturePath);
                        cursor.close();
                    }
                    break;
            }
            if (bitmap != null) {
              d = new Date();
                s1 = DateFormat.format("MM-dd-yy-hh-mm-ss",
                        d.getTime());
                try {
                    File tempFile = new File(imageUri.getPath());
                    long size = tempFile.length();
                    ByteArrayOutputStream byteArrayOutputStream;
                    Log.e("Size image:", "" + size);
                    int minSize = 600;
                    int widthOfImage = bitmap.getWidth();
                    int heightOfImage = bitmap.getHeight();
                    Log.e("Width", "" + widthOfImage);
                    Log.e("Height", "" + heightOfImage);
                    int newHeight = 600;
                    int newWidht = 600;
                    if (widthOfImage < minSize && heightOfImage < minSize) {
                        newWidht = widthOfImage;
                        newHeight = heightOfImage;
                    } else {
                        if (widthOfImage >= heightOfImage) {
                            //int newheght = heightOfImage/600;
                            float ratio = (float) minSize / widthOfImage;
                            Log.e("Multi width greater", "" + minSize + "/" + widthOfImage + "=" + ratio);
                            newHeight = (int) (heightOfImage * ratio);
                            newWidht = minSize;
                        } else if (heightOfImage > widthOfImage) {
                            float ratio = (float) minSize / heightOfImage;
                            newWidht = (int) (widthOfImage * ratio);
                            newHeight = minSize;
                        }

                    }
                    if (size > 1024 * 1024) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    }


                    if (size > (4 * 1024 * 1024)) {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } else {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int bounding = dpToPx(mContext.getResources().getDisplayMetrics().density);
                        float xScale = (10 * (float) bounding) / width;
                        float yScale = (100 * (float) bounding) / height;
                        float scale = (xScale <= yScale) ? xScale : yScale;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scale, scale);
                        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
                        takephoto.setImageDrawable(result);
                        //takephoto.setScaleType(ImageView.ScaleType.FIT_XY);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                         filePath = "/sdcard/file" + String.valueOf(s1) + ".jpg";
                        System.out.println("File path---"+filePath);
                        File f = new File(filePath);
                        try {
                            f.createNewFile();
                            FileOutputStream fos = null;

                            fos = new FileOutputStream(f);
                            fos.write(byteArray);
                            fos.close();
                            filePath = f.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }
        }
    }
    private int dpToPx(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public  void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
                /*Intent intent = new Intent(mContext, UserProfileActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

        dialog.show();

    }
private String getImageFromUrl(){
    try
    {
        URL url = new URL(img_url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
        String filename="downloadedFile.png";
        Log.i("Local filename:",""+filename);
        File file = new File(SDCardRoot,filename);
        if(file.createNewFile())
        {
            file.createNewFile();
        }
        FileOutputStream fileOutput = new FileOutputStream(file);
        InputStream inputStream = urlConnection.getInputStream();
        int totalSize = urlConnection.getContentLength();
        int downloadedSize = 0;
        byte[] buffer = new byte[1024];
        int bufferLength = 0;
        while ( (bufferLength = inputStream.read(buffer)) > 0 )
        {
            fileOutput.write(buffer, 0, bufferLength);
            downloadedSize += bufferLength;
            Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
        }
        fileOutput.close();
        if(downloadedSize==totalSize) filePath1=file.getPath();
    }
    catch (MalformedURLException e)
    {
        e.printStackTrace();
    }
    catch (IOException e)
    {
        filePath1=null;
        e.printStackTrace();
    }
    Log.i("filepath:"," "+filePath1) ;
    return filePath1;

}

    private class DownloadImgFromUrl extends AsyncTask<Void, Void, Void> {
        final CustomProgressBar pDialog = new CustomProgressBar(mContext,
                R.drawable.spinner);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            getImageFromUrl();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    private class UploadFileToServerWithoutImage extends AsyncTask<Void, Integer, String> {
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


                HttpPost httppost = new HttpPost(URL_EDIT_PROFILE + "?" + JTAG_ACCESS_TOKEN + "=" +AppPreferenceManager.getAccessToken(mContext));

                //for (int i = 0; i < mHashMap.size(); i++) {
                //String path = String.valueOf(mImageCaptureUriFile);
                // if()




                AndroidMultiPartEntity entity;
                entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            }
                        });


                Charset chars = Charset.forName("UTF-8");
                entity.addPart(JTAG_NAME, new StringBody(edtName.getText().toString()));
                entity.addPart(JTAG_ADDRESS, new StringBody(edtAddress.getText().toString().trim()));
                entity.addPart("parentId", new StringBody(AppPreferenceManager.getUserId(mContext)));
                entity.addPart(JTAG_CITY, new StringBody(editTextCity.getText().toString().trim()));
                entity.addPart("country", new StringBody(editTextCountry.getText().toString()));
                entity.addPart("pincode", new StringBody(editTextPostcode.getText().toString().trim()));
                entity.addPart("city", new StringBody(editTextCity.getText().toString()));
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
            if (status_code.equals("201")) {
                showDialogSignUpAlert((Activity) mContext, getString(R.string.success), getString(R.string.succ_prof), R.drawable.tick,  R.drawable.roundblue);
                //JSONObject objresponse = obj.optJSONObject("response");
                //String errorCode = objresponse.optString("statuscode");
                //CustomStatusDialog(errorCode);
            } else {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "No Changes Detected", R.drawable.exclamationicon,  R.drawable.roundblue);

                // CustomStatusDialog(responsecode);
            }
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
            AppPreferenceManager.setIsGuest(EditProfileActivity.this, false);
            AppPreferenceManager.setUserId(EditProfileActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(EditProfileActivity.this, false);
            AppPreferenceManager.setSchoolSelection(EditProfileActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
