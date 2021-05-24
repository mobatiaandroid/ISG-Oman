package com.algubra.activity.newsletter.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.newsletter.model.NewsLetterModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user2 on 4/5/17.
 */
public class NewsLetterAdapter  extends RecyclerView.Adapter<NewsLetterAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NewsLetterModel> NewsLetterList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtClass,createdDate,description;
        ImageView imgIcon;
        View v;
        RelativeLayout listBackGround;
        RelativeLayout img;
        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.title);
            imgIcon=(ImageView) view.findViewById(R.id.Img);
            v=view.findViewById(R.id.view);
            v.setVisibility(View.GONE);

            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
            img= (RelativeLayout) view.findViewById(R.id.img);
            createdDate= (TextView) view.findViewById(R.id.createdDate);
            description= (TextView) view.findViewById(R.id.description);
        }
    }


    public NewsLetterAdapter(Context mContext,ArrayList<NewsLetterModel> NewsLetterList) {
        this.mContext = mContext;
        this.NewsLetterList = NewsLetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_newsletter_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            //holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
           // holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(NewsLetterList.get(position).getTitle());
        holder.createdDate.setText(AppUtilityMethods.dateParsingToDdMmmYyyy(NewsLetterList.get(position).getNewsCreatedTime()).replace(".",""));
        holder.description.setText(NewsLetterList.get(position).getNewsDescription());
        /*Glide.with(mContext).load(NewsLetterList.get(position).getNewsImage())
        .placeholder(R.drawable.noimage)
                .into(holder.imgIcon);*/
        if(!NewsLetterList.get(position).getNewsImage().equals("")) {
            Picasso.with(mContext).load(NewsLetterList.get(position).getNewsImage().replaceAll(" ","%20")).fit().centerCrop()
                    .placeholder(R.drawable.noimage)
                    .into(holder.imgIcon);
            holder.img.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return NewsLetterList.size();
    }
}
