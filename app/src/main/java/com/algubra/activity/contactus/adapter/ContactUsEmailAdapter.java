package com.algubra.activity.contactus.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.contactus.model.ContactUsModel;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by user2 on 5/5/17.
 */
public class ContactUsEmailAdapter extends RecyclerView.Adapter<ContactUsEmailAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ContactUsModel> mContactUsModelList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;
        TextView manageradminValue;
      LinearLayout manageadminLayout;
      LinearLayout emailLinear;
        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.manageradminTitle);
            manageradminValue = (TextView) view.findViewById(R.id.manageradminValue);
            manageadminLayout = (LinearLayout) view.findViewById(R.id.manageadminLayout);
            emailLinear = (LinearLayout) view.findViewById(R.id.emailLinear);

        }
    }


    public ContactUsEmailAdapter(Context mContext, ArrayList<ContactUsModel> contactUsList) {
        this.mContext = mContext;
        this.mContactUsModelList = contactUsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_email_contact, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.white));
//            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.manageadminLayout.setBackgroundColor(mContext.getResources().getColor(R.color.login_button_bg));
//            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.manageadminLayout.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));

//            holder.v.setBackgroundColor(mContext.getResources().getColor(R.color.isg_int_blue));
//            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
        holder.mTitleTxt.setText(mContactUsModelList.get(position).getName());
        holder.manageradminValue.setText(mContactUsModelList.get(position).getEmail());
        holder.emailLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                                Intent emailIntent = new Intent(
//                        Intent.ACTION_SEND_MULTIPLE);
//                String[] deliveryAddress = {mContactUsModelList.get(position).getEmail()};
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                emailIntent.setType("text/plain");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(
//                        emailIntent, 0);
//                System.out.println("packge size" + activityList.size());
//                for (final ResolveInfo app : activityList) {
//                    System.out.println("packge name" + app.activityInfo.name);
//                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(
//                                activity.applicationInfo.packageName, activity.name);
//                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        emailIntent.setComponent(name);
//                        v.getContext().startActivity(emailIntent);
//                        break;
//                    }
//                }
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", mContactUsModelList.get(position).getEmail(), null));
                mContext.startActivity(Intent.createChooser(emailIntent, "Send Email:"));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mContactUsModelList.size();
    }
}
