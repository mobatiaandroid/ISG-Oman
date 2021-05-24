package com.algubra.activity.homework.model;

import java.io.Serializable;

public class HistoryModel implements Serializable {

String title;
String file;
String type;
String status;
String reason;
String corrected_file;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCorrected_file() {
        return corrected_file;
    }

    public void setCorrected_file(String corrected_file) {
        this.corrected_file = corrected_file;
    }
}
