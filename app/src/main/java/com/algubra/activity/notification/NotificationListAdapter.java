package com.algubra.activity.notification;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NotificationModel> mNotiSpecialMessageModels;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtClass;
        ImageView imgIcon;
        View v;
        RelativeLayout listBackGround;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtClass= (TextView) view.findViewById(R.id.listTxtDate);
            imgIcon=(ImageView) view.findViewById(R.id.imagicon);
            v=view.findViewById(R.id.view);
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public NotificationListAdapter(Context mContext,ArrayList<NotificationModel> mNotiSpecialMessageModels) {
        this.mContext = mContext;
        this.mNotiSpecialMessageModels = mNotiSpecialMessageModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
           // holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
            if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("t")){

                holder.imgIcon.setBackgroundResource(R.drawable.text);
            }else if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("i")){
                holder.imgIcon.setBackgroundResource(R.drawable.image);
            }else if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("v")){
                holder.imgIcon.setBackgroundResource(R.drawable.videoi);
            }else if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("a")){
                holder.imgIcon.setBackgroundResource(R.drawable.audio);
            }
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            //holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
            if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("t")){

                holder.imgIcon.setBackgroundResource(R.drawable.textblue);
            }else if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("i")){
                holder.imgIcon.setBackgroundResource(R.drawable.imageblue);
            }else if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("v")){
                holder.imgIcon.setBackgroundResource(R.drawable.videoblue);
            }else if(mNotiSpecialMessageModels.get(position).getAlert_type().equals("a")){
                holder.imgIcon.setBackgroundResource(R.drawable.audioblue);
            }
        }


        holder.mTitleTxt.setText(mNotiSpecialMessageModels.get(position).getMessage());
        holder.listTxtClass.setText(mNotiSpecialMessageModels.get(position).getCreated_time());
    }


    @Override
    public int getItemCount() {
        return mNotiSpecialMessageModels.size();
    }
}

