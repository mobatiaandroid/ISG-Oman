package com.algubra.activity.lostandfound.model;

import java.io.Serializable;

/**
 * Created by user2 on 5/5/17.
 */
public class LostandFoundModel implements Serializable{

    private String id;
    private String userId;
private String title;
    private String description;
    private String contact_person_name;
    private String contact_person_email;
    private String contact_person_phone;
    private String posted_user;
    private String posted_user_email;
    private String posted_user_contact;
    private String lostImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getContact_person_email() {
        return contact_person_email;
    }

    public void setContact_person_email(String contact_person_email) {
        this.contact_person_email = contact_person_email;
    }

    public String getContact_person_phone() {
        return contact_person_phone;
    }

    public void setContact_person_phone(String contact_person_phone) {
        this.contact_person_phone = contact_person_phone;
    }

    public String getPosted_user() {
        return posted_user;
    }

    public void setPosted_user(String posted_user) {
        this.posted_user = posted_user;
    }

    public String getPosted_user_email() {
        return posted_user_email;
    }

    public void setPosted_user_email(String posted_user_email) {
        this.posted_user_email = posted_user_email;
    }

    public String getPosted_user_contact() {
        return posted_user_contact;
    }

    public void setPosted_user_contact(String posted_user_contact) {
        this.posted_user_contact = posted_user_contact;
    }

    public String getLostImage() {
        return lostImage;
    }

    public void setLostImage(String lostImage) {
        this.lostImage = lostImage;
    }
}
