package com.algubra.activity.studentprofile.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.adapter.HomeViewPagerAdapter;
import com.algubra.activity.login.model.StudentModels;
import com.algubra.activity.studentprofile.StudentProfileListActivity;
import com.algubra.appcontroller.AppController;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gayatri on 17/5/17.
 */
public class StudentProfileAdapter extends RecyclerView.Adapter<StudentProfileAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentModels> studentModelsArrayList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout stlayout;
        LinearLayout btnLayout;
        TextView studentName, grNoValue, dobValue, houseValue, mentorName, elect1Name, elect2Name, studentClass, textClassTeacher, textBoard;
        //fatherName,motherName,
        ImageView image;
        ImageView sendMail;
        TextView verifyIcon;
        Button selectStudentButton;
        LinearLayout llMail;
        LinearLayout studentDetailLinear;

        public MyViewHolder(View view) {
            super(view);

            studentName = (TextView) view.findViewById(R.id.studentName);
            textBoard = (TextView) view.findViewById(R.id.textBoard);
            grNoValue = (TextView) view.findViewById(R.id.grNoValue);
            dobValue = (TextView) view.findViewById(R.id.dobValue);
            houseValue = (TextView) view.findViewById(R.id.houseValue);
            studentClass = (TextView) view.findViewById(R.id.studentClass);
            textClassTeacher = (TextView) view.findViewById(R.id.textClassTeacher);
            llMail = (LinearLayout) view.findViewById(R.id.llMail);
            // fatherName= (TextView) view.findViewById(R.id.fatherName);
            // motherName= (TextView) view.findViewById(R.id.motherName);
            mentorName = (TextView) view.findViewById(R.id.mentorName);
            elect1Name = (TextView) view.findViewById(R.id.elect1Name);
            elect2Name = (TextView) view.findViewById(R.id.elect2Name);
            selectStudentButton = (Button) view.findViewById(R.id.selectStudentButton);
            stlayout = (RelativeLayout) view.findViewById(R.id.stlayout);
            btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
            studentDetailLinear = (LinearLayout) view.findViewById(R.id.studentDetailLinear);
            image = (ImageView) view.findViewById(R.id.image);
            verifyIcon = (TextView) view.findViewById(R.id.verifyIcon);
            sendMail = (ImageView) view.findViewById(R.id.sendMail);
        }
    }

    public StudentProfileAdapter(Context mContext, ArrayList<StudentModels> studentModelsArrayList) {
        this.mContext = mContext;
        this.studentModelsArrayList = studentModelsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_profile_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            holder.stlayout.setBackgroundResource(R.color.login_button_bg);
//            holder.stlayout.getBackground().setAlpha(150);
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            holder.stlayout.setBackgroundResource(R.color.isg_int_blue);
//            holder.stlayout.getBackground().setAlpha(150);
        }
        holder.studentName.setText(studentModelsArrayList.get(position).getName());
        holder.grNoValue.setText(AppUtilityMethods.stringFromBase64Encoded(studentModelsArrayList.get(position).getStudent_gr_no()));
        holder.dobValue.setText(AppUtilityMethods.dateConversionStandardFormat(studentModelsArrayList.get(position).getDob()));
        holder.houseValue.setText(studentModelsArrayList.get(position).getHouse());
        holder.studentClass.setText(studentModelsArrayList.get(position).getClass_name()+" "+studentModelsArrayList.get(position).getStudent_division_name());
        // holder.fatherName.setText(studentModelsArrayList.get(position).getFather());
        // holder.motherName.setText(studentModelsArrayList.get(position).getMother());
        holder.textClassTeacher.setText(studentModelsArrayList.get(position).getTeacher());
        holder.mentorName.setText(studentModelsArrayList.get(position).getMentor());
        holder.elect1Name.setText(studentModelsArrayList.get(position).getElect1());
        holder.elect2Name.setText(studentModelsArrayList.get(position).getElect2());
        if (studentModelsArrayList.get(position).getBoardid().equals("1")) {
            String selectedFromList = "ISG";
            holder.textBoard.setText(selectedFromList);
        } else {
            String selectedFromList = "ISG-INT";
            holder.textBoard.setText(selectedFromList);
        }

        holder.sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!studentModelsArrayList.get(position).getTeacher_email().equalsIgnoreCase("")) {
//                    Intent emailIntent = new Intent(
//                            Intent.ACTION_SEND_MULTIPLE);
//                    String[] deliveryAddress = {studentModelsArrayList.get(position).getTeacher_email()};
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
                            "mailto",studentModelsArrayList.get(position).getTeacher_email(), null));
                    mContext.startActivity(Intent.createChooser(emailIntent, "Send Email:"));

                }
                else
                {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading),"Email Id not available.", R.drawable.infoicon,  R.drawable.roundred);

                }
            }
        });
       /* Glide.with(mContext).load(studentModelsArrayList.get(position).getStudent_photo())
                .placeholder(R.drawable.boy)
                .into(holder.image);*/
        if (studentModelsArrayList.get(position).getMapStatusVerify().equalsIgnoreCase("0")) {
            holder.verifyIcon.setVisibility(View.VISIBLE);
            holder.studentDetailLinear.setVisibility(View.GONE);
            holder.studentClass.setVisibility(View.GONE);
        } else {
            holder.verifyIcon.setVisibility(View.GONE);
            holder.studentClass.setVisibility(View.VISIBLE);
            holder.studentDetailLinear.setVisibility(View.VISIBLE);
        }
        holder.verifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.mapId=studentModelsArrayList.get(position).getMapId();
                StudentProfileListActivity.showVerifyDialogAdapterClick();
            }
        });
        if (studentModelsArrayList.get(position).getTeacher_email().equalsIgnoreCase("")) {
            holder.sendMail.setVisibility(View.GONE);
            holder.llMail.setVisibility(View.VISIBLE);
        } else {
            holder.llMail.setVisibility(View.VISIBLE);

        }
        if (!studentModelsArrayList.get(position).getStudent_photo().equals("")) {
            Picasso.with(mContext).load(studentModelsArrayList.get(position).getStudent_photo()).fit()
                    .into(holder.image);
        }
       /* Picasso.with(mContext).load(studentModelsArrayList.get(position).getStudent_photo()).fit()
                .placeholder(R.drawable.boy)
                .error(R.drawable.boy)
                .into(holder.image);*/
        if (AppPreferenceManager.getStudentId(mContext).equals(studentModelsArrayList.get(position).getId())) {
            //holder.btnLayout.setBackgroundResource(R.drawable.rounded_corner_loginedittextbg);
            holder.selectStudentButton.setBackgroundResource(R.drawable.event_attend_drwable_btn);
            //holder.selectStudentButton.setBackgroundResource(R.color.white);
            holder.selectStudentButton.setText("SELECTED");
        } else {

            holder.selectStudentButton.setBackgroundResource(R.drawable.edittext_bg_contactstaff);
            holder.selectStudentButton.setText("SELECT");
            holder.selectStudentButton.setTextColor(mContext.getResources().getColor(R.color.login_hint_textcolor));
        }

        holder.selectStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.btnLayout.setBackgroundResource(R.drawable.rounded_corner_loginedittextbg);
                /*holder.selectStudentButton.setBackgroundResource(R.drawable.event_attend_drwable_btn);
                holder.selectStudentButton.setText("SELECTED");*/
                if (holder.selectStudentButton.getText().toString().equals("SELECTED")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", "Student already selected.", R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    showDialog((Activity) mContext, "Alert", "Do you want to select  " + studentModelsArrayList.get(position).getName()+"?", R.drawable.question, R.drawable.roundblue, position);
                }
               /* AppPreferenceManager.setStudentId(mContext, studentModelsArrayList.get(position).getId());
                AppPreferenceManager.setStudentName(mContext,studentModelsArrayList.get(position).getName());
                //Intent intent=new Intent(mContext)
                Intent intent = ((Activity) mContext).getIntent();

                ((Activity) mContext).setResult(((Activity) mContext).RESULT_OK,
                        intent);
                ((Activity) mContext).finish();
            mContext.startActivity(intent);*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return studentModelsArrayList.size();
    }

    /*public interface MyTestInterface {

        void testFunctionOne();


    }*/

    public void showDialog(final Activity activity, String msgHead, String msg, int ico, int bgIcon, final int pos) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_switch_dialog);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_signup);
        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_maybelater);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppPreferenceManager.setStudentId(mContext, studentModelsArrayList.get(pos).getId());
                AppPreferenceManager.setStudentName(mContext, studentModelsArrayList.get(pos).getName());
                AppPreferenceManager.setStudentClassName(mContext, studentModelsArrayList.get(pos).getClass_name());
                AppPreferenceManager.setStudentSectionName(mContext, studentModelsArrayList.get(pos).getStudent_division_name());
                if (studentModelsArrayList.get(pos).getBoardid().equals("1")) {
                    String selectedFromList = "ISG";
                    AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                } else {
                    String selectedFromList = "ISG-INT";
                    AppPreferenceManager.setSchoolSelection(mContext, selectedFromList);
                }                //Intent intent=new Intent(mContext)
                HomeViewPagerAdapter.userName.setText(AppPreferenceManager.getStudentName(mContext) + "(" + AppPreferenceManager.getSchoolSelection(mContext) + ")");

                Intent intent = ((Activity) mContext).getIntent();

                ((Activity) mContext).setResult(((Activity) mContext).RESULT_OK,
                        intent);
                ((Activity) mContext).finish();
                mContext.startActivity(intent);
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }

}