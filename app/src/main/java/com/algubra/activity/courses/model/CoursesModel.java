package com.algubra.activity.courses.model;

import java.io.Serializable;

/**
 * Created by user2 on 5/5/17.
 */
public class CoursesModel implements Serializable{

    private String courseName;
    private String courseId;
    private String courseNoofSeats;
    private String courseDuration;
    private String courseDescription;
    private String couserSchedule;
    private String courseSubjects;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseNoofSeats() {
        return courseNoofSeats;
    }

    public void setCourseNoofSeats(String courseNoofSeats) {
        this.courseNoofSeats = courseNoofSeats;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCouserSchedule() {
        return couserSchedule;
    }

    public void setCouserSchedule(String couserSchedule) {
        this.couserSchedule = couserSchedule;
    }

    public String getCourseSubjects() {
        return courseSubjects;
    }

    public void setCourseSubjects(String courseSubjects) {
        this.courseSubjects = courseSubjects;
    }
}
