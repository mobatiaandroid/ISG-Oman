package com.algubra.activity.home.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 18/4/17.
 */
public class HomeTabModel implements Serializable{
    public String tab_id;
    public String tab_title;
    public String icon_image;
    //public String icon_title;
    ArrayList<HomeTabModel> mTabArragementModel;
    ArrayList<HomeTabModel> mTabTitleModel;

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }

    public String getTab_title() {
        return tab_title;
    }

    public void setTab_title(String tab_title) {
        this.tab_title = tab_title;
    }

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    public ArrayList<HomeTabModel> getmTabArragementModel() {
        return mTabArragementModel;
    }

    public void setmTabArragementModel(ArrayList<HomeTabModel> mTabArragementModel) {
        this.mTabArragementModel = mTabArragementModel;
    }

    public ArrayList<HomeTabModel> getmTabTitleModel() {
        return mTabTitleModel;
    }

    public void setmTabTitleModel(ArrayList<HomeTabModel> mTabTitleModel) {
        this.mTabTitleModel = mTabTitleModel;
    }
}
