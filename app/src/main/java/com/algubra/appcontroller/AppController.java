package com.algubra.appcontroller;

import androidx.multidex.MultiDexApplication;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;

import com.algubra.activity.home.adapter.HomeViewPagerAdapter;
import com.algubra.activity.home.model.HomeTabModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by gayatri on 11/4/17.
 */
 public class AppController extends MultiDexApplication {
    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
public  static  String mapId="";
    private static AppController mInstance;
    public static ArrayList<HomeTabModel> mTabArrangeModelArrayList;
    public static ArrayList<HomeTabModel> mTabTitleModelArrayList;
    public static ViewPager viewPager;
    public static HomeViewPagerAdapter homeViewPagerAdapter;
    public static  ArrayList<String> mImageArrayList;
    public static CirclePageIndicator mIndicator;

    ;
   // public static ArrayList<StudentModels> studentArrayList=new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mTabArrangeModelArrayList=new ArrayList<HomeTabModel>();
        mTabTitleModelArrayList=new ArrayList<HomeTabModel>();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}