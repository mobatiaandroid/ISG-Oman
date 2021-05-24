package com.algubra.activity.aboutus.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.algubra.R;
import com.algubra.activity.aboutus.model.AboutUsModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class AboutusRecyclerviewAdapter extends RecyclerView.Adapter<AboutusRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<AboutUsModel> mTimeTableList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtClass;
        ImageView imgIcon;
        View v;
        RelativeLayout listBackGround;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            imgIcon=(ImageView) view.findViewById(R.id.imagicon);
            v=view.findViewById(R.id.view);
            v.setVisibility(View.GONE);
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public AboutusRecyclerviewAdapter(Context mContext,ArrayList<AboutUsModel> timeTableList) {
        this.mContext = mContext;
        this.mTimeTableList = timeTableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_settings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(mTimeTableList.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return mTimeTableList.size();
    }
}
