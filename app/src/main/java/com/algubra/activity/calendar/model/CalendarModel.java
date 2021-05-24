package com.algubra.activity.calendar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 22/5/17.
 */
public class CalendarModel implements Serializable {

    String dateNTime;
    String event;
    String fromTime;
    String startTime;
    String endTime;
    String monthDate;
    String eventName;
    String description;
    String montrhNo;

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public String getDayStringDate() {
        return dayStringDate;
    }

    public void setDayStringDate(String dayStringDate) {
        this.dayStringDate = dayStringDate;
    }

    public String getYearDate() {
        return yearDate;
    }

    public void setYearDate(String yearDate) {
        this.yearDate = yearDate;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    String yearDate;
    String dayStringDate;
    String dayDate;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    String toTime;
    String id;

    public ArrayList<CalendarModel> getEventModels() {
        return eventModels;
    }

    public void setEventModels(ArrayList<CalendarModel> eventModels) {
        this.eventModels = eventModels;
    }

    ArrayList<CalendarModel>eventModels;
    public String getDateNTime() {
        return dateNTime;
    }

    public void setDateNTime(String dateNTime) {
        this.dateNTime = dateNTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMontrhNo() {
        return montrhNo;
    }

    public void setMontrhNo(String montrhNo) {
        this.montrhNo = montrhNo;
    }


    String dayOfTheWeekTo;
    String dayTo ;
    String monthStringTo ;
    String monthNumberTo ;
    String yearTo;

    public String getDayOfTheWeekTo() {
        return dayOfTheWeekTo;
    }

    public void setDayOfTheWeekTo(String dayOfTheWeekTo) {
        this.dayOfTheWeekTo = dayOfTheWeekTo;
    }

    public String getDayTo() {
        return dayTo;
    }

    public void setDayTo(String dayTo) {
        this.dayTo = dayTo;
    }

    public String getMonthStringTo() {
        return monthStringTo;
    }

    public void setMonthStringTo(String monthStringTo) {
        this.monthStringTo = monthStringTo;
    }

    public String getMonthNumberTo() {
        return monthNumberTo;
    }

    public void setMonthNumberTo(String monthNumberTo) {
        this.monthNumberTo = monthNumberTo;
    }

    public String getYearTo() {
        return yearTo;
    }

    public void setYearTo(String yearTo) {
        this.yearTo = yearTo;
    }
}
