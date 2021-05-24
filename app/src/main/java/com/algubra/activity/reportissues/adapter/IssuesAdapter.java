package com.algubra.activity.reportissues.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.reportissues.model.IssueModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by user2 on 4/5/17.
 */
public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<IssueModel> mSettingsList;
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
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public IssuesAdapter(Context mContext,ArrayList<IssueModel> mSettingsList) {
        this.mContext = mContext;
        this.mSettingsList = mSettingsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_settings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("Schooll---"+ AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.gradient_green_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.gradient_blue_isg_int));
        }
      holder.mTitleTxt.setText(mSettingsList.get(position).getIssue()+"  "+mSettingsList.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return mSettingsList.size();
    }
}
