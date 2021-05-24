package com.algubra.activity.home;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.adapter.HomeViewPagerAdapter;
import com.algubra.activity.home.model.HomeTabModel;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.settings.SettingsActivity;
import com.algubra.appcontroller.AppController;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.volleymanager.VolleyAPIManager;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by gayatri on 18/4/17.
 */
public class HomeActivity extends AppCompatActivity implements JsonTagConstants, URLConstants, StausCodes {
    Context mContext = this;
    static Activity mActivity ;
    public static Context mContexts;
//    ViewPager viewPager;
    int pagerCount=0;
//    HomeViewPagerAdapter homeViewPagerAdapter;
    ImageView imageSettings;
    //HomeViewPagerAdapter_New homeViewPagerAdapter;
    //ArrayList<HomeTabModel> homeTabModelsTabArrangemnt=new ArrayList<>();
    ArrayList<HomeTabModel> homeTabModelsTabTitle = new ArrayList<>();
    LinearLayout selectschoolLinearLayout, topLinearLayout;//, c;
    PopupWindow popupWindow;
    ListView listSchool;
    private static LayoutInflater inflater = null;
    ImageView switchLanguagearrow, logo;
    RelativeLayout chooseSchoolRelative;
    RelativeLayout llHead;
//    CirclePageIndicator mIndicator;
    TextView txtSwitch;
    String selectedFromList;
    private Timer timer;
    int mWidthPixels = 0, mHeightPixels = 0;
    boolean confirmlogin=false;
Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);
        AppPreferenceManager.setHomePos(mContext, String.valueOf(0));
        mActivity=this;
        try{
        String versionFromPreference = AppPreferenceManager.getVersionFromApi(mContext).replace(".","");
        int versionNumberAsInteger = Integer.parseInt(versionFromPreference);
        String replaceVersion = AppUtilityMethods.getVersionInfo(mContext).replace(".","");
        int replaceCurrentVersion=Integer.parseInt(replaceVersion);
        if (!(AppPreferenceManager.getVersionFromApi(mContext).equalsIgnoreCase(""))) {
            if(versionNumberAsInteger >replaceCurrentVersion) {

                AppUtilityMethods.showDialogAlertUpdate(mContext);
            }

        }
        } catch (Exception e) {
            Log.e("Numberformat: ", e.getMessage());
        }
        initUI();

        //System.out.println("JSON---"+loadJSONFromAsset());
    }

    @SuppressLint("WrongConstant")
    private void initUI() {
        mContexts = this;
        mActivity = this;
        AppController.viewPager = (ViewPager) findViewById(R.id.viewPager);
        selectschoolLinearLayout = (LinearLayout) findViewById(R.id.selectschool);
        topLinearLayout = (LinearLayout) findViewById(R.id.toplayout);
        listSchool = (ListView) findViewById(R.id.listSchool);
        switchLanguagearrow = (ImageView) findViewById(R.id.switchLanguagearrow);
        logo = (ImageView) findViewById(R.id.logo);
        chooseSchoolRelative = (RelativeLayout) findViewById(R.id.chooseSchoolRelative);
        txtSwitch = (TextView) findViewById(R.id.txtSwitch);
        if (!AppPreferenceManager.getIsGuest(mContext)) {
            selectschoolLinearLayout.setVisibility(View.GONE);
        } else {
            selectschoolLinearLayout.setVisibility(View.GONE);

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        System.out.println("Icon---" + AppPreferenceManager.getSchoolSelection(mContext));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
//            logo.setImageResource(R.drawable.title_logo);
            // getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbargreennew));
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            View view = getSupportActionBar().getCustomView();
            getSupportActionBar().setElevation(0);
            Toolbar toolbar = (Toolbar) view.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
            imageSettings = (ImageView) view.findViewById(R.id.imageSettings);
            llHead = (RelativeLayout) view.findViewById(R.id.llHead);
            llHead.setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbargreennew));
            imageSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopDisconnectTimer();
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }
            });
            txtSwitch.setText("ISG");
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
//            logo.setImageResource(R.drawable.titlelogoblue);
            //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbarbluenew));
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            View view = getSupportActionBar().getCustomView();
            getSupportActionBar().setElevation(0);
            Toolbar toolbar = (Toolbar) view.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
            imageSettings = (ImageView) view.findViewById(R.id.imageSettings);
            llHead = (RelativeLayout) view.findViewById(R.id.llHead);
            llHead.setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbarbluenew));
            imageSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopDisconnectTimer();
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }
            });
            txtSwitch.setText("ISG-INT");

        }
        setArrayList(loadJSONFromAsset());
        //System.out.println("count::"+homeTabModelsTabArrangemnt.size());
      /*  if (homeTabModelsTabArrangemnt.size() <= 6) {
            pagerCount = 1;

        } else if (homeTabModelsTabArrangemnt.size() <= 12) {
            pagerCount = 2;

        } else*/
//
        pagerCount = 2;      //was 3
        AppController.mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        AppController.mIndicator.setPageColor(Color.parseColor("#b2b2b2"));
        AppController.mIndicator.setFillColor(Color.parseColor("#71ACD6"));
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
//            callBannerImageAPI();
            getBadgeAPI();
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
//        homeViewPagerAdapter=new HomeViewPagerAdapter(mContext,pagerCount,viewPager,getScreenSizeInInches());
//        //homeViewPagerAdapter=new HomeViewPagerAdapter_New(mContext,pagerCount,viewPager);
//
//        viewPager.setAdapter(homeViewPagerAdapter);
//        mIndicator.setViewPager(viewPager);


        AppController.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                AppPreferenceManager.setHomePos(mContext,String.valueOf(position));

            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                AppPreferenceManager.setHomePos(mContext, String.valueOf(position));
                if (AppPreferenceManager.getIsGuest(mContext)) {
                    if (position == 0) {
//                        selectschoolLinearLayout.setVisibility(View.VISIBLE);//09oct2017 commented
                        selectschoolLinearLayout.setVisibility(View.GONE);
                    } else {
                        selectschoolLinearLayout.setVisibility(View.GONE);

                    }
                } else {
                    selectschoolLinearLayout.setVisibility(View.GONE);

                }
//                if (AppController.viewPager!=null)
//                AppController.mIndicator.setCurrentItem(position);


            }
        });

        selectschoolLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupWindow popUp = popupWindowsort();
                int coordinate = chooseSchoolRelative.getBottom();
                popUp.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, 0, coordinate);
                //showDialog((Activity) mContext, getString(R.string.alert_heading), "Do you want to switch to ISG", R.drawable.infoicon,  R.drawable.roundblue);

            }
        });
        extras=getIntent().getExtras();
        if (extras!=null) {
            confirmlogin=extras.getBoolean("confirmlogin");
        }
        else {
            confirmlogin=false;
        }
        if (confirmlogin)
        {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.first_otp_alert), R.drawable.infoicon, R.drawable.roundblue);

        }
    }

    private void setArrayList(String json) {
        try {

            AppController.mTabTitleModelArrayList = new ArrayList<>();
            AppController.mTabArrangeModelArrayList = new ArrayList<>();
            JSONObject rootObject = new JSONObject(json);

            String responseCode = rootObject.getString(JTAG_RESPONSECODE);

            if (responseCode.equals(RESPONSE_SUCCESS)) {
                JSONObject responseObject = rootObject.optJSONObject("response");

                String statusCode = responseObject.getString(JTAG_STATUSCODE);
                if (statusCode.equalsIgnoreCase("303")) {

                    JSONObject data = responseObject.optJSONObject("data");
                    JSONArray tabArrangeArray = data.optJSONArray("tab_arrange");

                    for (int i = 0; i < tabArrangeArray.length(); i++) {
                        HomeTabModel mTabArrangeModel = new HomeTabModel();
                        mTabArrangeModel.setTab_id(tabArrangeArray.getString(i));

                        //homeTabModelsTabArrangemnt.add(mTabArrangeModel);
                        AppController.mTabArrangeModelArrayList.add(mTabArrangeModel);
                        //AppController.mTabArrangeModelArrayList.add(mTabArrangeModel);
                    }
//                                            PreferenceManager.setTabArrange(mContext, mTabArrangeModelArrayList);
                    JSONArray tabTitleArray = data.optJSONArray("tab_title");

                    for (int i = 0; i < tabTitleArray.length(); i++) {
                        HomeTabModel mTabTitleModel = new HomeTabModel();
                        mTabTitleModel.setTab_title(tabTitleArray.getString(i));
                        //System.out.println("item---2--" + tabTitleArray.getString(i));

                        // homeTabModelsTabTitle.add(mTabTitleModel);
                        AppController.mTabTitleModelArrayList.add(mTabTitleModel);
                    }


                } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                        statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {

                } else {

                }
            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is;
//            if (AppPreferenceManager.getIsGuest(mContext)) {
            if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
                is = mContext.getAssets().open("tabarrangemntsguest.json");

            } else {
                is = mContext.getAssets().open("tabarrangemnts.json");
                selectschoolLinearLayout.setVisibility(View.GONE);
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        //System.out.println("JSON---"+json);
        return json;
    }

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup));
        final View popUpView = getLayoutInflater().inflate(R.layout.activity_main,
                null);
        //popupWindow.setContentView(popUpView);

        CustomAdapter customAdapter = new CustomAdapter(mContext);
        // the drop down list is a list view
        //ListView listViewSort = new ListView(this);
        // set our adapter and pass our pop up window contents
        final ListView listViewSort = (ListView) popUpView.findViewById(R.id.listSchool);
        listViewSort.setAdapter(customAdapter);

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(400);
        //popupWindow.set;
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // set the listview as popup content
        popupWindow.setContentView(popUpView);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();

                if (position == 0) {
                    selectedFromList = "ISG";
                } else if (position == 1) {
                    selectedFromList = "ISG-INT";
                }
                AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                showDialog((Activity) mContext, getString(R.string.alert_heading), "Do you want to switch to " + selectedFromList, R.drawable.infoicon, R.drawable.roundblue, position);

            }
        });

        return popupWindow;
    }

    public class CustomAdapter extends BaseAdapter {
        // String [] result=new String[]{};
        Context context;
        String[] result = {"ISG", "ISG-INT"};
        int[] prgmImages = {R.drawable.logo2, R.drawable.logo1};


        public CustomAdapter(Context context) {
            // TODO Auto-generated constructor stub
            //result=prgmNameList;
            this.context = context;
            // imageId=prgmImages;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {
            TextView tv;
            ImageView img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.custom_adapter_home_school_item, null);
            holder.tv = (TextView) rowView.findViewById(R.id.dep_name);
            holder.img = (ImageView) rowView.findViewById(R.id.imgView);
            holder.tv.setText(result[position]);
            holder.img.setImageResource(prgmImages[position]);

            return rowView;
        }

    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    public void showDialog(final Activity activity, String msgHead, String msg, int ico, int bgIcon, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (position == 0) {
                    logo.setImageResource(R.drawable.title_logo);
                    txtSwitch.setText("ISG");
                    AppPreferenceManager.setSchoolSelection(mContext, "ISG");
                } else if (position == 1) {
                    logo.setImageResource(R.drawable.titlelogoblue);
                    txtSwitch.setText("ISG-INT");
                    AppPreferenceManager.setSchoolSelection(mContext, "ISG-INT");

                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    public void showDialogLogout(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferenceManager.setIsGuest(mContext, true);
                Intent mIntent = new Intent(activity, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(mIntent);
                //dialog.dismiss();

                /*if(position==0){
                    logo.setImageResource(R.drawable.title_logo);
                    txtSwitch.setText("ISG");
                    AppPreferenceManager.setSchoolSelection(mContext,"ISG");
                }else if(position==1){
                    logo.setImageResource(R.drawable.titlelogoblue);
                    txtSwitch.setText("ISG-INT");
                    AppPreferenceManager.setSchoolSelection(mContext, "ISG-INT");

                }*/
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onRestart() {
        super.onRestart();


        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbargreennew));
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            View view = getSupportActionBar().getCustomView();
            getSupportActionBar().setElevation(0);
            Toolbar toolbar = (Toolbar) view.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
            imageSettings = (ImageView) view.findViewById(R.id.imageSettings);
            llHead = (RelativeLayout) view.findViewById(R.id.llHead);
            llHead.setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbargreennew));
            imageSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopDisconnectTimer();
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }
            });
            txtSwitch.setText("ISG");
            if (AppPreferenceManager.getUserId(mContexts).equalsIgnoreCase("")) {
                Intent mIntent = new Intent(mContexts, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContexts.startActivity(mIntent);
                mActivity.finish();
            }
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            View view = getSupportActionBar().getCustomView();
            getSupportActionBar().setElevation(0);
            Toolbar toolbar = (Toolbar) view.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);
            imageSettings = (ImageView) view.findViewById(R.id.imageSettings);
            llHead = (RelativeLayout) view.findViewById(R.id.llHead);
            llHead.setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbarbluenew));
            imageSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopDisconnectTimer();
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                }
            });
            txtSwitch.setText("ISG-INT");
            if (AppPreferenceManager.getUserId(mContexts).equalsIgnoreCase("")) {
                Intent mIntent = new Intent(mContexts, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContexts.startActivity(mIntent);
                mActivity.finish();
            }

        }
    }



    public static final long DISCONNECT_TIMEOUT =300000; // 5 min = 5 * 60 * 1000 ms

    public static Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    public static Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(mContexts, false);
            AppPreferenceManager.setUserId(mContexts, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(mContexts, false);
            AppPreferenceManager.setSchoolSelection(mContexts, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContexts)) {

                Intent mIntent = new Intent(mContexts, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContexts.startActivity(mIntent);
                mActivity.finish();
            }
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public static void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        if (AppController.viewPager.getCurrentItem()==0) {
            getBadgeAPI();
            System.out.println("homePos:" + AppPreferenceManager.getHomePos(mContext));
        }


        resetDisconnectTimer();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }


    private double getScreenSizeInInches() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        setRealDeviceSizeInPixels();
        double x = Math.pow(mWidthPixels / dm.xdpi, 2);
        double y = Math.pow(mHeightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("debug", "Screen inches : " + screenInches);
        return screenInches;
    }

    private void setRealDeviceSizeInPixels() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);


        // since SDK_INT = 1;
        mWidthPixels = displayMetrics.widthPixels;
        mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
            }
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }
    }
    private void callBannerImageAPI() {
        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_GET_HOMELIST_IMAGE);
        String[] name = {"access_token"};

        String[] value = {AppPreferenceManager.getAccessToken(mContext)};

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    AppController.mImageArrayList = new ArrayList<String>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
                            AppController.viewPager = (ViewPager) findViewById(R.id.viewPager);
                            AppController.mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
                            AppController.mIndicator.setPageColor(Color.parseColor("#b2b2b2"));
                            AppController.mIndicator.setFillColor(Color.parseColor("#71ACD6"));
                            JSONArray staffArray = secobj.getJSONArray("sliders");
                            if (staffArray.length() > 0) {
                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject mImageObject = staffArray.optJSONObject(i);
                                    AppController.mImageArrayList.add(mImageObject.optString("image"));
                                }
                                AppController.homeViewPagerAdapter = new HomeViewPagerAdapter(mContext,mActivity, pagerCount,AppController.viewPager, getScreenSizeInInches(), AppController.mImageArrayList);
                                AppController.homeViewPagerAdapter.notifyDataSetChanged();
                                AppController.viewPager.setAdapter(AppController.homeViewPagerAdapter);
                                AppController.mIndicator.setViewPager(AppController.viewPager);
                                AppController.viewPager.setCurrentItem(0);
                                AppPreferenceManager.setHomePos(mContext, String.valueOf(0));




                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon, R.drawable.roundblue);

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callBannerImageAPI();

                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in homeActivity  is" + ex.toString());
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
    public  void getBadgeAPI() {

        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_GET_BADGE);
        String[] name = {"access_token","parentId"};

        String[] value = {AppPreferenceManager.getAccessToken(mContexts),AppPreferenceManager.getUserId(mContexts)};

        volleyWrapper.getResponsePOST(mContexts, 11, name, value, new VolleyAPIManager.ResponseListener() {
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
                            String badgeCount = secobj.optString("badgeCount");
                            AppPreferenceManager.setBadgecount(mContexts, badgeCount);
                            callBannerImageAPI();

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContexts, mContexts.getString(R.string.error_heading), mContexts.getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContexts, mContexts.getString(R.string.error_heading), mContexts.getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContexts, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getBadgeAPI();

                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContexts, "Alert", mContexts.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {


            }
        });


    }


}