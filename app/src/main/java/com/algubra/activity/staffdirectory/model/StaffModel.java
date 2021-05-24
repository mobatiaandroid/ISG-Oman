package com.algubra.activity.staffdirectory.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 28/4/17.
 */
public class StaffModel implements Serializable{
    public String staff_id;
    public String staff_phone;
    public String staff_email;
    public String staff_staff_photo;
    public String staff_name;
    public String staff_about;
    public String staff_subject;
    public String staff_dept;
    public String staff_class;
    public String staff_section;

    public String getStaff_class() {
        return staff_class;
    }

    public void setStaff_class(String staff_class) {
        this.staff_class = staff_class;
    }

    public String getStaff_section() {
        return staff_section;
    }

    public void setStaff_section(String staff_section) {
        this.staff_section = staff_section;
    }

    public ArrayList<StaffModel> staffModelArrayList;

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_phone() {
        return staff_phone;
    }

    public void setStaff_phone(String staff_phone) {
        this.staff_phone = staff_phone;
    }

    public String getStaff_email() {
        return staff_email;
    }

    public void setStaff_email(String staff_email) {
        this.staff_email = staff_email;
    }

    public String getStaff_staff_photo() {
        return staff_staff_photo;
    }

    public void setStaff_staff_photo(String staff_staff_photo) {
        this.staff_staff_photo = staff_staff_photo;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_about() {
        return staff_about;
    }

    public void setStaff_about(String staff_about) {
        this.staff_about = staff_about;
    }

    public String getStaff_subject() {
        return staff_subject;
    }

    public void setStaff_subject(String staff_subject) {
        this.staff_subject = staff_subject;
    }

    public String getStaff_dept() {
        return staff_dept;
    }

    public void setStaff_dept(String staff_dept) {
        this.staff_dept = staff_dept;
    }

    public ArrayList<StaffModel> getStaffModelArrayList() {
        return staffModelArrayList;
    }

    public void setStaffModelArrayList(ArrayList<StaffModel> staffModelArrayList) {
        this.staffModelArrayList = staffModelArrayList;
    }
}
