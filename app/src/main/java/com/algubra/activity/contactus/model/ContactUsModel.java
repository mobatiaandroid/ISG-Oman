package com.algubra.activity.contactus.model;

import java.io.Serializable;

/**
 * Created by gayatri on 17/5/17.
 */
public class ContactUsModel implements Serializable {
String name;
String email;
String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
