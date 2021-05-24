package com.algubra.activity.lostandfound.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.lostandfound.model.LostandFoundModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by user2 on 5/5/17.
 */
public class LostandFoundListAdapter  extends RecyclerView.Adapter<LostandFoundListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<LostandFoundModel> mLostFoundList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;
        //,listTxtDate
        ImageView imgIcon;
        View v;
        RelativeLayout listBackGround;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            //listTxtDate= (TextView) view.findViewById(R.id.listTxtDate);
            imgIcon=(ImageView) view.findViewById(R.id.imagicon);
            v=view.findViewById(R.id.view);
            v.setVisibility(View.GONE);

            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public LostandFoundListAdapter(Context mContext,ArrayList<LostandFoundModel> mLostFoundList) {
        this.mContext = mContext;
        this.mLostFoundList = mLostFoundList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lost_found_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("Schooll---"+ AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            //holder.listTxtDate.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
            //holder.listTxtDate.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
        }
        holder.mTitleTxt.setText(mLostFoundList.get(position).getTitle());
        //holder.listTxtDate.setText(mLostFoundList.get(position).get);
    }


    @Override
    public int getItemCount() {
        return mLostFoundList.size();
    }
}
