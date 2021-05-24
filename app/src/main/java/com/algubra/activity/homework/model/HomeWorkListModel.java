package com.algubra.activity.homework.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeWorkListModel implements Serializable {
    String title;
    String file;
    String teacher;
    String teacherId;
    String type;
    String id;
    String description;
    String due_date;
    String setCorrected_file;
    String status;
    String set_date;
    Boolean isPopupShown;
    Boolean isOpen;
    ArrayList<HistoryModel>mHistoryList;

    public ArrayList<HistoryModel> getmHistoryList() {
        return mHistoryList;
    }

    public void setmHistoryList(ArrayList<HistoryModel> mHistoryList) {
        this.mHistoryList = mHistoryList;
    }


    public String getSetCorrected_file() {
        return setCorrected_file;
    }

    public void setSetCorrected_file(String setCorrected_file) {
        this.setCorrected_file = setCorrected_file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Boolean getPopupShown() {
        return isPopupShown;
    }

    public void setPopupShown(Boolean popupShown) {
        isPopupShown = popupShown;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSet_date() {
        return set_date;
    }

    public void setSet_date(String set_date) {
        this.set_date = set_date;
    }
}
