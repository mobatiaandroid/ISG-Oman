package com.algubra.activity.home.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.model.HomeTabModel;
import com.algubra.appcontroller.AppController;
import com.algubra.constants.TabIDConstants;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 18/4/17.
 */
public class HomeGridAdapter extends BaseAdapter implements TabIDConstants {
    Context mContext;
    ArrayList<HomeTabModel> mTabModelArrayList;
    int[] mImageList;
    public  static View mView;
    TextView mTxtView;
    public  static TextView notifyIconBadge;
    ImageView mImgView;
    int row_counts = 1;
    int mPosition;

    public HomeGridAdapter(Context mContext, ArrayList<HomeTabModel> mTabModelArrayList, int[] mImageList) {
        this.mContext = mContext;
        this.mTabModelArrayList = mTabModelArrayList;
        this.mImageList = mImageList;
    }

    public HomeGridAdapter(Context context, int row_count, int mPosition) {
        this.mContext = context;
        this.row_counts = row_count;
        this.mPosition = mPosition;
    }

    @Override
    public int getCount() {
        return row_counts;
    }

    @Override
    public Object getItem(int position) {
        return mTabModelArrayList.get(position).getTab_title();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if (convertView == null) {
        LayoutInflater inflate = LayoutInflater.from(mContext);
        mView = inflate.inflate(R.layout.adapter_home_gridview, null);
        /*} else {
            mView = convertView;
        }
*/
        mTxtView = (TextView) mView.findViewById(R.id.dep_name);
        mImgView = (ImageView) mView.findViewById(R.id.imgView);
//       AppController.textBadge = (TextView) mView.findViewById(R.id.notifyIconBadge);
        notifyIconBadge = (TextView) mView.findViewById(R.id.notifyIconBadge);
        try{
            System.out.println("Size of array---" + AppController.mTabArrangeModelArrayList.size() + "row count---" + row_counts + mPosition);
            if (row_counts == 6 && mPosition == 0) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName());
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())-1).getTabName());
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName());

                int TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(position).getTab_id()));
                SetIconsAndTitle(TAB_ID);


            } else if (row_counts == 12 && mPosition == 1) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName()+6);
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())+5).getTabName());
                int TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(position + 6).getTab_id()));
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position+6).getTabName());
                System.out.println("TAB_ID--" + TAB_ID);

                SetIconsAndTitle(TAB_ID);


            }
/*            else if (row_counts == 5 && mPosition == 2) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName()+18);
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())+17).getTabName());
                int TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(position + 18).getTab_id()));
                System.out.println("TAB_ID--" + TAB_ID);

//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position+18).getTabName());
//System.out.println("Tab id---"+TAB_ID);
                SetIconsAndTitle(TAB_ID);
            } else if (row_counts == 12 && mPosition == 2) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName()+18);
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())+17).getTabName());
                int TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(position + 18).getTab_id()));
                System.out.println("TAB_ID--" + TAB_ID);

//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position+18).getTabName());
//System.out.println("Tab id---"+TAB_ID);
                SetIconsAndTitle(TAB_ID);
            }*/
        } catch (Exception e) {
            Log.e("Numberformat: ", e.getMessage());
        }
        return mView;
    }

    public void setNotifyIconVisibility(int visible) {
        if (visible == 0) {
//            AppController.textBadge.setVisibility(View.INVISIBLE);
            notifyIconBadge.setVisibility(View.INVISIBLE);
        } else {
//            AppController.textBadge.setVisibility(View.VISIBLE);
            notifyIconBadge.setVisibility(View.VISIBLE);

        }

    }

    public void SetIconsAndTitle(int tab_id) {
        switch (tab_id) {
            case NEWS_LETTER_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.news);
                mTxtView.setText(NEWS_LETTER);
                setNotifyIconVisibility(0);
                break;
            case ABOUT_US_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.aboutus);
                mTxtView.setText(ABOUT_US);
                setNotifyIconVisibility(0);

                break;
            case CONTACT_US_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.location);
                mTxtView.setText(CONTACT_US);
                setNotifyIconVisibility(0);


                break;
            case COURSES_TAB_ID:
//                mImgView.setBackgroundResource(R.drawable.courses);
                mImgView.setBackgroundResource(R.drawable.videos);
                mTxtView.setText(COURSES);
                setNotifyIconVisibility(0);


                break;
            case STAFF_DIRECTORY_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.staffdirectory_icon);
                mTxtView.setText(STAFF_DIRECTORY);
                setNotifyIconVisibility(0);


                break;
            case SCHEDULES_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.schedule);
                mTxtView.setText(SCHEDULES);

                break;
            case NEWS_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.news_new);
                mTxtView.setText(NEWS);
                setNotifyIconVisibility(0);


                break;
            /*case SCHOOL_SHOP_TAB_ID: mImgView.setBackgroundResource(R.drawable.schoolsupply);
                mTxtView.setText(SCHOOL_SHOP);

                break;*/
            case CLUB_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.club);
                mTxtView.setText(CLUB);
                setNotifyIconVisibility(0);


                break;
            case TIMETABLE_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.timetable);
                mTxtView.setText(TIMETABLE);
                setNotifyIconVisibility(0);


                break;
            case CALENDAR_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.calender);
                mTxtView.setText(CALENDAR);
                setNotifyIconVisibility(0);


                break;
            case EVENTS_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.event);
                mTxtView.setText(EVENTS);
                setNotifyIconVisibility(0);

                break;
            case LEAVES_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.leaves);
                mTxtView.setText(LEAVES);
                setNotifyIconVisibility(0);

                break;
            case LOSTANDFOUND_TAB_ID:
//                mImgView.setBackgroundResource(R.drawable.lostandfound);
                mImgView.setBackgroundResource(R.drawable.studentleaders);
                mTxtView.setText(LOSTANDFOUND);
                setNotifyIconVisibility(0);

                break;
            /*case REPORT_ISSUE_TAB_ID: mImgView.setBackgroundResource(R.drawable.reportanissue);
                mTxtView.setText(REPORT_ISSUE);
                break;*/
            case STUDENT_AWARDS_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.student_awards);
                mTxtView.setText(STUDENT_AWARDS);
                setNotifyIconVisibility(0);

                break;
           /* case CANTEEN_TAB_ID: mImgView.setBackgroundResource(R.drawable.meals);
                mTxtView.setText(CANTEEN);
                break;*/
            case GALLERY_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.photos);
                mTxtView.setText(GALLERY);
                setNotifyIconVisibility(0);

                break;
            case QUOTESSTORIES_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.quotesstories);
                mTxtView.setText(QUOTESSTORIES);
                setNotifyIconVisibility(0);

                break;
            case NOTIFICATION_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.notification);
                mTxtView.setText(NOTIFICATION);

                if (AppPreferenceManager.getBadgecount(mContext).equalsIgnoreCase("0")) {

                    setNotifyIconVisibility(0);

                } else {
//                    AppController.textBadge.setText(AppPreferenceManager.getBadgecount(mContext));
                    notifyIconBadge.setText(AppPreferenceManager.getBadgecount(mContext));
                    setNotifyIconVisibility(1);

                }
                LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageReceiver, new IntentFilter("badgenotify"));

                break;
            case SETTINGS_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.settings);
                mTxtView.setText(SETTINGS);
                setNotifyIconVisibility(0);

                break;
            case USERPROFILE_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.userprofile);
                mTxtView.setText(USERPROFILE);
                setNotifyIconVisibility(0);

                break;
            case STUDENTROFILE_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.kidsprofile);
                mTxtView.setText(STUDENTROFILE);
                setNotifyIconVisibility(0);

                break;
            case STUDYANDCURRICULAMTAB_ID:
                mImgView.setBackgroundResource(R.drawable.study_plan_and_curriculam);
                mTxtView.setText(STUDYANDCURRICULAM);
                setNotifyIconVisibility(0);

                break;
            /*case ROUND_SQUARE_TAB_ID: mImgView.setBackgroundResource(R.drawable.round_square);
            mTxtView.setText(ROUND_SQUARE);
            break;*/
            case SPECIAL_MESSAGE_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.special_messages);
                mTxtView.setText(SPECIAL_MESSAGE);
                setNotifyIconVisibility(0);

                break;
            case CIRCULAR_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.circulars);
                mTxtView.setText(CIRCULAR_MESSAGE);
                setNotifyIconVisibility(0);

                break;
            case HOMEWORK_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.homework);
                mTxtView.setText(HOME_WORK);
                setNotifyIconVisibility(0);

                break;
            case WORKSHEET_TAB_ID:
                mImgView.setBackgroundResource(R.drawable.worksheet);
                mTxtView.setText(WORK_SHEET);
                setNotifyIconVisibility(0);
                break;

            case INV_TAB_ID_1:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_1);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_2:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_2);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_3:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_3);
                mTxtView.setVisibility(View.INVISIBLE);
                break;

            case INV_TAB_ID_4:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_4);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_5:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_5);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_6:
                mImgView.setBackgroundResource(R.drawable.homework);
//                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_6);
//                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_7:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_7);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_8:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_8);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_9:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_9);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            case INV_TAB_ID_10:
                mImgView.setBackgroundResource(R.drawable.homework);
                mImgView.setVisibility(View.INVISIBLE);
                mImgView.setEnabled(false);
                mTxtView.setText(INV_TAB_SHEET_10);
                mTxtView.setVisibility(View.INVISIBLE);
                break;
            /*case OLYMPIAD_TAB_ID: mImgView.setBackgroundResource(R.drawable.olympiadregistration);
                mTxtView.setText(OLYMPIAD);
                break;*/

        }
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String identifierString = intent.getAction();

            if (identifierString.equals("badgenotify")){

                System.out.println("Badge Count::"+AppPreferenceManager.getBadgecount(mContext));
//                notifyIconBadge = (TextView) mView.findViewById(R.id.notifyIconBadge);
//                notifyIconBadge.setVisibility(View.VISIBLE);
                if (!(AppPreferenceManager.getBadgecount(mContext).equalsIgnoreCase("0"))) {
                    notifyIconBadge.setText(AppPreferenceManager.getBadgecount(mContext));

                    notifyIconBadge.setVisibility(View.VISIBLE);


                } else {
                    notifyIconBadge.setText(AppPreferenceManager.getBadgecount(mContext));
//                    setNotifyIconVisibility(1);
                    notifyIconBadge.setVisibility(View.INVISIBLE);


                }
//                SetIconsAndTitle(NOTIFICATION_TAB_ID);

//                notifyIconBadge.setText(AppPreferenceManager.getBadgecount(mContext));
//                setNotifyIconVisibility(1);

            }
        }
    };


}