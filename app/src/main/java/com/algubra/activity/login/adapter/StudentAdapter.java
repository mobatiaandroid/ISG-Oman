package com.algubra.activity.login.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.login.model.StudentModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gayatri on 26/4/17.
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentModels> mStudentList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtClass;
        ImageView imgIcon;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtClass= (TextView) view.findViewById(R.id.listTxtClass);
            imgIcon=(ImageView) view.findViewById(R.id.imagicon);
        }
    }


    public StudentAdapter(Context mContext,ArrayList<StudentModels> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_student_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTitleTxt.setText(mStudentList.get(position).getName());
        holder.imgIcon.setVisibility(View.VISIBLE);
        if(!mStudentList.get(position).getStudent_photo().equals("")) {
            Picasso.with(mContext).load(mStudentList.get(position).getStudent_photo().replaceAll(" ", "%20")).fit()
                    .placeholder(R.drawable.noimage)
                    .into(holder.imgIcon);
        }

        holder.listTxtClass.setText(mStudentList.get(position).getClass_name()+" "+mStudentList.get(position).getStudent_division_name());

    }


    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
