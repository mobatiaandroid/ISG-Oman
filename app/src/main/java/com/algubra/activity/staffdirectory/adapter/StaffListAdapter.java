package com.algubra.activity.staffdirectory.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.staffdirectory.StaffListActivity;
import com.algubra.activity.staffdirectory.model.StaffModel;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppUtilityMethods;

import java.util.ArrayList;

/**
 * Created by gayatri on 28/4/17.
 */
public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.MyViewHolder> implements URLConstants, JsonTagConstants, StausCodes {

    private Context mContext;
    private ArrayList<StaffModel> mStaffList;
    String title;
    Dialog dialog;
    EditText text_dialog, text_content;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView staffName, staffType, staffDescription, staffClassDiv;
        ImageView staffImg, callIcon, sendEmail;

        public MyViewHolder(View view) {
            super(view);

            staffName = (TextView) view.findViewById(R.id.staffName);
            staffType = (TextView) view.findViewById(R.id.staffType);
            staffClassDiv = (TextView) view.findViewById(R.id.staffClassDiv);
            staffDescription = (TextView) view.findViewById(R.id.staffDescription);
            staffImg = (ImageView) view.findViewById(R.id.staffImg);
            callIcon = (ImageView) view.findViewById(R.id.callicon);
            sendEmail = (ImageView) view.findViewById(R.id.sendMail);
        }
    }


    public StaffListAdapter(Context mContext, ArrayList<StaffModel> mStaffList, String title) {
        this.mContext = mContext;
        this.mStaffList = mStaffList;
        this.title = title;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_staffdirectorylistitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.staffName.setText(mStaffList.get(position).getStaff_name());
        if (mStaffList.get(position).getStaff_subject().equalsIgnoreCase("")) {
            holder.staffType.setVisibility(View.GONE);
        } else {
            holder.staffType.setText(mStaffList.get(position).getStaff_subject());

        }

        if ((!(mStaffList.get(position).getStaff_class().equalsIgnoreCase(""))) && (!mStaffList.get(position).getStaff_section().equalsIgnoreCase(""))) {
            holder.staffClassDiv.setText(mStaffList.get(position).getStaff_class() + " " + mStaffList.get(position).getStaff_section());
        } else if ((!mStaffList.get(position).getStaff_class().equalsIgnoreCase("")) && mStaffList.get(position).getStaff_section().equalsIgnoreCase("")) {
            holder.staffClassDiv.setText(mStaffList.get(position).getStaff_class());
        } else if ((!mStaffList.get(position).getStaff_section().equalsIgnoreCase("")) && mStaffList.get(position).getStaff_class().equalsIgnoreCase("")) {
            holder.staffClassDiv.setText(mStaffList.get(position).getStaff_section());
        } else {
            holder.staffClassDiv.setVisibility(View.GONE);

        }

        if ((mStaffList.get(position).getStaff_about().equalsIgnoreCase(""))) {
            holder.staffDescription.setText("");

        } else {
            holder.staffDescription.setText(mStaffList.get(position).getStaff_about());

        }
        holder.staffImg.setVisibility(View.GONE);
        /*Picasso.with(mContext)
                .load("http://static.wixstatic.com/media/10b8b8_5495d24d8e9347ec9afcefde3f156903~mv2.png")
                .into(holder.staffImg);*/
        // Glide.with(mContext).load("http://static.wixstatic.com/media/10b8b8_5495d24d8e9347ec9afcefde3f156903~mv2.png").into(holder.staffImg);
        if (title.equalsIgnoreCase("Teaching")) {
            holder.callIcon.setVisibility(View.GONE);

            if (mStaffList.get(position).getStaff_email().equalsIgnoreCase(""))
            {
                holder.sendEmail.setVisibility(View.INVISIBLE);

            }
            else {
                holder.sendEmail.setVisibility(View.VISIBLE);
            }
//            if (mStaffList.get(position).getStaff_phone().equalsIgnoreCase(""))
//            {
//                holder.callIcon.setVisibility(View.INVISIBLE);
//
//            }
//            else {
//                holder.callIcon.setVisibility(View.VISIBLE);
//            }
        } else {
            holder.sendEmail.setVisibility(View.GONE);

            if (mStaffList.get(position).getStaff_phone().equalsIgnoreCase(""))
            {
                holder.callIcon.setVisibility(View.INVISIBLE);

            }
            else {
                holder.callIcon.setVisibility(View.VISIBLE);
            }

//            if (mStaffList.get(position).getStaff_email().equalsIgnoreCase(""))
//            {
//                holder.sendEmail.setVisibility(View.INVISIBLE);
//
//            }
//            else {
//                holder.sendEmail.setVisibility(View.VISIBLE);
//            }
        }
        holder.callIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStaffList.get(position).getStaff_phone().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.phone_not_found), R.drawable.exclamationicon, R.drawable.roundred);

                } else if (Build.VERSION.SDK_INT < 23) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mStaffList.get(position).getStaff_phone()));
                    mContext.startActivity(intent);
                }
                else
                {

                    StaffListActivity.proceedAfterPermissions(mStaffList.get(position).getStaff_phone());


                }
            }
        });
        holder.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click on mail--");

                   /* dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.layout_sendemail_staff);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                    Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
                    text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
                    text_content = (EditText) dialog.findViewById(R.id.text);
                    // text_dialog.setSelection(0);
                    //text_content.setSelection(0);


                    dialogCancelButton.setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            dialog.dismiss();

                        }

                    });

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("submit btn clicked");

                            if (text_dialog.getText().equals("")) {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter your subject", R.drawable.exclamationicon,  R.drawable.roundblue);

                            } else if (text_content.getText().toString().equals("")) {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter your content", R.drawable.exclamationicon,  R.drawable.roundblue);

                            } else {
                                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                                    //sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                                } else {
                                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }

                        }
                    });

                    dialog.show();*/

                if (mStaffList.get(position).getStaff_email().equalsIgnoreCase("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.email_not_found), R.drawable.exclamationicon, R.drawable.roundred);

                }
                else {
//                    Intent emailIntent = new Intent(
//                            Intent.ACTION_SEND_MULTIPLE);
//                    String[] deliveryAddress = {mStaffList.get(position).getStaff_email()};
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
//                    emailIntent.setType("text/plain");
//                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                    PackageManager pm = v.getContext().getPackageManager();
//                    List<ResolveInfo> activityList = pm.queryIntentActivities(
//                            emailIntent, 0);
//                    System.out.println("packge size" + activityList.size());
//                    for (final ResolveInfo app : activityList) {
//                        System.out.println("packge name" + app.activityInfo.name);
//                        if ((app.activityInfo.name).contains("com.google.android.gm")) {
//                            final ActivityInfo activity = app.activityInfo;
//                            final ComponentName name = new ComponentName(
//                                    activity.applicationInfo.packageName, activity.name);
//                            emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                            emailIntent.setComponent(name);
//                            v.getContext().startActivity(emailIntent);
//                            break;
//                        }
//                    }
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",mStaffList.get(position).getStaff_email(), null));
                    mContext.startActivity(Intent.createChooser(emailIntent, "Send Email:"));

                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

}