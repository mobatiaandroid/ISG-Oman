package com.algubra.activity.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
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

import com.algubra.BuildConfig;
import com.algubra.R;
import com.algubra.activity.contactus.ContactUsActivity;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.settings.adapter.SettingsAdapter;
import com.algubra.activity.userprofile.UserProfileActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;
import com.algubra.recyclerviewmanager.RecyclerItemListener;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 24/4/17.
 */
public class SettingsActivity extends Activity implements JsonTagConstants, URLConstants, StausCodes {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    TextView app_version;
    Context mContext = this;
    private SharedPreferences locationPermissionStatus;
    EditText text_currentpswd, newpassword, confirmpassword;
    String[] permissionsRequiredLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE};
    private static final int PERMISSION_CALLBACK_CONSTANT_LOCATION = 3;
    private static final int REQUEST_PERMISSION_LOCATION = 103;
    String versionName;

    private boolean locationToSettings = false;
    ArrayList<String> mSettingsListArray = new ArrayList<String>() {
        {
            add("Change App Settings");
            add("Terms of Use");
            add("Tutorial");
            add("Logout");
        }
    };
    ArrayList<String> mSettingsListArrayRegistered = new ArrayList<String>() {
        {
            add("Change App Settings");
            add("Terms of Use");
            add("User Profile");
            add("Contact Us");
            add("Change Password");
            add("Logout");
        }
    };
    Dialog dialog;
    Dialog dialog1;

    RecyclerView settingItemList;
    SettingsAdapter settingsAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);
        initUI();
        versionName = BuildConfig.VERSION_NAME;
        app_version.setText("Version"+" "+versionName);
        resetDisconnectTimer();

    }

    @SuppressLint("WrongConstant")
    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        dialog = new Dialog(mContext, R.style.NewDialog);
        settingItemList = (RecyclerView) findViewById(R.id.settingItemList);
        app_version = findViewById(R.id.app_version);
        settingItemList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        settingItemList.setLayoutManager(llm);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        settingItemList.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(SettingsActivity.this, getString(R.string.settings));
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
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            settingsAdapter = new SettingsAdapter(mContext, mSettingsListArray);
        } else {
            settingsAdapter = new SettingsAdapter(mContext, mSettingsListArrayRegistered);
        }
        settingItemList.setAdapter(settingsAdapter);

        settingItemList.addOnItemTouchListener(new RecyclerItemListener(mContext, settingItemList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (position == 5) {
                            AppUtilityMethods.showDialogAlertLogout(SettingsActivity.this, "Confirm?", "Do you want to Logout?", R.drawable.question, R.drawable.roundblue);
                        }
                        if (position == 4) {
                            showChangePasswordAlert();
                        }
                        if(position==2)
                        {
                            Intent intent=new Intent(SettingsActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                        }
                        if (position==3)
                        {
                            locationPermissionStatus = mContext.getSharedPreferences("locationPermissionStatus", mContext.MODE_PRIVATE);


                            if (Build.VERSION.SDK_INT < 23) {
                                //Do not need to check the permission
                                Intent intent = new Intent(mContext, ContactUsActivity.class);
                                HomeActivity.stopDisconnectTimer();
                                mContext.startActivity(intent);
                            } else {

                                if (ActivityCompat.checkSelfPermission(mContext, permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
                                        || ActivityCompat.checkSelfPermission(mContext, permissionsRequiredLocation[1]) != PackageManager.PERMISSION_GRANTED
                                        || ActivityCompat.checkSelfPermission(mContext, permissionsRequiredLocation[2]) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, permissionsRequiredLocation[0])
                                            || ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, permissionsRequiredLocation[1])
                                            || ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, permissionsRequiredLocation[2])) {
                                        //Show Information about why you need the permission
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("Need Location and Phone Permission");
                                        builder.setMessage("This module needs location and phone permissions.");

                                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                ActivityCompat.requestPermissions(SettingsActivity.this, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                            }
                                        });
                                        builder.show();
                                    } else if (locationPermissionStatus.getBoolean(permissionsRequiredLocation[0], false)) {
                                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                                        // Redirect to Settings after showing Information about why you need the permission
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("Need Location and Phone Permission");
                                        builder.setMessage("This module needs location and phone permissions.");
                                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                locationToSettings = true;

                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                                intent.setData(uri);
                                                SettingsActivity.this.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
                                                Toast.makeText(mContext, "Go to settings and grant access to location and phone.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                locationToSettings = false;

                                            }
                                        });
                                        builder.show();
                                    } else if (locationPermissionStatus.getBoolean(permissionsRequiredLocation[1], false)) {
                                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                                        // Redirect to Settings after showing Information about why you need the permission
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle("Need Location and Phone Permission");
                                        builder.setMessage("This module needs location and phone permissions.");
                                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                locationToSettings = true;

                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                                intent.setData(uri);
                                                SettingsActivity.this.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
                                                Toast.makeText(mContext, "Go to settings and grant access to location and phone.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                locationToSettings = false;

                                            }
                                        });
                                        builder.show();
                                    }else if (locationPermissionStatus.getBoolean(permissionsRequiredLocation[2], false)) {
                                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                                        // Redirect to Settings after showing Information about why you need the permission
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                                        builder.setTitle("Need Location and Phone Permission");
                                        builder.setMessage("This module needs location and phone permissions.");
                                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                locationToSettings = true;

                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", SettingsActivity.this.getPackageName(), null);
                                                intent.setData(uri);
                                                SettingsActivity.this.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
                                                Toast.makeText(mContext, "Go to settings and grant access to location and phone.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                locationToSettings = false;

                                            }
                                        });
                                        builder.show();
                                    }  else {

                                        //just request the permission
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                        SettingsActivity.this.requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

                                    }
                                    SharedPreferences.Editor editor = locationPermissionStatus.edit();
                                    editor.putBoolean(permissionsRequiredLocation[0], true);
                                    editor.commit();
                                }
                                else {
                                   Intent intent = new Intent(mContext, ContactUsActivity.class);
                                   // HomeActivity.stopDisconnectTimer();
                                    mContext.startActivity(intent);
                                }
                            }
                        }

                  /*      if (position == 2) {

                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto","testaccount@algubra.com", null));
                            startActivity(Intent.createChooser(emailIntent, "Send Email:"));
                        }*/
                        if (position == 0) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                        if (position == 1) {

                            stopDisconnectTimer();
                            Intent intent = new Intent(mContext, TermsOfUseActivity.class);
                            intent.putExtra("page_type", "terms");
                            intent.putExtra("tab_type", "Terms of Use");
                            mContext.startActivity(intent);
                        }

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }

    private void showChangePasswordAlert() {
        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_changepassword);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        text_currentpswd = (EditText) dialog.findViewById(R.id.text_currentpassword);
        newpassword = (EditText) dialog.findViewById(R.id.text_currentnewpassword);
        confirmpassword = (EditText) dialog.findViewById(R.id.text_confirmpassword);

        Button dialogSubmitButton = (Button) dialog
                .findViewById(R.id.btn_changepassword);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                    //  AppUtilityMethods.hideKeyBoard(mContext);
                                                      if (text_currentpswd.getText().toString().trim().length() == 0) {
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_current_password), R.drawable.infoicon, R.drawable.roundblue);

                                                      } else if (newpassword.getText().toString().trim().length() == 0) {
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_new_password), R.drawable.infoicon, R.drawable.roundblue);

                                                          //newpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (confirmpassword.getText().toString().trim().length() == 0) {
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_confirm_password), R.drawable.infoicon, R.drawable.roundblue);

                                                          //confirmpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (!newpassword.getText().toString().trim().equals(confirmpassword.getText().toString().trim())) {
                                                          //confirmpassword.setError(mContext.getResources().getString(R.string.password_mismatch));
                                                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.password_mismatch), R.drawable.infoicon, R.drawable.roundblue);

                                                      } else {
                                                          if (AppUtilityMethods.isNetworkConnected(mContext)) {
                                                              callChangePasswordAPI(URL_CHANGEPASSWORD);
                                                          } else {
                                                              AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                          }
                                                      }
                                                  }
                                              }

        );

        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener()

                                        {
                                            @Override
                                            public void onClick(View v) {
                                                AppUtilityMethods.hideKeyBoard(mContext);

                                                dialog.dismiss();
                                            }
                                        }

        );
        dialog.show();
    }

    private void callChangePasswordAPI(String URL) {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL);
        String[] name = {"access_token", "parentId", "current_passwd", "new_passwd"};
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getUserId(mContext),
                text_currentpswd.getText().toString().trim(),
                newpassword.getText().toString().trim()};

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
                            /*JSONArray responseArray = secobj.getJSONArray(JTAG_RESPONSE_ARRAY);
                            for (int i = 0; i < responseArray.length(); i++) {
								JSONObject respObj = responseArray.getJSONObject(i);
								PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USERS_ID));
							}*/
//dialog.dismiss();

                            Log.d("TAG", "Inside response success---");
                            showDialogSignUpAlert((Activity) mContext, "Success", "Password successfully changed. Login again to continue.", R.drawable.tick, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_PASSWORD_MISMATCH)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Incorrect current password", R.drawable.infoicon, R.drawable.roundblue);

                        } else if (status_code.equalsIgnoreCase(STATUSCODE_INVALIDUSER)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_user), R.drawable.infoicon, R.drawable.roundblue);

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
                        callChangePasswordAPI(URL_CHANGEPASSWORD);

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

    public void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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
                AppPreferenceManager.setIsGuest(SettingsActivity.this, false);
                AppPreferenceManager.setUserId(mContext, "");
                AppPreferenceManager.setSchoolSelection(mContext, "ISG");

                dialog1.dismiss();
                Intent mIntent = new Intent(activity, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(mIntent);
                finish();
            }
        });

        dialog1.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mContext = this;

        stopDisconnectTimer();

    }

   /* public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(mContext, false);
            AppPreferenceManager.setUserId(mContext, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(mContext, false);
            AppPreferenceManager.setSchoolSelection(mContext, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(mContext, LoginActivity.class);
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
         mContext = this;
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mContext = this;
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(SettingsActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
