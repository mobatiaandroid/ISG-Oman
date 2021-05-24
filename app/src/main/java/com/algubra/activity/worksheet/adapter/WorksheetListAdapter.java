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
import com.algubra.activity.homework.OnBottomReachedListener;
import com.algubra.activity.worksheet.model.WorksheetListModel;
import com.algubra.activity.worksheet.model.WorksheetSubjectModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

public class WorksheetListAdapter extends RecyclerView.Adapter<WorksheetListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<WorksheetListModel> mWorksheetList;
    String dept;
    OnBottomReachedListener onBottomReachedListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtDate;
        ImageView imgIcon,download;
        RelativeLayout listBackGround;
        LinearLayout operationLinear;

        public MyViewHolder(View view)
        {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtDate= (TextView) view.findViewById(R.id.listTxtDate);
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
            operationLinear= (LinearLayout) view.findViewById(R.id.operationLinear);
            download= (ImageView) view.findViewById(R.id.download);
        }
    }


    public WorksheetListAdapter(Context mContext,ArrayList<WorksheetListModel> mWorksheetList) {
        this.mContext = mContext;
        this.mWorksheetList = mWorksheetList;
    }

    @Override
    public WorksheetListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_worksheet_list, parent, false);

        return new WorksheetListAdapter.MyViewHolder(itemView);
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    @Override
    public void onBindViewHolder(final WorksheetListAdapter.MyViewHolder holder, int position) {
        if (position == mWorksheetList.size() - 1){

            onBottomReachedListener.onBottomReached(position);

        }
        System.out.println("Schooll---"+ AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.operationLinear.setVisibility(View.GONE);
        holder.mTitleTxt.setText(mWorksheetList.get(position).getTitle());
        holder.listTxtDate.setVisibility(View.INVISIBLE);


//        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList.get(position).getCircularDate())+" "+AppUtilityMethods.separateTime(mTimeTableList.get(position).getCircularDate()).replaceAll(".", ""));
    }


    @Override
    public int getItemCount() {
        return mWorksheetList.size();
    }
}
