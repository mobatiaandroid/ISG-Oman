package com.algubra.activity.home.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.aboutus.AboutusRecyclerActivity;
import com.algubra.activity.calendar.CalendarActivity;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.clubs.ClubListActivity;
import com.algubra.activity.contactus.ContactUsActivity;
import com.algubra.activity.curriculam.CurriculamActivity;
import com.algubra.activity.events.EventListActivity;
import com.algubra.activity.gallery.GalleryActivity;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.home.model.HomeTabModel;
import com.algubra.activity.homework.HomeWorkSubjectActivity;
import com.algubra.activity.leaves.LeavesActivity;
import com.algubra.activity.news.NewsActivity;
import com.algubra.activity.newsletter.NewsLetterActivity;
import com.algubra.activity.notification.NotificationListActivity;
import com.algubra.activity.quotesandstories.QuotesandStoriesActivity;
import com.algubra.activity.schedules.SchedulesActivity;
import com.algubra.activity.settings.SettingsActivity;
import com.algubra.activity.specialmessages.SpecialMessageActivity;
import com.algubra.activity.staffdirectory.StaffDirectoryActivity;
import com.algubra.activity.studentawards.StudentAwardsListActivity;
import com.algubra.activity.studentleaders.StudentLeadersListActivity;
import com.algubra.activity.studentprofile.StudentProfileListActivity;
import com.algubra.activity.timetable.TimeTableHomeActivity;
import com.algubra.activity.userprofile.UserProfileActivity;
import com.algubra.activity.videos.VideosListActivity;
import com.algubra.activity.worksheet.WorkSheetSubjectListActivity;
import com.algubra.appcontroller.AppController;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.TabIDConstants;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.volleymanager.VolleyAPIManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gayatri on 18/4/17.
 */
public class HomeViewPagerAdapter extends PagerAdapter implements TabIDConstants, JsonTagConstants, URLConstants, StausCodes {
    ArrayList<HomeTabModel> mHomeTabModelsArrangement;
    ArrayList<HomeTabModel> mHomeTabModelsTitle;
    RelativeLayout relativeHeader;
    Context mContext;
    int[] mListImgArray;
    private LayoutInflater mInflaters;
    int mPagerCount;
    ViewPager viewPager;
    int row_count = 1;
    HomeGridAdapter homeGridAdapter;
    GridView mGridView;
    int TAB_ID;
    Intent intent;
    double screensize;
    public static TextView userName;
    LinearLayout llProfile;
    ViewPager bannerImagePager;
    ArrayList<String> homeBannerUrlImageArray;
    int currentPage = 0;
    Activity mActivity;

    private static final int REQUEST_PERMISSION_LOCATION = 103;
    String[] permissionsRequiredLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE};
    private SharedPreferences locationPermissionStatus;
    private boolean locationToSettings = false;
    private static final int PERMISSION_CALLBACK_CONSTANT_LOCATION = 3;


    String[] permissionsRequiredPhone = new String[]{Manifest.permission.CALL_PHONE};
    private static final int REQUEST_PERMISSION_PHONE = 104;
    public HomeViewPagerAdapter(Context mContext, ArrayList<HomeTabModel> mHomeTabModelsArrangement, ArrayList<HomeTabModel> mHomeTabModelsTitle, int[] mListImgArray) {
        this.mContext = mContext;
        this.mHomeTabModelsArrangement = mHomeTabModelsArrangement;
        this.mHomeTabModelsTitle = mHomeTabModelsTitle;
        this.mListImgArray = mListImgArray;
    }

    public HomeViewPagerAdapter(Context mContext, int pagerCount, ViewPager mViewPager, double screensize) {
        this.mContext = mContext;
        this.mPagerCount = pagerCount;
        this.viewPager = mViewPager;
        this.screensize = screensize;
    }

    public HomeViewPagerAdapter(Context mContext, Activity mActivity, int pagerCount, ViewPager mViewPager, double screensize, ArrayList<String> mImageArray) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mPagerCount = pagerCount;
        this.viewPager = mViewPager;
        this.screensize = screensize;
        this.homeBannerUrlImageArray = new ArrayList<>();
        this.homeBannerUrlImageArray = mImageArray;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPagerCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final View container, final int position) {
        View pageview = null;
        System.out.println("mPagerCount---" + mPagerCount);
        mInflaters = LayoutInflater.from(mContext);
        if (position == 0)
        {
            pageview = mInflaters.inflate(R.layout.adapter_viewpager_home_first, null);
            bannerImagePager = (ViewPager) pageview.findViewById(R.id.bannerImagePager);
            llProfile = (LinearLayout) pageview.findViewById(R.id.llProfile);
            userName = (TextView) pageview.findViewById(R.id.userName);
            llProfile.getBackground().setAlpha(150);
            userName.setText(AppPreferenceManager.getStudentName(mContext) + " (" + AppPreferenceManager.getSchoolSelection(mContext) + ")");
//            homeBannerUrlImageArray = new ArrayList<>();
//            homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/algubra/media/appimages/413607.jpg");
//            homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/algubra/media/appimages/2.jpg");
//            homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/algubra/media/appimages/1.jpg");
//            if(AppUtilityMethods.isNetworkConnected(mContext)) {
//                callBannerImageAPI();
//            }else{
//                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//            }

            llProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, StudentProfileListActivity.class));
                }
            });
            bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, mContext));
            if (homeBannerUrlImageArray != null) {

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == homeBannerUrlImageArray.size()) {
                            currentPage = 0;
                            bannerImagePager.setCurrentItem(currentPage,
                                    false);
                        } else {

                            bannerImagePager
                                    .setCurrentItem(currentPage++, true);
                        }
                    }
                };
                final Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 100, 6000);
            }
        } else {
            if (position == 1)
            {
                pageview = mInflaters.inflate(R.layout.adapter_home_view_pager, null);
            }
            if (position == 2) {

                pageview = mInflaters.inflate(R.layout.adapter_home_view_pager, null);
            }

        }
        mGridView = (GridView) pageview.findViewById(R.id.viewGrid);
        mGridView.setNumColumns(3);


        if (position == 0) {
//           mBannerImage.setVisibility(View.VISIBLE);
//            mBannerImage=(ImageView)pageview.findViewById(R.id.man);
            System.out.println("row_count 0 ---" + row_count);
            mGridView.setPadding(0, 10, 0, 0);
            row_count = 6;
            homeGridAdapter = new HomeGridAdapter(mContext, row_count, position);
//            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);
            mGridView.setGravity(Gravity.CENTER);
            mGridView.setAdapter(homeGridAdapter);
            homeGridAdapter.notifyDataSetChanged();


        } else if (position == 1) {
            System.out.println("row_count 1 ---" + row_count);
            if ((screensize > 4) && (screensize < 5)) {
                mGridView.setVerticalSpacing(27);
            }
            mGridView.setNumColumns(3);
            mGridView.setPadding(0, 30, 0, 0);
            row_count = 12;
            homeGridAdapter = new HomeGridAdapter(mContext, row_count, position);
            mGridView.setAdapter(homeGridAdapter);
            homeGridAdapter.notifyDataSetChanged();

        } else if (position == 2) {
            System.out.println("row_count 2 ---" + row_count);
            if ((screensize > 4) && (screensize < 5)) {
                mGridView.setVerticalSpacing(27);
            }
            mGridView.setNumColumns(3);
            mGridView.setPadding(0, 30, 0, 0);
            row_count = 12;
            homeGridAdapter = new HomeGridAdapter(mContext, row_count, position);
//            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);

            mGridView.setAdapter(homeGridAdapter);
            homeGridAdapter.notifyDataSetChanged();

        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    if (position == 0) {
                        TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(i).getTab_id()));
                        IntentGridClick(TAB_ID);

                    } else if (position == 1) {
                        TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(i + 6).getTab_id()));

                        IntentGridClick(TAB_ID);
                    } else if (position == 2) {
                        TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(i + 18).getTab_id()));

                        IntentGridClick(TAB_ID);
                    }
                } catch (Exception e) {
                    Log.e("Numberformat: ", e.getMessage());
                }
            }


        });
        ((ViewPager) container).addView(pageview, 0);

        return pageview;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    public void IntentGridClick(int tab_id) {
        switch (tab_id) {
            case NEWS_LETTER_TAB_ID:
                intent = new Intent(mContext, NewsLetterActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case ABOUT_US_TAB_ID:
                intent = new Intent(mContext, AboutusRecyclerActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case CONTACT_US_TAB_ID:
                locationPermissionStatus = mContext.getSharedPreferences("locationPermissionStatus", mContext.MODE_PRIVATE);


                if (Build.VERSION.SDK_INT < 23) {
                    //Do not need to check the permission
                    intent = new Intent(mContext, ContactUsActivity.class);
                    HomeActivity.stopDisconnectTimer();
                    mContext.startActivity(intent);
                } else {

                    if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[1]) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[2]) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[0])
                                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[1])
                                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[2])) {
                            //Show Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle("Need Location and Phone Permission");
                            builder.setMessage("This module needs location and phone permissions.");

                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle("Need Location and Phone Permission");
                            builder.setMessage("This module needs location and phone permissions.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    locationToSettings = true;

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    mActivity.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle("Need Location and Phone Permission");
                            builder.setMessage("This module needs location and phone permissions.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    locationToSettings = true;

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    mActivity.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle("Need Location and Phone Permission");
                            builder.setMessage("This module needs location and phone permissions.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    locationToSettings = true;

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    mActivity.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
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
                            mActivity.requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

                        }
                        SharedPreferences.Editor editor = locationPermissionStatus.edit();
                        editor.putBoolean(permissionsRequiredLocation[0], true);
                        editor.commit();
                    }
                    else {
                        intent = new Intent(mContext, ContactUsActivity.class);
                        HomeActivity.stopDisconnectTimer();
                        mContext.startActivity(intent);
                    }
                }

                break;
            case COURSES_TAB_ID:
                intent = new Intent(mContext, VideosListActivity.class);
//                intent = new Intent(mContext, CoursesListActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case STAFF_DIRECTORY_TAB_ID:
                intent = new Intent(mContext, StaffDirectoryActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case SCHEDULES_TAB_ID:
                intent = new Intent(mContext, SchedulesActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            /*case LIBRARY_TAB_ID:
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading),"This module is under construction", R.drawable.infoicon,  R.drawable.roundblue);
                break;*/
            /*case SCHOOL_SHOP_TAB_ID:
                intent =new Intent(mContext, SchoolShopCategoryActivity.class);
                mContext.startActivity(intent);
                break;*/
            case CLUB_TAB_ID:
                intent = new Intent(mContext, ClubListActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case TIMETABLE_TAB_ID:
                intent = new Intent(mContext, TimeTableHomeActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case QUOTESSTORIES_TAB_ID:
                intent = new Intent(mContext, QuotesandStoriesActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case EVENTS_TAB_ID:
                intent = new Intent(mContext, EventListActivity.class);
                HomeActivity.stopDisconnectTimer();
                mContext.startActivity(intent);
                break;
            case LEAVES_TAB_ID:
                intent = new Intent(mContext, LeavesActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case LOSTANDFOUND_TAB_ID:

                intent = new Intent(mContext, StudentLeadersListActivity.class);
//                intent = new Intent(mContext, LostandFoundListActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            /*case OLYMPIAD_TAB_ID:
             *//*intent =new Intent(mContext, ReportIssuesListActivity.class);
                mContext.startActivity(intent);*//*
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading),"This module is under construction", R.drawable.infoicon,  R.drawable.roundblue);

                break;*/
            case STUDENT_AWARDS_TAB_ID:
                intent = new Intent(mContext, StudentAwardsListActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            /*case CANTEEN_TAB_ID:
                intent =new Intent(mContext, MealsActivity.class);
                mContext.startActivity(intent);                break;*/

            case GALLERY_TAB_ID:
                intent = new Intent(mContext, GalleryActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case USERPROFILE_TAB_ID:
                intent = new Intent(mContext, UserProfileActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case STUDENTROFILE_TAB_ID:
                intent = new Intent(mContext, StudentProfileListActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;
            case CALENDAR_TAB_ID:
                intent = new Intent(mContext, CalendarActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading),"Work in progress", R.drawable.infoicon,  R.drawable.roundblue);
                break;
            case NOTIFICATION_TAB_ID:
                intent = new Intent(mContext, NotificationListActivity.class);
                HomeActivity.stopDisconnectTimer();

//                HomeGridAdapter.notifyIconBadge.setVisibility(View.INVISIBLE);

                mContext.startActivity(intent);
                // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Work in progress", R.drawable.infoicon,  R.drawable.roundblue);

                break;
            case SETTINGS_TAB_ID:
                intent = new Intent(mContext, SettingsActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                //((Activity)mContext).finish();

                break;
            case STUDYANDCURRICULAMTAB_ID:
                intent = new Intent(mContext, CurriculamActivity.class);
                HomeActivity.stopDisconnectTimer();
                mContext.startActivity(intent);
                break;
            case NEWS_TAB_ID:
                intent = new Intent(mContext, NewsActivity.class);
                HomeActivity.stopDisconnectTimer();

                intent.putExtra("page_type", "news");
                intent.putExtra("tab_type", "News");
                mContext.startActivity(intent);


               /* intent = new Intent(mContext, LoadWebUrlActivity.class);
                intent.putExtra("page_type", "news");
                intent.putExtra("tab_type", "News");
                mContext.startActivity(intent);*/
                break;
            case CIRCULAR_TAB_ID:
                intent = new Intent(mContext, CircularActivity.class);
                HomeActivity.stopDisconnectTimer();
                mContext.startActivity(intent);
                break;
            case SPECIAL_MESSAGE_TAB_ID:
                intent = new Intent(mContext, SpecialMessageActivity.class);
                HomeActivity.stopDisconnectTimer();

                mContext.startActivity(intent);
                break;

            case HOMEWORK_TAB_ID:
                intent =new Intent(mContext, HomeWorkSubjectActivity.class);
                HomeActivity.stopDisconnectTimer();
                mContext.startActivity(intent);
                break;

            case WORKSHEET_TAB_ID:
                intent =new Intent(mContext, WorkSheetSubjectListActivity.class);
                HomeActivity.stopDisconnectTimer();
                mContext.startActivity(intent);
                break;
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
                    homeBannerUrlImageArray = new ArrayList<String>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
                            JSONArray staffArray = secobj.getJSONArray("sliders");
                            if (staffArray.length() > 0) {
                                for (int i = 0; i < staffArray.length(); i++) {
                                    JSONObject mImageObject = staffArray.optJSONObject(i);
                                    homeBannerUrlImageArray.add(mImageObject.optString("image"));
                                }

                                bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, mContext));

                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.no_datafound), R.drawable.infoicon, R.drawable.roundblue);

                            }

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.error_heading), mContext.getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CALLBACK_CONSTANT_LOCATION) {
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
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location and Phone Permission");
                builder.setMessage("This module needs location and phone permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location and Phone Permission");
                builder.setMessage("This module needs location and phone permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            }else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CALL_PHONE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location and Phone Permission");
                builder.setMessage("This module needs location and phone permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationToSettings = false;

                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
//                Toast.makeText(mActivity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                locationToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT_LOCATION);
                Toast.makeText(mContext, "Go to settings and grant access to location and phone.", Toast.LENGTH_LONG).show();

            }
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ) {
                //Got Permission
                proceedAfterPermission();
            }/*else  if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                //DENIAL
                requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

            }*/
        }
    }

    public void proceedAfterPermission() {
        intent = new Intent(mContext, ContactUsActivity.class);
        HomeActivity.stopDisconnectTimer();
        mContext.startActivity(intent);
    }
}