package com.algubra.activity.staffdirectory.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.staffdirectory.model.StaffModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 2/5/17.
 */
public class StaffDirectoryAdapter extends RecyclerView.Adapter<StaffDirectoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StaffModel> mStaffList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;
        RelativeLayout layoutList;
        View v;
        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);
            layoutList= (RelativeLayout) view.findViewById(R.id.listBackGround);
            v=view.findViewById(R.id.view);
            v.setVisibility(View.GONE);

        }
    }


    public StaffDirectoryAdapter(Context mContext,ArrayList<StaffModel> mStaffList) {
        this.mContext = mContext;
        this.mStaffList = mStaffList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_settings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mTitleTxt.setText(mStaffList.get(position).getStaff_dept());
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.layoutList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.layoutList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }

    }


    @Override
    public int getItemCount() {
        return mStaffList.size();
    }
}