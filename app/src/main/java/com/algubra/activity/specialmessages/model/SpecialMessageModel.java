package com.algubra.activity.specialmessages.model;

import java.io.Serializable;

/**
 * Created by gayatri on 4/5/17.
 */
public class SpecialMessageModel implements Serializable{
    String id;
    String message;
    String time;
    String img_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
