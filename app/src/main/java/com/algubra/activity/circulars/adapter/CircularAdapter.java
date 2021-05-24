package com.algubra.activity.circulars.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.circulars.model.CircularModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

/**
 * Created by gayatri on 17/5/17.
 */
public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CircularModel> mTimeTableList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtDate;
        ImageView imgIcon;
        RelativeLayout listBackGround;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtDate= (TextView) view.findViewById(R.id.listTxtDate);


            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public CircularAdapter(Context mContext,ArrayList<CircularModel> timeTableList) {
        this.mContext = mContext;
        this.mTimeTableList = timeTableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_circular, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("Schooll---"+ AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(mTimeTableList.get(position).getTitle());
        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList.get(position).getCircularDate()));
//        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList.get(position).getCircularDate())+" "+AppUtilityMethods.separateTime(mTimeTableList.get(position).getCircularDate()).replaceAll(".", ""));
    }


    @Override
    public int getItemCount() {
        return mTimeTableList.size();
    }
}
