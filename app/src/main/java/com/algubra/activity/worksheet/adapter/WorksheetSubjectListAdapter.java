package com.algubra.activity.worksheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.circulars.adapter.CircularAdapter;
import com.algubra.activity.circulars.model.CircularModel;
import com.algubra.activity.worksheet.model.WorksheetSubjectModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

public class  WorksheetSubjectListAdapter extends RecyclerView.Adapter<WorksheetSubjectListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<WorksheetSubjectModel> mWorksheetSubjectList;
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


    public WorksheetSubjectListAdapter(Context mContext,ArrayList<WorksheetSubjectModel> mWorksheetSubjectList) {
        this.mContext = mContext;
        this.mWorksheetSubjectList = mWorksheetSubjectList;
    }

    @Override
    public WorksheetSubjectListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_worksheet_subject, parent, false);

        return new WorksheetSubjectListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WorksheetSubjectListAdapter.MyViewHolder holder, int position) {
        System.out.println("Schooll---"+ AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(mWorksheetSubjectList.get(position).getSubjectName());
        holder.listTxtDate.setVisibility(View.INVISIBLE);
//        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList.get(position).getCircularDate())+" "+AppUtilityMethods.separateTime(mTimeTableList.get(position).getCircularDate()).replaceAll(".", ""));
    }


    @Override
    public int getItemCount() {
        return mWorksheetSubjectList.size();
    }
}
