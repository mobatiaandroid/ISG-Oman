package com.algubra.activity.homework.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.homework.model.HistoryModel;
import com.algubra.activity.homework.model.HomeWorkListModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeWorkHistoryListAdapter extends RecyclerView.Adapter<HomeWorkHistoryListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HistoryModel> mWorksheetList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView historyTitleTxt;
        RelativeLayout listBackGround;
        ImageView statusImg;

        public MyViewHolder(View view)
        {
            super(view);

            historyTitleTxt= (TextView) view.findViewById(R.id.historyTitleTxt);
            statusImg= (ImageView) view.findViewById(R.id.statusImg);
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
        }
    }


    public HomeWorkHistoryListAdapter(Context mContext, ArrayList<HistoryModel> mWorksheetList) {
        this.mContext = mContext;
        this.mWorksheetList = mWorksheetList;
    }

    @Override
    public HomeWorkHistoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_homework_history_list, parent, false);

        return new HomeWorkHistoryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeWorkHistoryListAdapter.MyViewHolder holder, final int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
        }
        holder.historyTitleTxt.setText(mWorksheetList.get(position).getTitle());
        if (mWorksheetList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.statusImg.setImageResource(R.drawable.homework_pending);
        }
        else if (mWorksheetList.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.statusImg.setImageResource(R.drawable.homework_redo);
        }
        else if (mWorksheetList.get(position).getStatus().equalsIgnoreCase("3"))
        {
            holder.statusImg.setImageResource(R.drawable.homework_done);
        }

    }


    @Override
    public int getItemCount() {
        return mWorksheetList.size();
    }


}
