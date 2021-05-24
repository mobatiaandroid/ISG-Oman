package com.algubra.activity.studentawards.model;

/**
 * Created by gayatri on 5/5/17.
 */
public class StudentAwardModel {
    private String subject;
    String id;
    String title;
    String pdf_link;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
