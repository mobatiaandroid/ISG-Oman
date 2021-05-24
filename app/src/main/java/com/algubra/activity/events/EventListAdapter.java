package com.algubra.activity.events;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.events.model.EventModels;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

/**
 * Created by gayatri on 25/4/17.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<EventModels> mAboutusModelArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView start_date,start_time,event,end_date,end_time;
        LinearLayout layoutList,lineLinear;
        ImageView eventBg;
        public MyViewHolder(View view) {
            super(view);

            start_date = (TextView) view.findViewById(R.id.start_date);
            start_time= (TextView) view.findViewById(R.id.start_time);
            end_date= (TextView) view.findViewById(R.id.end_date);
            end_time= (TextView) view.findViewById(R.id.end_time);
            event= (TextView) view.findViewById(R.id.event);
            eventBg= (ImageView) view.findViewById(R.id.eventBg);

        }
    }


    public EventListAdapter(Context mContext, ArrayList<EventModels> mAboutusModelArrayList) {
        this.mContext = mContext;
        this.mAboutusModelArrayList = mAboutusModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_event_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.eventBg.setBackgroundResource(R.drawable.evnticon);
        }else{
            holder.eventBg.setBackgroundResource(R.drawable.evnticonisg);

        }
        holder.end_date.setText(AppUtilityMethods.separateDate(mAboutusModelArrayList.get(position).getEnd_date()));
        holder.end_time.setText(AppUtilityMethods.separateTime(mAboutusModelArrayList.get(position).getEnd_date()).replace(".", ""));

        holder.start_date.setText(AppUtilityMethods.separateDate(mAboutusModelArrayList.get(position).getStart_date()));
        holder.start_time.setText(AppUtilityMethods.separateTime(mAboutusModelArrayList.get(position).getStart_date()).replace(".",""));
        holder.event.setText(mAboutusModelArrayList.get(position).getName());
    }



    @Override
    public int getItemCount() {
        return mAboutusModelArrayList.size();
    }
}
