package com.algubra.activity.contactus.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.contactus.model.ContactUsModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by user2 on 5/5/17.
 */
public class ContactUsPhoneAdapter extends RecyclerView.Adapter<ContactUsPhoneAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ContactUsModel> mContactUsModelList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;
        TextView mainSchoolValue;
        ImageView callMainSchoolImageView;
        LinearLayout mainScLayout;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.mainSchoolTitle);
            mainSchoolValue = (TextView) view.findViewById(R.id.mainSchoolValue);
            callMainSchoolImageView = (ImageView) view.findViewById(R.id.callMainSchool);
            mainScLayout = (LinearLayout) view.findViewById(R.id.mainScLayout);

        }
    }


    public ContactUsPhoneAdapter(Context mContext, ArrayList<ContactUsModel> contactUsList) {
        this.mContext = mContext;
        this.mContactUsModelList = contactUsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_phone_contact, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.mainScLayout.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
//            holder.mainScLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.login_button_bg));
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.mainScLayout.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));

//            holder.mainScLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(mContactUsModelList.get(position).getName());
        holder.mainSchoolValue.setText(mContactUsModelList.get(position).getPhone());
        if (mContactUsModelList.get(position).getName().equalsIgnoreCase("Fax")) {
            holder.callMainSchoolImageView.setVisibility(View.INVISIBLE);
        } else {
            holder.callMainSchoolImageView.setVisibility(View.VISIBLE);

        }
        holder.callMainSchoolImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mContactUsModelList.get(position).getPhone()));

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mContactUsModelList.size();
    }
}
