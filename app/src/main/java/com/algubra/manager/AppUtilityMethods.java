package com.algubra.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.URLConstants;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gayatri on 11/4/17.
 */
public class AppUtilityMethods implements URLConstants,JsonTagConstants{
     static Context mContext;    private static int count = 0;

    //internet checking
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    //valid email checking
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    //date parsing
    public static String dateParsingToDdMmmYyyy(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd MMMM yyyy HH:mm a");
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }
    public static String dateParsingToDdMmmYyyywithoutTime(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd MMMM yyyy");
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }
    public static String dateParsingToDdmmYyyy(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd MMM yyyy");
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }
    public static String dateParsingToDdmmYyyyAmPm(String date) {

        String strCurrentDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        strCurrentDate = format.format(newDate);
        return strCurrentDate;
    }
    //function to encode string into base 64

    public static String base64EncodedString(String inputString) {
        byte[] data;
        String base64 = "";
        try {
            data = inputString.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
            Log.i("Base 64 ", base64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64.replace("+/=", "-_~");
    }

    //decode base 64 to string

    public static String stringFromBase64Encoded(String inputString) {
        byte[] decodeValue = Base64.decode(inputString, Base64.DEFAULT);
        Log.i("Base 64 ", new String(decodeValue).replace("-_~","+/="));
        return new String(decodeValue).replace("-_~","+/=");
    }

    public interface GetTokenSuccess {
        void tokenrenewed();
    }

    public static void getToken(Context context, GetTokenSuccess tokenobj) {
        mContext = context;
        AppUtilityMethods apputils = new AppUtilityMethods();
        Securitycode accesstoken = apputils.new Securitycode(context, tokenobj);
        accesstoken.getToken();
    }

    private class Securitycode {
        private Context mContext;
        private GetTokenSuccess getTokenObj;

        public Securitycode(Context context, GetTokenSuccess getTokenObj) {
            this.mContext = context;
            this.getTokenObj = getTokenObj;
        }

        public void getToken() {


            final VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_HEAD + URL_METHOD_GET_ACCESSTOKEN);
            String name[] = {JTAGNAME_GRANT_TYPE,JTAGNAME_CLIENT_ID,JTAGNAME_CLIENT_SECRET,JTAGNAME_USERNAME,JTAGNAME_PASSWORD};
            String values[] = {JTAGVALUE_GRANT_TYPE,JTAGVALUE_CLIENT_ID,JTAGVALUE_CLIENT_SECRET,JTAGVALUE_USERNAME,JTAGVALUE_PASSWORD};

            volleyWrapper.getResponsePOST(mContext, 11, name, values, new VolleyAPIManager.ResponseListener()

                    {
                        @Override
                        public void responseSuccess(String successResponse) {
                            count++;
                            if (successResponse != null) {
                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);

                                    if (rootObject != null) {
                                       String access_token=rootObject.optString(JTAG_ACCESS_TOKEN);
                                        AppPreferenceManager.setAccessToken(mContext,access_token);
                                    } else {
                                        //CustomStatusDialog(RESPONSE_FAILURE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            System.out.println("Response---" + failureResponse);

                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }
                    }

            );

        }
    }

    public static void showDialogAlertDismiss(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }
    //hide keyboard
    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

       /* if (imm.isAcceptingText()) {

            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);

        }*/
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    /*public static void showDialogAlertDismissFinish(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }
*/

    public static void showDialogAlertLogout(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_switch_dialog);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_signup);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferenceManager.setIsGuest(activity, false);
                AppPreferenceManager.setUserId(activity, "");
                AppPreferenceManager.setIsUserAlreadyLoggedIn(activity, false);
                AppPreferenceManager.setSchoolSelection(activity,"ISG");
                Intent mIntent = new Intent(activity, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(mIntent);
                activity.finish();
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void showAlertFinish(final Activity activity, String message,
                                       String okBtnTitle, String cancelBtnTitle, boolean okBtnVisibility) {
        // custom dialog
        final Dialog dialog = new Dialog(activity, R.style.NewDialog);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image, button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(message);
        int sdk = Build.VERSION.SDK_INT;

        Button dialogCancelButton = (Button) dialog
                .findViewById(R.id.dialogButtonCancel);
        dialogCancelButton.setText(cancelBtnTitle);
        /*if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogCancelButton.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        } else {
            dialogCancelButton.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        }*/
        // if button is clicked, close the custom dialog
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();

            }
        });

        Button dialogOkButton = (Button) dialog
                .findViewById(R.id.dialogButtonOK);
        dialogOkButton.setVisibility(View.GONE);
        dialogOkButton.setText(okBtnTitle);
        if (okBtnVisibility) {
            dialogOkButton.setVisibility(View.VISIBLE);
            /*if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                dialogOkButton.setBackgroundDrawable(AppUtils
                        .getButtonDrawableByScreenCathegory(activity,
                                R.color.split_bg, R.color.list_selector));
            } else {
                dialogOkButton.setBackground(AppUtils
                        .getButtonDrawableByScreenCathegory(activity,
                                R.color.split_bg, R.color.list_selector));
            }*/
            // if button is clicked, close the custom dialog
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        }

        dialog.show();
    }
//convert the input date to 12 January(dd MMMM) form
    public static String separateDate(String inputDate){
        String mDate="";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMM yyyy");
            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    public static String separateTime(String inputDate){
        String mTime="";
        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm:ss");

            mTime = formatterTime.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
    }
    //convert input datetime into 12 January 2017 (dd MMMM yyyy) form
    public static String separateDateTodDmMmMyYyY(String inputDate){
        String mDate="";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }
    public static String replace(String str) {
        return str.replaceAll(" ", "%20");
    }

    public static String getCurrentDateToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String today = dateFormat.format(calendar.getTime());
        return today;
    }

    public static String dateConversionY(String inputDate){
        String mDate="";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");

            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }
    public static String htmlparsing(String text) {
        String encodedString;
        encodedString = text.replaceAll("&lt;", "<");
        encodedString = encodedString.replaceAll("&gt;", ">");
        encodedString = encodedString.replaceAll("&amp;", "");
        encodedString = encodedString.replaceAll("amp;", "");
        return encodedString;
    }

    public static String changeDateFormat(String inputDate){
        String mDate="";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy ");
            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    public static String removeDate(String inputDate){
        String mTime="";
        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm:ss");

            mTime = formatterTime.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
    }
    public static String removeDatetoAMPM(String inputDate){
        String mTime="";
        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");

            mTime = formatterTime.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
    }

    public static String removeDateOnly(String inputDate){
        String mTime="";
        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");

            mTime = formatterTime.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
    }

    public static String removeTime(String inputDate){
        String mTime="";
        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat formatterTime = new SimpleDateFormat("yyyy-MM-dd");

            mTime = formatterTime.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
    }
    public  static long getMiliseconds(String date, String time){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(date+" "+time);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar beginCal = Calendar.getInstance();

            beginCal.set(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), cal1.get(Calendar.HOUR_OF_DAY), cal1.get(Calendar.MINUTE));
            return beginCal.getTimeInMillis();
        }
        catch (Exception e) {
            return new Date().getTime();
        }
    }
    public static String durationInSecondsToString(int sec) {
        int hours = sec / 3600;
        int minutes = (sec / 60) - (hours * 60);
        int seconds = sec - (hours * 3600) - (minutes * 60);
        String formatted = String.format("%d:%02d:%02d", hours, minutes,
                seconds);
        return formatted;
    }
    public static boolean isAppInForeground(Context context)
    {
        System.out.println("Working.....");
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        String className = componentInfo.getClassName();
        System.out.println("className::"+className);
        if (className.equalsIgnoreCase("com.algubra.activity.login.LoginActivity"))
        {
            return false;
        }
        else {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager am = (ActivityManager) context.getSystemService(context.getApplicationContext().ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

                return foregroundTaskPackageName.toLowerCase().equals(context.getPackageName().toLowerCase());
            } else {
                ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
                ActivityManager.getMyMemoryState(appProcessInfo);
                if (appProcessInfo.importance == appProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == appProcessInfo.IMPORTANCE_VISIBLE) {
                    return true;
                }

                KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                // App is foreground, but screen is locked, so show notification
                return km.inKeyguardRestrictedInputMode();
            }
        }
    }

    public static String dateConversionyyyyMMddToDDMMYYYY(String inputDate){
        String mDate="";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }
    public static String dateConversionStandardFormat(String inputDate){
        String mDate="";

        try {
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd-MM-yyyy");

            mDate = formatterFullDate.format(time);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }
    //force update

    public static void showDialogAlertUpdate(final Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_update_version);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        dialog.show();

    }
    public static String getVersionInfo(Context mContext) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
//    TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
//    textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }
}
