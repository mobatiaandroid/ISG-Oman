package com.algubra.activity.circulars.model;

/**
 * Created by gayatri on 17/5/17.
 */
public class CircularModel {
    String id;
    String title;


    String circularDate;
    String pdf_link;
    public String getCircularDate() {
        return circularDate;
    }

    public void setCircularDate(String circularDate) {
        this.circularDate = circularDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }
}
