package com.algubra.activity.leaves.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.leaves.model.LeavesModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;


public class LeavesAdapter  extends RecyclerView.Adapter<LeavesAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<LeavesModel> mLeavesList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView listDate,listStatus;
        ImageView imgIcon;
        View v;
        RelativeLayout listBackGround;

        public MyViewHolder(View view) {
            super(view);

            //listName= (TextView) view.findViewById(R.id.listName);
            listDate= (TextView) view.findViewById(R.id.listDate);
            listStatus= (TextView) view.findViewById(R.id.listStatus);
            v=view.findViewById(R.id.view);
            v.setVisibility(View.GONE);

            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public LeavesAdapter(Context mContext,ArrayList<LeavesModel> timeTableList) {
        this.mContext = mContext;
        this.mLeavesList = timeTableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_leave_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
           // holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            //holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
         if(mLeavesList.get(position).getTo_date().equals(mLeavesList.get(position).getFrom_date())){
            holder.listDate.setText(AppUtilityMethods.dateParsingToDdmmYyyy(mLeavesList.get(position).getFrom_date()));
        }else{
             holder.listDate.setText(Html.fromHtml(AppUtilityMethods.dateParsingToDdmmYyyy(mLeavesList.get(position).getFrom_date()) + " to " +
                     AppUtilityMethods.dateParsingToDdmmYyyy(mLeavesList.get(position).getTo_date())));
         }
        //holder.listName.setText(AppPreferenceManager.getStudentName(mContext));
        if(mLeavesList.get(position).getStatus().equals("1")){
            holder.listStatus.setText("Approved");
            holder.listStatus.setTextColor(mContext.getResources().getColor(R.color.black));

        }else if(mLeavesList.get(position).getStatus().equals("2")){
            holder.listStatus.setText("Pending");
            holder.listStatus.setTextColor(mContext.getResources().getColor(R.color.red));


        }else if(mLeavesList.get(position).getStatus().equals("3")){
            holder.listStatus.setText("Noted");
            holder.listStatus.setTextColor(mContext.getResources().getColor(R.color.newsletter_red));


        }
    }


    @Override
    public int getItemCount() {
        return mLeavesList.size();
    }
}
