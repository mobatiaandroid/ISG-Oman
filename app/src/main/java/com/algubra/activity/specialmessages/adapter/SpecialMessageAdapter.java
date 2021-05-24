package com.algubra.activity.specialmessages.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.specialmessages.model.SpecialMessageModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

/**
 * Created by gayatri on 4/5/17.
 */
public class SpecialMessageAdapter extends RecyclerView.Adapter<SpecialMessageAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SpecialMessageModel> mSpecialMessageModelList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtDate;
        ImageView imgIcon;
        View v;
        RelativeLayout listBackGround;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            imgIcon=(ImageView) view.findViewById(R.id.imagicon);
            listTxtDate= (TextView) view.findViewById(R.id.listTxtDate);
            v=view.findViewById(R.id.view);
            v.setVisibility(View.GONE);

            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public SpecialMessageAdapter(Context mContext,ArrayList<SpecialMessageModel> mSpecialMessageModelList) {
        this.mContext = mContext;
        this.mSpecialMessageModelList = mSpecialMessageModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_specialmessages_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            //holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
            holder.imgIcon.setImageResource(R.drawable.chatgreen);
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
           // holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.imgIcon.setImageResource(R.drawable.chatblue);

            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(mSpecialMessageModelList.get(position).getMessage());
        holder.listTxtDate.setText(AppUtilityMethods.dateParsingToDdMmmYyyy(mSpecialMessageModelList.get(position).getTime()));
    }


    @Override
    public int getItemCount() {
        return mSpecialMessageModelList.size();
    }
}
