package com.algubra.activity.calendar.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.calendar.model.CalendarModel;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by gayatri on 22/5/17.
 */
public class EventsAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<CalendarModel> calendarModels;
    int colors,mPosition;
    LayoutInflater minfalter;
    ViewHolder viewHolder;
    private int mnthId;

    public  EventsAdapter(Context mContext,ArrayList<CalendarModel> calendarModels){
        this.mContext=mContext;
        this.calendarModels=calendarModels;
    }
    public  EventsAdapter(Context mContext,ArrayList<CalendarModel> calendarModels,int colors){
        this.mContext=mContext;
        this.calendarModels=calendarModels;
        this.colors=colors;
    }
    public  EventsAdapter(Context mContext,ArrayList<CalendarModel> calendarModels,int colors,int mPosition){
        this.mContext=mContext;
        this.calendarModels=calendarModels;
        this.colors=colors;
        this.mPosition=mPosition;
    }
    @Override
    public int getCount() {
        return calendarModels.size();
    }

    @Override
    public Object getItem(int position) {
        return calendarModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final LayoutInflater minfalter = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            minfalter = LayoutInflater.from(mContext);
            convertView = minfalter.inflate(R.layout.layout_calendar_event_item, null);
            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.eventName);
            viewHolder.eventTime = (TextView) convertView.findViewById(R.id.eventTime);
            viewHolder.addiconImageView = (ImageView) convertView.findViewById(R.id.addicon);
            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventTime.setTextColor(colors);
        viewHolder.eventName.setTextColor(colors);
        viewHolder.eventName.setText(calendarModels.get(position).getEvent());
        viewHolder.eventTime.setText(calendarModels.get(position).getStartTime() + " - " + calendarModels.get(position).getEndTime());
        if (mPosition==0)
        {
            viewHolder.addiconImageView.setImageResource(R.drawable.calendarorange);
        }
        else   if (mPosition==1)
        {
            viewHolder.addiconImageView.setImageResource(R.drawable.calendarviolet);
        }
        else   if (mPosition==2)
        {
            viewHolder.addiconImageView.setImageResource(R.drawable.calendarred);
        }
        else   if (mPosition==3)
        {
            viewHolder.addiconImageView.setImageResource(R.drawable.calendarblue);
        }

        viewHolder.addiconImageView .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, calendarModels.get(position).getEvent());
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        AppUtilityMethods.getMiliseconds(AppUtilityMethods.removeTime(calendarModels.get(position).getFromTime()), AppUtilityMethods.removeDateOnly(calendarModels.get(position).getFromTime())));
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        AppUtilityMethods.getMiliseconds(AppUtilityMethods.removeTime(calendarModels.get(position).getToTime()), AppUtilityMethods.removeDateOnly(calendarModels.get(position).getToTime())));
                intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
                intent.putExtra(CalendarContract.Events.DESCRIPTION, calendarModels.get(position).getDescription());
                mContext.startActivity(intent);

//             String reqSentence = AppUtilityMethods.htmlparsing(String.valueOf(
//                        Html.fromHtml(calendarModels.get(position)
//                                .getEvent())).replaceAll("\\s+", " "));
//                String[] splited = reqSentence.split("\\s+");
//                String[] dateString;
//                int yearTo = -1;
//                int monthTo = -1;
//                int dayTo = -1;
//                int year = -1;
//                int month = -1;
//                int day = -1;
//                int day1=-1;
//                String[] timeString;
//                int hour = -1;
//                int min = -1;
//                String[] timeString1;
//                int hour1 = -1;
//                int min1 = -1;
//                year = Integer.parseInt(calendarModels.get(position).getYearDate());
//                System.out.println("Year--"+year);
//                String[] months=AppUtilityMethods.removeTime(calendarModels.get(position).getFromTime()).split("-");
//                month = Integer.parseInt(months[1])-1;
//                day =  Integer.parseInt(calendarModels.get(position).getDayDate());
//                String[] months1=AppUtilityMethods.removeTime(calendarModels.get(position).getToTime()).split("-");
//
//                day1= Integer.parseInt(months1[2])-1;
//                System.out.println("Year--"+day);
//
//                // timeString= //calendarModels.get(position).getFromTime();
//                timeString = AppUtilityMethods.removeDate(calendarModels.get(position).getFromTime()).split(":");
//                hour = Integer.parseInt(timeString[0]);
//                System.out.println("Year--"+hour);
//
//                min = Integer.parseInt(timeString[1]);
//                System.out.println("Year--"+min);
//
//                //timeString1 = AppUtilityMethods(calendarModels.get(position).getToTime().split(":");
//                timeString1 = AppUtilityMethods.removeDate(calendarModels.get(position).getToTime()).split(":");
//
//                hour1 = Integer.parseInt(timeString1[0]);
//                min1 = Integer.parseInt(timeString1[1]);
//                boolean addToCalendar = true;
//
//                System.out.println("addToCalendar---"+addToCalendar);
//                if (addToCalendar) {
//                    if (year != -1 && month != -1 && day != -1 && hour != -1
//                            && min != -1 && day1 != -1) {
//                        addReminder(year, month, day, hour, min, year, month,
//                                (day1), hour1, min1,
//                                calendarModels.get(position).getEvent(),
//                                calendarModels.get(position).getEvent(), 0, position);
//                       // Toast.makeText(mContext,"Successfully added to calendar",Toast.LENGTH_SHORT).show();
//                    } else {
//
//                    }
//                } else {
//
//                }
            }
        });


        return convertView;
    }

    class ViewHolder{
        TextView eventName;
        TextView eventTime;
        ImageView addiconImageView;
        ImageView removeiconImageView;

    }

    /*******************************************************
     * Method name : addReminder() Description : add reminder to calendar
     * without popup Parameters : statrYear, startMonth, startDay, startHour,
     * startMinute, endYear, endMonth, endDay, endHour, endMinutes, name Return
     * type : void Date : 21-Jan-2015 Author : Rijo K Jose
     *****************************************************/

    public void addReminder(int startYear, int startMonth, int startDay,
                            int startHour, int startMinute, int endYear, int endMonth,
                            int endDay, int endHour, int endMinutes, String name,
                            String description, int count, int position) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(endYear, endMonth, endDay, endHour, endMinutes);
        long endMillis = endTime.getTimeInMillis();
        /*Uri EVENTS_URI = Uri.parse(CalendarContract.Events.CONTENT_URI.toString());

       // String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);
        eventValues.put(CalendarContract.Events.TITLE, name);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.SHORT);
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        eventValues.put("eventStatus", 1);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);
        *//*Uri eventUri = mContext.getContentResolver().insert(
                Uri.parse(eventUriString), eventValues);*//*
        Uri eventUri = mContext.getContentResolver().insert(
                EVENTS_URI, eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());
        Log.d("TAG", "1----" + eventID);
        calendarModels.get(position).setId(String.valueOf(eventID));
        Log.d("TAG", "2----");*/

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, name);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                endMillis);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
        intent.putExtra(CalendarContract.Events.DESCRIPTION,description);
        mContext.startActivity(intent);

    }





}
