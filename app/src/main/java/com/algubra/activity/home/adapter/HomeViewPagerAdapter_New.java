package com.algubra.activity.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.aboutus.AboutusRecyclerActivity;
import com.algubra.activity.calendar.CalendarActivity;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.clubs.ClubListActivity;
import com.algubra.activity.contactus.ContactUsActivity;
import com.algubra.activity.courses.CoursesListActivity;
import com.algubra.activity.curriculam.CurriculamActivity;
import com.algubra.activity.events.EventListActivity;
import com.algubra.activity.gallery.GalleryActivity;
import com.algubra.activity.home.model.HomeTabModel;
import com.algubra.activity.homework.HomeWorkSubjectActivity;
import com.algubra.activity.leaves.LeavesActivity;
import com.algubra.activity.loadweburl.LoadWebUrlActivity;
import com.algubra.activity.lostandfound.LostandFoundListActivity;
import com.algubra.activity.newsletter.NewsLetterActivity;
import com.algubra.activity.quotesandstories.QuotesandStoriesActivity;
import com.algubra.activity.schedules.SchedulesActivity;
import com.algubra.activity.settings.SettingsActivity;
import com.algubra.activity.specialmessages.SpecialMessageActivity;
import com.algubra.activity.staffdirectory.StaffDirectoryActivity;
import com.algubra.activity.studentawards.StudentAwardsListActivity;
import com.algubra.activity.studentprofile.StudentProfileListActivity;
import com.algubra.activity.timetable.TimeTableActivity;
import com.algubra.activity.userprofile.UserProfileActivity;
import com.algubra.activity.worksheet.WorkSheetSubjectListActivity;
import com.algubra.appcontroller.AppController;
import com.algubra.constants.TabIDConstants;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

/**
 * Created by gayatri on 30/5/17.
 */
public class HomeViewPagerAdapter_New extends PagerAdapter implements TabIDConstants {
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
    ImageView homebannerimg;

    public HomeViewPagerAdapter_New(Context mContext,ArrayList<HomeTabModel> mHomeTabModelsArrangement,ArrayList<HomeTabModel> mHomeTabModelsTitle,int[] mListImgArray) {
        this.mContext = mContext;
        this.mHomeTabModelsArrangement=mHomeTabModelsArrangement;
        this.mHomeTabModelsTitle=mHomeTabModelsTitle;
        this.mListImgArray=mListImgArray;
    }

    public HomeViewPagerAdapter_New(Context mContext, int pagerCount, ViewPager mViewPager) {
        this.mContext = mContext;
        this.mPagerCount = pagerCount;
        this.viewPager = mViewPager;
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
        System.out.println("mPagerCount---"+mPagerCount);
        mInflaters = LayoutInflater.from(mContext);
        //if (position == 0) {
            pageview = mInflaters.inflate(R.layout.adapter_viewpager_home_first, null);
       /* }else{
            if(position==1){

                pageview = mInflaters.inflate(R.layout.adapter_home_view_pager, null);
            }
            if(position==2){

                pageview = mInflaters.inflate(R.layout.adapter_home_view_pager, null);
            }

        }*/
        mGridView = (GridView) pageview.findViewById(R.id.viewGrid);
//        homebannerimg= (ImageView) pageview.findViewById(R.id.homebannerimg);
        mGridView.setNumColumns(3);

        if (position == 0) {
//           mBannerImage.setVisibility(View.VISIBLE);
//            mBannerImage=(ImageView)pageview.findViewById(R.id.man);
            System.out.println("row_count 0 ---"+row_count);

            homebannerimg.setVisibility(View.VISIBLE);
            row_count = 6;
            homeGridAdapter = new HomeGridAdapter(mContext,  row_count,position);
//            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);

            mGridView.setGravity(Gravity.CENTER);
            mGridView.setAdapter(homeGridAdapter);
            homeGridAdapter.notifyDataSetChanged();


        } else if (position == 1) {
            System.out.println("row_count 1 ---"+row_count);
            homebannerimg.setVisibility(View.GONE);
            mGridView.setNumColumns(3);
            mGridView.setPadding(0, 160, 0, 0);

            row_count = 12;
            homeGridAdapter = new HomeGridAdapter(mContext,  row_count,position);

//            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);

            mGridView.setAdapter(homeGridAdapter);
            homeGridAdapter.notifyDataSetChanged();

        } else if (position == 2) {
            homebannerimg.setVisibility(View.GONE);
            System.out.println("row_count 2 ---"+row_count);
            mGridView.setNumColumns(3);
            mGridView.setPadding(0, 160, 0, 0);

            row_count = 2;
            homeGridAdapter = new HomeGridAdapter(mContext,  row_count,position);
//            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);

            mGridView.setAdapter(homeGridAdapter);
            homeGridAdapter.notifyDataSetChanged();

        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                if (position==0) {
                    TAB_ID=(Integer.parseInt(AppController.mTabArrangeModelArrayList.get(i).getTab_id()));
                    IntentGridClick(TAB_ID);

                } else if (position==1) {
                    TAB_ID=(Integer.parseInt(AppController.mTabArrangeModelArrayList.get(i+6).getTab_id()));

                    IntentGridClick(TAB_ID);
                } else if (position==2) {
                    TAB_ID=(Integer.parseInt(AppController.mTabArrangeModelArrayList.get(i+18).getTab_id()));

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
    public  void IntentGridClick(int tab_id)
    {
        switch (tab_id)
        {
            case NEWS_LETTER_TAB_ID:
                intent=new Intent(mContext, NewsLetterActivity.class);
                mContext.startActivity(intent);
                break;
            case ABOUT_US_TAB_ID:
                intent =new Intent(mContext, AboutusRecyclerActivity.class);
                mContext.startActivity(intent);
                break;
            case CONTACT_US_TAB_ID:
                intent=new Intent(mContext, ContactUsActivity.class);
                mContext.startActivity(intent);
                break;
            case COURSES_TAB_ID:
                intent=new Intent(mContext, CoursesListActivity.class);
                mContext.startActivity(intent);
                break;
            case STAFF_DIRECTORY_TAB_ID:
                intent=new Intent(mContext, StaffDirectoryActivity.class);
                mContext.startActivity(intent);
                break;
            case SCHEDULES_TAB_ID:
                intent=new Intent(mContext, SchedulesActivity.class);
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
                intent =new Intent(mContext, ClubListActivity.class);
                mContext.startActivity(intent);
                break;
            case TIMETABLE_TAB_ID:
                intent =new Intent(mContext, TimeTableActivity.class);
                mContext.startActivity(intent);
                break;
            case QUOTESSTORIES_TAB_ID:
                intent =new Intent(mContext, QuotesandStoriesActivity.class);
                mContext.startActivity(intent);                break;
            case EVENTS_TAB_ID:
                intent=new Intent(mContext, EventListActivity.class);
                mContext.startActivity(intent);
                break;
            case LEAVES_TAB_ID:
                intent =new Intent(mContext, LeavesActivity.class);
                mContext.startActivity(intent);
                break;
            case LOSTANDFOUND_TAB_ID:

                intent =new Intent(mContext, LostandFoundListActivity.class);
                mContext.startActivity(intent);
                break;
            /*case OLYMPIAD_TAB_ID:
                *//*intent =new Intent(mContext, ReportIssuesListActivity.class);
                mContext.startActivity(intent);*//*
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading),"This module is under construction", R.drawable.infoicon,  R.drawable.roundblue);

                break;*/
            case STUDENT_AWARDS_TAB_ID:
                intent =new Intent(mContext, StudentAwardsListActivity.class);
                mContext.startActivity(intent);
                break;
            /*case CANTEEN_TAB_ID:
                intent =new Intent(mContext, MealsActivity.class);
                mContext.startActivity(intent);                break;*/

            case GALLERY_TAB_ID:
                intent =new Intent(mContext, GalleryActivity.class);
                mContext.startActivity(intent);
                break;
            case USERPROFILE_TAB_ID:
                intent =new Intent(mContext, UserProfileActivity.class);
                mContext.startActivity(intent);                 break;
            case STUDENTROFILE_TAB_ID:
                intent =new Intent(mContext, StudentProfileListActivity.class);
                mContext.startActivity(intent);                 break;
            case CALENDAR_TAB_ID:
                intent =new Intent(mContext, CalendarActivity.class);
                mContext.startActivity(intent);
                //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading),"Work in progress", R.drawable.infoicon,  R.drawable.roundblue);
                break;
            case NOTIFICATION_TAB_ID:
               /* intent =new Intent(mContext, NotificationListActivity.class);
                mContext.startActivity(intent); */
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Work in progress", R.drawable.infoicon,  R.drawable.roundblue);

                break;
            case SETTINGS_TAB_ID:
                intent =new Intent(mContext, SettingsActivity.class);
                mContext.startActivity(intent);
                //((Activity)mContext).finish();

                break;
            case STUDYANDCURRICULAMTAB_ID:
                intent=new Intent(mContext, CurriculamActivity.class);
                mContext.startActivity(intent);
                break;
            case NEWS_TAB_ID:
                intent =new Intent(mContext, LoadWebUrlActivity.class);
                intent.putExtra("page_type","news");
                intent.putExtra("tab_type","News");
                mContext.startActivity(intent);
                break;
            case CIRCULAR_TAB_ID:
                intent =new Intent(mContext, CircularActivity.class);
                mContext.startActivity(intent);
                break;
            case SPECIAL_MESSAGE_TAB_ID:
                intent =new Intent(mContext, SpecialMessageActivity.class);
                mContext.startActivity(intent);
                break;
                case HOMEWORK_TAB_ID:
                intent =new Intent(mContext, HomeWorkSubjectActivity.class);
                mContext.startActivity(intent);
                break;
                case WORKSHEET_TAB_ID:
                intent =new Intent(mContext, WorkSheetSubjectListActivity.class);
                mContext.startActivity(intent);
                break;

        }
    }

}