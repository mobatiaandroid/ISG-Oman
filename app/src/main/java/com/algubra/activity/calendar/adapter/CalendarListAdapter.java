package com.algubra.activity.calendar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.calendar.model.CalendarModel;
import com.algubra.appcontroller.AppController;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/5/17.
 */
public class CalendarListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<CalendarModel> calendarModels;
    //ArrayList<CalendarModel> eventModels=new ArrayList<>();
    ArrayList<CalendarModel> eventModels;
    //    int[] colours = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};
    int[] colours = AppController.getInstance().getResources().getIntArray(R.array.calendar_row_colors);
    int colorValue;
    LayoutInflater minfalter;
    ViewHolder viewHolder;
    String eventDatesToDisplay="";
    String  eventStartDatesWithoutTime="";
    String  eventStartDatesWithoutDate="";
    String  eventEndDatesWithoutTime="";
    String  eventEndDatesWithoutDate="";
     Dialog dialog;
    public  CalendarListAdapter(Context mContext,ArrayList<CalendarModel> calendarModels,ArrayList<CalendarModel> eventModels){
        this.mContext=mContext;
        this.calendarModels=calendarModels;
        this.eventModels=eventModels;
    }
    public  CalendarListAdapter(Context mContext,ArrayList<CalendarModel> calendarModels){
        this.mContext=mContext;
        this.calendarModels=calendarModels;
         dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_calendar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            minfalter = LayoutInflater.from(mContext);
//              minfalter = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minfalter.inflate(R.layout.adapter_calendar_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.eventsListView = (ListView) convertView.findViewById(R.id.eventsListView);
            viewHolder.headerTextView = (TextView) convertView.findViewById(R.id.dateNTime);
            viewHolder.header = (LinearLayout) convertView.findViewById(R.id.header);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.headerTextView.setText(calendarModels.get(position).getDayStringDate()+" "+calendarModels.get(position).getDayDate()+" "+calendarModels.get(position).getMonthDate()+" "+calendarModels.get(position).getYearDate());
        colorValue=colours[position % colours.length];
        viewHolder.header.setBackgroundColor(colorValue);
        setListView(colorValue, position);
        setListViewHeightBasedOnChildren(viewHolder.eventsListView);
        return convertView;
    }

    class ViewHolder{
        ListView eventsListView;
        TextView headerTextView;
        LinearLayout header;
    }

    /*private void loadItems() {

        //creating new items in the list
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setEvent("Mindful Parenting Workshop");
        calendarModel.setFromTime("07:00pm");
        calendarModel.setToTime("09:00pm");
        eventModels.add(calendarModel);
    }*/
    private void setListView(int colors, final int mPosition) {
        //for(int i=0;i<2;i++){
        // loadSubItems();
        // }
        EventsAdapter calendarFragmentListAdapter=new EventsAdapter(mContext,calendarModels.get(mPosition).getEventModels(),colors,mPosition);
        viewHolder.eventsListView.setAdapter(calendarFragmentListAdapter);
        //calendarFragmentListAdapter.notifyDataSetChanged();
        viewHolder.eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dialog.isShowing())
                {
                    dialog.dismiss();
                    showCalendarEvent(calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(mPosition).getEventModels().get(position).getDescription(), calendarModels.get(mPosition).getEventModels().get(position).getFromTime(), calendarModels.get(mPosition).getEventModels().get(position).getToTime());

                }
                else {
                    showCalendarEvent(calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(mPosition).getEventModels().get(position).getDescription(), calendarModels.get(mPosition).getEventModels().get(position).getFromTime(), calendarModels.get(mPosition).getEventModels().get(position).getToTime());
                }
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {




        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
    }

    private void showCalendarEvent(final String eventTitles, final String eventDescriptions, String eventStartDates,String eventEndDates) {

        // set the custom dialog components - edit text, button
        TextView eventTitle = (TextView) dialog.findViewById(R.id.eventTitle);
        TextView eventDescription = (TextView) dialog.findViewById(R.id.eventDescription);
        TextView eventDate = (TextView) dialog.findViewById(R.id.eventDate);
        final ScrollView eventNameScroll = (ScrollView) dialog.findViewById(R.id.eventNameScroll);
        eventNameScroll.post(new Runnable() {
            public void run() {
                eventNameScroll.fullScroll(View.FOCUS_UP);
            }
        });
        eventTitle.setText(eventTitles);
         eventStartDatesWithoutTime=AppUtilityMethods.removeTime(eventStartDates);
         eventStartDatesWithoutDate=AppUtilityMethods.removeDateOnly(eventStartDates);
          eventEndDatesWithoutTime=AppUtilityMethods.removeTime(eventEndDates);
          eventEndDatesWithoutDate=AppUtilityMethods.removeDateOnly(eventEndDates);
        if (eventStartDatesWithoutTime.equalsIgnoreCase(eventEndDatesWithoutTime))
        {
            if (eventStartDatesWithoutDate.equalsIgnoreCase(eventEndDatesWithoutDate))
            {
                eventDatesToDisplay=AppUtilityMethods.dateConversionyyyyMMddToDDMMYYYY(eventStartDates);
                eventDate.setText(eventDatesToDisplay);

            }
            else
            {
                eventDatesToDisplay=AppUtilityMethods.dateConversionyyyyMMddToDDMMYYYY(eventStartDates)+" to "+AppUtilityMethods.removeDatetoAMPM(eventEndDates);
                eventDate.setText(eventDatesToDisplay);

            }
        }
        else
        {
            eventDatesToDisplay=AppUtilityMethods.dateConversionyyyyMMddToDDMMYYYY(eventStartDates) + " to "+AppUtilityMethods.dateConversionyyyyMMddToDDMMYYYY(eventEndDates);
            eventDate.setText(eventDatesToDisplay);

        }
        eventDescription.setText(eventDescriptions);
        Button addToCalendar = (Button) dialog
                .findViewById(R.id.addToCalendar);
        Button dismiss = (Button) dialog.findViewById(R.id.dismiss);



        // if button is clicked, close the custom dialog
        addToCalendar.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(Intent.ACTION_EDIT);
                                                 intent.setType("vnd.android.cursor.item/event");
                                                 intent.putExtra(CalendarContract.Events.TITLE, eventTitles);
                                                 intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                                         AppUtilityMethods.getMiliseconds(eventStartDatesWithoutTime,eventStartDatesWithoutDate));
                                                 intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                                         AppUtilityMethods.getMiliseconds(eventEndDatesWithoutTime,eventEndDatesWithoutDate));
                                                 intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
                                                 intent.putExtra(CalendarContract.Events.DESCRIPTION, eventDescriptions);
                                                 mContext.startActivity(intent);

                                             }
                                         }

        );

        dismiss.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {

                                           dialog.dismiss();
                                       }
                                   }

        );
        dialog.show();
    }



}
