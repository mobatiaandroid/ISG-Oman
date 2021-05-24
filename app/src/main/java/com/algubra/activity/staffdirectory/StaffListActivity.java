package com.algubra.activity.staffdirectory;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.staffdirectory.adapter.StaffListAdapter;
import com.algubra.activity.staffdirectory.model.StaffModel;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.DividerItemDecoration;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by gayatri on 27/4/17.
 */
public class StaffListActivity extends Activity implements JsonTagConstants,URLConstants,StausCodes{
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    private ArrayList<StaffModel> mStaffModelArrayList=new ArrayList<>();
LinearLayout searchLinear;
    RecyclerView staffDirectoryList;
    Bundle extras;
    String title;
    EditText searchEditText;
    ImageView btnImgsearch;
    ArrayList<StaffModel> filtered;
    StaffListAdapter staffListAdapter;
    private static final int REQUEST_PERMISSION_LOCATION = 103;
    static String[] permissionsRequiredPhone = new String[]{Manifest.permission.CALL_PHONE};
    private static SharedPreferences phonePermissionStatus;
    private boolean phoneToSettings = false;
    private static final int PERMISSION_CALLBACK_CONSTANT_PHONE = 3;
    static String phoneNumberNonTeaching = "";
    static Context mContext;
    static Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafflist);
        mContext = this;
        mActivity = this;
        initUI();
        resetDisconnectTimer();

    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            mStaffModelArrayList = (ArrayList<StaffModel>) extras
                    .getSerializable("array");
            title=extras.getString("title");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        searchLinear= (LinearLayout) findViewById(R.id.searchLinear);
        searchEditText= (EditText) findViewById(R.id.searchEditText);
        btnImgsearch= (ImageView) findViewById(R.id.btnImgsearch);

        headermanager = new HeaderManager(StaffListActivity.this, title);
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            searchLinear.setBackgroundResource(R.color.login_button_bg);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            searchLinear.setBackgroundResource(R.color.isg_int_blue);

        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        staffDirectoryList= (RecyclerView) findViewById(R.id.settingItemList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                finish();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        staffDirectoryList.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line)));
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        staffDirectoryList.addItemDecoration(itemDecoration);
        staffDirectoryList.setLayoutManager(llm);
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) staffDirectoryList.getLayoutParams();
        marginLayoutParams.setMargins(10, 0, 10, 0);
        staffDirectoryList.setLayoutParams(marginLayoutParams);
        if(mStaffModelArrayList.size()>0) {
             staffListAdapter = new StaffListAdapter(mContext, mStaffModelArrayList, title);
            staffDirectoryList.setAdapter(staffListAdapter);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon,  R.drawable.roundblue);

        }
        btnImgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtilityMethods.hideKeyBoard(mContext);
//                if (!(searchEditText.getText().toString().equalsIgnoreCase(""))) {
//                    staffListAdapter = new StaffListAdapter(mContext, filtered, title);
//                    staffDirectoryList.setAdapter(staffListAdapter);
//                }
//                else
//                {
//                    staffListAdapter = new StaffListAdapter(mContext, mStaffModelArrayList, title);
//                    staffDirectoryList.setAdapter(staffListAdapter);
//                }

            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // TODO Auto-generated method stub
                if (mStaffModelArrayList.size()>0) {
                    filtered = new ArrayList<StaffModel>();
                    for (int i = 0; i < mStaffModelArrayList.size(); i++) {
                        if (mStaffModelArrayList.get(i).getStaff_subject()
                                .toLowerCase().contains(s.toString().toLowerCase()) || mStaffModelArrayList.get(i).getStaff_name()
                                .toLowerCase().contains(s.toString().toLowerCase()) ||mStaffModelArrayList.get(i).getStaff_class()
                                .toLowerCase().contains(s.toString().toLowerCase())) {
                            filtered.add(mStaffModelArrayList.get(i));
                        }
                    }
                    staffListAdapter = new StaffListAdapter(mContext, filtered, title);
                    staffDirectoryList.setAdapter(staffListAdapter);
                    if (searchEditText.getText().toString()
                            .equalsIgnoreCase("")) {
                        staffListAdapter = new StaffListAdapter(mContext,
                                mStaffModelArrayList, title);
                        staffDirectoryList.setAdapter(staffListAdapter);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
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
            AppPreferenceManager.setIsGuest(StaffListActivity.this, false);
            AppPreferenceManager.setUserId(StaffListActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(StaffListActivity.this, false);
            AppPreferenceManager.setSchoolSelection(StaffListActivity.this,"ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(StaffListActivity.this, LoginActivity.class);
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CALLBACK_CONSTANT_PHONE) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(phoneNumberNonTeaching);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Phone Permission");
                builder.setMessage("This needs phone permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        phoneToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredPhone, PERMISSION_CALLBACK_CONSTANT_PHONE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phoneToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                phoneToSettings = true;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT_PHONE);
                Toast.makeText(mContext, "Go to settings and grant access to phone.", Toast.LENGTH_LONG).show();

            }
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(phoneNumberNonTeaching);
            }/*else  if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                //DENIAL
                requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

            }*/
        }
    }

    public void proceedAfterPermission(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
    public static void proceedAfterPermissions(String phoneNumber) {
        phoneNumberNonTeaching=phoneNumber;
        phonePermissionStatus = mContext.getSharedPreferences("phonePermissionStatus", mContext.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumberNonTeaching));
            mContext.startActivity(intent);
        } else {

            if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredPhone[0]) != PackageManager.PERMISSION_GRANTED )
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredPhone[0])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("Need Phone Permission");
                    builder.setMessage("This module needs phone permissions.");

                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(mActivity, permissionsRequiredPhone, PERMISSION_CALLBACK_CONSTANT_PHONE);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    builder.show();

                } else {

                    //just request the permission
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                    mActivity.requestPermissions(permissionsRequiredPhone, PERMISSION_CALLBACK_CONSTANT_PHONE);

                }
                SharedPreferences.Editor editor = phonePermissionStatus.edit();
                editor.putBoolean(permissionsRequiredPhone[0], true);
                editor.commit();
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumberNonTeaching));
                mContext.startActivity(intent);
            }
        }
    }
  @Override protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(StaffListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
