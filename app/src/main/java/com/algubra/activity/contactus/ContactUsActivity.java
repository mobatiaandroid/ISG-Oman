package com.algubra.activity.contactus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.contactus.adapter.ContactUsEmailAdapter;
import com.algubra.activity.contactus.adapter.ContactUsPhoneAdapter;
import com.algubra.activity.contactus.model.ContactUsModel;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.loadweburl.LoadWebViewActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 2/5/17.
 */
public class ContactUsActivity extends FragmentActivity implements URLConstants, JsonTagConstants, StausCodes, LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener {
    Context mContext = this;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView backImgView;
    //    ImageView bannerImg, callEleSchool, callMainSchool, callMainSchool2;
//    TextView mainSchool1, mainSchool2, elementSchool, Fax, website;
    TextView website;
    //    TextView principal, secToprinci, manToAdmin, school, addressSchool;
    TextView addressSchool;
    //    LinearLayout mainScLayout, mainLayout2, schoolLayout, princLayout, secToPrinlayout, manageadminLayout;
    String mainschool1;
    String mainschool2;
    String eleschool;
    ImageView showMap;
    String latitude, longitude;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    //    TextView elementarySchoolTitle, faxTitle;
    String web, c_latitude, c_longitude;
    private LocationManager lm;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    Double lat, lng;
    String floor_plan;
    String fp1, fp2;
    ArrayList<String> optString = new ArrayList<>();
    RecyclerView phoneNumberRecyclerList;
    RecyclerView emailRecyclerList;
    ArrayList<ContactUsModel> mContactUsPhoneArrayList;
    ArrayList<ContactUsModel> mContactUsEmailArrayList;
    LinearLayout contactNumberLinear;
    LinearLayout contactEmailLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        initUI();
        resetDisconnectTimer();

        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            getlatlong();

            getContactUsList(URL_CONTACTUS);
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        website = (TextView) findViewById(R.id.website);
        headermanager = new HeaderManager(ContactUsActivity.this, "Contact Us");
      /*if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            headermanager.getHeader(relativeHeader, 0);
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            headermanager.getHeader(relativeHeader, 3);
        }*/

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        /*mapFragment.getMapAsync((OnMapReadyCallback) mContext);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) mContext)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) ContactUsActivity.this)
                .addApi(LocationServices.API)
                .build();*/
//        mainSchool1 = (TextView) findViewById(R.id.mainSchoolValue);
//        mainSchool2 = (TextView) findViewById(R.id.mainSchool2Value);
//        elementSchool = (TextView) findViewById(R.id.elementaryschoolValue);
//        Fax = (TextView) findViewById(R.id.faxValue);
//        principal = (TextView) findViewById(R.id.principalValue);
//        secToprinci = (TextView) findViewById(R.id.secToprinci);
//        manToAdmin = (TextView) findViewById(R.id.manageradminValue);
//        school = (TextView) findViewById(R.id.schoolValue);
        addressSchool = (TextView) findViewById(R.id.address);
//        mainScLayout = (LinearLayout) findViewById(R.id.mainScLayout);
//        mainLayout2 = (LinearLayout) findViewById(R.id.mainLayout2);
//        elementarySchoolTitle = (TextView) findViewById(R.id.elementarySchoolTitle);
//        faxTitle = (TextView) findViewById(R.id.faxTitle);
        // eleLayout= (LinearLayout) findViewById(R.id.eleLayout);
        //faxLayout= (LinearLayout) findViewById(R.id.faxLayout);
//        schoolLayout = (LinearLayout) findViewById(R.id.schoolLayout);
//        princLayout = (LinearLayout) findViewById(R.id.princLayout);
//        secToPrinlayout = (LinearLayout) findViewById(R.id.secToPrinlayout);
//        manageadminLayout = (LinearLayout) findViewById(R.id.manageadminLayout);
//        callEleSchool = (ImageView) findViewById(R.id.callEleSchool);
//        callMainSchool = (ImageView) findViewById(R.id.callMainSchool);
//        callMainSchool2 = (ImageView) findViewById(R.id.callMainSchool2);
        showMap = (ImageView) findViewById(R.id.showMap);
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 0);
//            mainScLayout.setBackgroundResource(R.color.login_button_bg);
//            mainLayout2.setBackgroundResource(R.color.login_button_bg);
//            elementarySchoolTitle.setBackgroundResource(R.color.login_button_bg);
//            faxTitle.setBackgroundResource(R.color.login_button_bg);
//            schoolLayout.setBackgroundResource(R.color.login_button_bg);
//            princLayout.setBackgroundResource(R.color.login_button_bg);
//            secToPrinlayout.setBackgroundResource(R.color.login_button_bg);
//            manageadminLayout.setBackgroundResource(R.color.login_button_bg);


        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            headermanager.getHeader(relativeHeader, 3);
//            mainScLayout.setBackgroundResource(R.color.isg_int_blue);
//            mainLayout2.setBackgroundResource(R.color.isg_int_blue);
//            elementarySchoolTitle.setBackgroundResource(R.color.isg_int_blue);
//            faxTitle.setBackgroundResource(R.color.isg_int_blue);
//            schoolLayout.setBackgroundResource(R.color.isg_int_blue);
//            princLayout.setBackgroundResource(R.color.isg_int_blue);
//            secToPrinlayout.setBackgroundResource(R.color.isg_int_blue);
//            manageadminLayout.setBackgroundResource(R.color.isg_int_blue);
        }

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager llmE = new LinearLayoutManager(mContext);
        llmE.setOrientation(LinearLayoutManager.VERTICAL);
        phoneNumberRecyclerList = (RecyclerView) findViewById(R.id.phoneNumberRecyclerList);
        phoneNumberRecyclerList.setHasFixedSize(true);
        phoneNumberRecyclerList.setLayoutManager(llm);
        emailRecyclerList = (RecyclerView) findViewById(R.id.emailRecyclerList);
        emailRecyclerList.setHasFixedSize(true);
        emailRecyclerList.setLayoutManager(llmE);
        contactNumberLinear = (LinearLayout) findViewById(R.id.contactNumberLinear);
        contactEmailLinear = (LinearLayout) findViewById(R.id.contactEmailLinear);
//        int spacing = 10; // 50px
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
//        messageList.addItemDecoration(itemDecoration);
        backImgView = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        backImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                AppUtilityMethods.hideKeyBoard(mContext);
                finish();
            }
        });

//        callEleSchool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + eleschool));
//                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                mContext.startActivity(intent);
//            }
//        });
//
//        callMainSchool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mainschool1));
//                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                mContext.startActivity(intent);
//            }
//        });
//
//        callMainSchool2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mainschool2));
//                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                mContext.startActivity(intent);
//            }
//        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(ContactUsActivity.this,LocationActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("name",addressSchool.getText().toString());
                startActivity(intent);*/
                if (optString.size() > 0) {
                    stopDisconnectTimer();
                    Intent intent = new Intent(ContactUsActivity.this, FloorPlanActivity.class);
                    intent.putExtra("array", optString);
                    startActivity(intent);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "Floor plan not available.", R.drawable.exclamationicon, R.drawable.roundblue);

                }
            }
        });

//        school.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent emailIntent = new Intent(
//                        Intent.ACTION_SEND_MULTIPLE);
//                String[] deliveryAddress = {school.getText().toString()};
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                emailIntent.setType("text/plain");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(
//                        emailIntent, 0);
//                System.out.println("packge size" + activityList.size());
//                for (final ResolveInfo app : activityList) {
//                    System.out.println("packge name" + app.activityInfo.name);
//                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(
//                                activity.applicationInfo.packageName, activity.name);
//                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        emailIntent.setComponent(name);
//                        v.getContext().startActivity(emailIntent);
//                        break;
//                    }
//                }
//
//
//            }
//        });

//        principal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent emailIntent = new Intent(
//                        Intent.ACTION_SEND_MULTIPLE);
//                String[] deliveryAddress = {principal.getText().toString()};
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                emailIntent.setType("text/plain");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(
//                        emailIntent, 0);
//                System.out.println("packge size" + activityList.size());
//                for (final ResolveInfo app : activityList) {
//                    System.out.println("packge name" + app.activityInfo.name);
//                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(
//                                activity.applicationInfo.packageName, activity.name);
//                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        emailIntent.setComponent(name);
//                        v.getContext().startActivity(emailIntent);
//                        break;
//                    }
//                }
//
//
//            }
//        });

//        secToprinci.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent emailIntent = new Intent(
//                        Intent.ACTION_SEND_MULTIPLE);
//                String[] deliveryAddress = {secToprinci.getText().toString()};
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                emailIntent.setType("text/plain");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(
//                        emailIntent, 0);
//                System.out.println("packge size" + activityList.size());
//                for (final ResolveInfo app : activityList) {
//                    System.out.println("packge name" + app.activityInfo.name);
//                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(
//                                activity.applicationInfo.packageName, activity.name);
//                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        emailIntent.setComponent(name);
//                        v.getContext().startActivity(emailIntent);
//                        break;
//                    }
//                }
//
//
//            }
//        });

//        manToAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent emailIntent = new Intent(
//                        Intent.ACTION_SEND_MULTIPLE);
//                String[] deliveryAddress = {manToAdmin.getText().toString()};
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                emailIntent.setType("text/plain");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(
//                        emailIntent, 0);
//                System.out.println("packge size" + activityList.size());
//                for (final ResolveInfo app : activityList) {
//                    System.out.println("packge name" + app.activityInfo.name);
//                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(
//                                activity.applicationInfo.packageName, activity.name);
//                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        emailIntent.setComponent(name);
//                        v.getContext().startActivity(emailIntent);
//                        break;
//                    }
//                }
//
//            }
//        });

//        school.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent emailIntent = new Intent(
//                        Intent.ACTION_SEND_MULTIPLE);
//                String[] deliveryAddress = {school.getText().toString()};
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                emailIntent.setType("text/plain");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(
//                        emailIntent, 0);
//                System.out.println("packge size" + activityList.size());
//                for (final ResolveInfo app : activityList) {
//                    System.out.println("packge name" + app.activityInfo.name);
//                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(
//                                activity.applicationInfo.packageName, activity.name);
//                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        emailIntent.setComponent(name);
//                        v.getContext().startActivity(emailIntent);
//                        break;
//                    }
//                }
//
//
//            }
//        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                Intent intent = new Intent(mContext, LoadWebViewActivity.class);
                intent.putExtra("url", web);
                intent.putExtra("tab_type", "The Indian School-Al Ghubra");
                mContext.startActivity(intent);
            }
        });
    }

    private void getContactUsList(final String URL_CONTACT_US) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_CONTACT_US);
        String[] name = {"access_token", "boardId"};
        String boardId = "";
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            boardId = "1";
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            boardId = "2";
        } else {
            boardId = "1";
        }
        String[] value = {AppPreferenceManager.getAccessToken(mContext), boardId};

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
                            JSONObject contactObj = secobj.getJSONObject(JTAG_CONTACT);
                            String name = contactObj.optString(JTAG_NAME);
                            String address = contactObj.optString(JTAG_ADDRESS);
                            String city = contactObj.optString(JTAG_CITY);
                            //String email=contactObj.optString(JTAG_EMAIL);
                            JSONObject emailObj = contactObj.getJSONObject(JTAG_EMAIL);
                            String schoolemail = emailObj.optString(JTAG_SCHOOL);
                            String principalemail = emailObj.optString(JTAG_PRINCIPAL);
                            String secratarytopadmin = emailObj.optString(JTAG_SECRETARY);
                            String sectoadmin = emailObj.optString(JTAG_MANAGERADMIN);

                            JSONObject phObject = contactObj.getJSONObject(JTAG_PHONE);
                            mainschool1 = phObject.optString(JTAG_MAINSCHOOL1);
                            mainschool2 = phObject.optString(JTAG_MAINSCHOOL2);
                            eleschool = phObject.optString(JTAG_ELEMENTARYSCHOOL);
                            String fax = contactObj.optString(JTAG_FAX);
                            floor_plan = contactObj.optString(JTAG_FLOORPLAN);
                            JSONArray array = new JSONArray(floor_plan);
                            for (int i = 0; i < array.length(); i++) {
                                fp1 = array.optString(i);
                                optString.add(fp1);
                            }

                            web = contactObj.optString(JTAG_WEBSITE);
                            website.setText("Website : " + web);
                            String pincode = contactObj.optString(JTAG_PINCODE);
                            latitude = contactObj.optString(JTAG_LATITUDE);
                            longitude = contactObj.optString(JTAG_LONGITUDE);
                            addressSchool.setText(Html.fromHtml("<html>" + name + "<br>" + address + "<br>" + city + "<br>" + "Pincode : " + pincode));
//                            mainSchool1.setText(mainschool1);
//                            mainSchool2.setText(mainschool2);
//                            elementSchool.setText(eleschool);
//                            Fax.setText(fax);
//                            school.setText(schoolemail);
//                            secToprinci.setText(secratarytopadmin);
//                            principal.setText(principalemail);
//                            manToAdmin.setText(sectoadmin);
                            JSONArray emailArr = contactObj.optJSONArray("emailArr");
                            JSONArray phoneArr = contactObj.optJSONArray("phoneArr");
                            mContactUsPhoneArrayList = new ArrayList<ContactUsModel>();
                            mContactUsEmailArrayList = new ArrayList<ContactUsModel>();
                            for (int i = 0; i < emailArr.length(); i++) {
                                JSONObject emailJsonObject = emailArr.optJSONObject(i);
                                ContactUsModel emailContactUsModel = new ContactUsModel();
                                emailContactUsModel.setName(emailJsonObject.optString("name"));
                                emailContactUsModel.setEmail(emailJsonObject.optString("email"));
                                mContactUsEmailArrayList.add(emailContactUsModel);
                            }
                            for (int i = 0; i < phoneArr.length(); i++) {
                                JSONObject phoneJsonObject = phoneArr.optJSONObject(i);
                                ContactUsModel phoneContactUsModel = new ContactUsModel();
                                phoneContactUsModel.setName(phoneJsonObject.optString("name"));
                                phoneContactUsModel.setPhone(phoneJsonObject.optString("phone"));
                                mContactUsPhoneArrayList.add(phoneContactUsModel);
                            }
                            ContactUsPhoneAdapter mContactUsPhoneAdapter = new ContactUsPhoneAdapter(mContext, mContactUsPhoneArrayList);

                            contactNumberLinear.setVisibility(View.VISIBLE);
                            phoneNumberRecyclerList.setAdapter(mContactUsPhoneAdapter);
                            ContactUsEmailAdapter mContactUsEmailAdapter = new ContactUsEmailAdapter(mContext, mContactUsEmailArrayList);
                            contactEmailLinear.setVisibility(View.VISIBLE);
                            emailRecyclerList.setAdapter(mContactUsEmailAdapter);
                            mapFragment.getMapAsync((OnMapReadyCallback) mContext);
                            googleApiClient = new GoogleApiClient.Builder(ContactUsActivity.this)
                                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) mContext)
                                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) ContactUsActivity.this)
                                    .addApi(LocationServices.API)
                                    .build();
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                getContactUsList(URL_CONTACT_US);

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

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title(addressSchool.getText().toString()));


        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    if (!isGPSEnabled) {
                        Intent callGPSSettingIntent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    } else {
                        Intent intent = new Intent(mContext, LoadWebViewActivity.class);
                        intent.putExtra("url", "http://maps.google.com/maps?saddr=" + c_latitude + "," + c_longitude + "&daddr=Nord Anglia International School Dubai - Dubai - United Arab Emirates");
                        intent.putExtra("tab_type", "Contact Us");
                        startActivity(intent);
                    }
                    //startActivity(intent);
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }


            }
        });
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    /*protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }*/


    private void getlatlong() {
        android.location.Location location;
        lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
        } else {
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L,
                            0.0F, this);
                    location = lm
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }
                }
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L,
                            0.0F, this);
                    location = lm
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }

                }


            }

        }
        c_latitude = String.valueOf(lat);
        c_longitude = String.valueOf(lng);
        System.out.println("lat---" + c_latitude);
        System.out.println("lat---" + c_longitude);

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
            AppPreferenceManager.setIsGuest(ContactUsActivity.this, false);
            AppPreferenceManager.setUserId(ContactUsActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(ContactUsActivity.this, false);
            AppPreferenceManager.setSchoolSelection(ContactUsActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(ContactUsActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };
*/
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
        getlatlong();
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
            Intent mIntent = new Intent(ContactUsActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
