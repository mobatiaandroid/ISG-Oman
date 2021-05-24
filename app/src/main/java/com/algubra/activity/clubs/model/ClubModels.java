package com.algubra.activity.clubs.model;

import java.io.Serializable;

/**
 * Created by gayatri on 3/5/17.
 */
public class ClubModels implements Serializable{
    String club_id;
    String club_name;
    String club_description;
    String club_cordinator;
    String club_student_id;
    String image;
    String pdf;

    public String getClub_id() {
        return club_id;
    }

    public void setClub_id(String club_id) {
        this.club_id = club_id;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getClub_description() {
        return club_description;
    }

    public void setClub_description(String club_description) {
        this.club_description = club_description;
    }

    public String getClub_cordinator() {
        return club_cordinator;
    }

    public void setClub_cordinator(String club_cordinator) {
        this.club_cordinator = club_cordinator;
    }

    public String getClub_student_id() {
        return club_student_id;
    }

    public void setClub_student_id(String club_student_id) {
        this.club_student_id = club_student_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
