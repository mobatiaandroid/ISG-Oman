package com.algubra.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gayatri on 11/4/17.
 */
public class AppPreferenceManager {

    public static void setAccessToken(Context mContext, String accesstoken) {
        SharedPreferences prefs = mContext.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", accesstoken);
        editor.commit();
    }

    public static String getAccessToken(Context mContext) {
        String tokenValue = "";
        SharedPreferences prefs = mContext.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        tokenValue = prefs.getString("access_token", "0");
        return tokenValue;
    }
    public static void setHomePos(Context mContext, String accesstoken) {
        SharedPreferences prefs = mContext.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("HomePos", accesstoken);
        editor.commit();
    }

    public static String getHomePos(Context mContext) {
        String tokenValue = "";
        SharedPreferences prefs = mContext.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        tokenValue = prefs.getString("HomePos", "0");
        return tokenValue;
    }
    public static boolean getIsGuest(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        return prefs.getBoolean("is_guest", true);

    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Vandana Surendranath
     *****************************************************/
    public static void setIsGuest(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_guest", result);
        editor.commit();

    }

    public static String getSchoolSelection(Context context) {
        String tokenValue = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        tokenValue = prefs.getString("selection", "ISG");
        return tokenValue;

    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Vandana Surendranath
     *****************************************************/
    public static void setSchoolSelection(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("selection", result);
        editor.commit();

    }

  public static String getMobileNo(Context context) {
        String mobile = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
      mobile = prefs.getString("mobile", "");
        return mobile;

    }

    public static void setMobileNo(Context context, String mobile) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mobile", mobile);
        editor.commit();

    }
    public static boolean getIsUserAlreadyLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        return prefs.getBoolean("is_loggedin", false);

    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Vandana Surendranath
     *****************************************************/
    public static void setIsUserAlreadyLoggedIn(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_loggedin", result);
        editor.commit();

    }

    public static String getUserId(Context context) {
        String userId = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        userId = prefs.getString("userId", "");
        return userId;

    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", userId);
        editor.commit();

    }

    public static String getStudentsResponseFromLoginAPI(Context context) {
        String student_list = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        student_list = prefs.getString("student_list", "");
        return student_list;

    }

    public static void setStudentsResponseFromLoginAPI(Context context, String student_list) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("student_list", student_list);
        editor.commit();

    }

    public static String getStudentId(Context context) {
        String studentId = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        studentId = prefs.getString("studentId", "");
        return studentId;

    }

    public static void setStudentId(Context context, String studentId) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("studentId", studentId);
        editor.commit();

    }

    public static String getStudentName(Context context) {
        String studentName = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        studentName = prefs.getString("studentName", "");
        return studentName;

    }

    public static void setStudentName(Context context, String studentName) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("studentName", studentName);
        editor.commit();

    }
    public static String getStudentClassName(Context context) {
        String studentClassName = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        studentClassName = prefs.getString("studentClassName", "");
        return studentClassName;

    }

    public static void setStudentClassName(Context context, String studentClassName) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("studentClassName", studentClassName);
        editor.commit();

    }
    public static String getStudentSectionName(Context context) {
        String studentSectionName = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        studentSectionName = prefs.getString("studentSectionName", "");
        return studentSectionName;

    }

    public static void setStudentSectionName(Context context, String studentSectionName) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("studentSectionName", studentSectionName);
        editor.commit();

    }

    public static String getPhno(Context context) {
        String Phno = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        Phno = prefs.getString("Phno", "");
        return Phno;

    }

    public static void setPhno(Context context, String Phno) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Phno", Phno);
        editor.commit();

    }
    public static String getBadgecount(Context context) {
        String badgecount = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        badgecount = prefs.getString("badgecount", "0");
        return badgecount;

    }

    public static void setBadgecount(Context context, String badgecount) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("badgecount", badgecount);
        editor.commit();

    }

    public static String getUserRespFromLoginAPI(Context context) {
        String user_resp = "";
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        user_resp = prefs.getString("user_resp", "");
        return user_resp;

    }

    public static void setUserRespFromLoginAPI(Context context, String user_resp) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_resp", user_resp);
        editor.commit();

    }
    /*********** Force Update **********/
    public static void setVersionFromApi(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("android_version", result);
        editor.commit();
    }

    public static String getVersionFromApi(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("ALGUBRA",
                Context.MODE_PRIVATE);
        return prefs.getString("android_version", "");
    }

}