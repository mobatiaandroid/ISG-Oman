package com.algubra.activity.homework.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.algubra.R;
import com.algubra.activity.homework.HomeWorkListDetailActivity;
import com.algubra.activity.homework.model.HistoryModel;
import com.algubra.activity.pdf.PDFTest;
import com.algubra.activity.pdf.PdfReaderActivity;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

public class HomeWorkHistoryListAdapterNew extends RecyclerView.Adapter<HomeWorkHistoryListAdapterNew.MyViewHolder> {

    private Context mContext;
    private ArrayList<HistoryModel> mWorksheetList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionHistory,fileNameHistoryTxt,statusHistoryTxt;
        RelativeLayout listBackGround;
        ImageView fileLogoHistory,downloadHistoryLinear,shareHistoryLinear;
         LinearLayout clickHistoryLinear;
        public MyViewHolder(View view)
        {
            super(view);

            descriptionHistory= (TextView) view.findViewById(R.id.descriptionHistory);
            fileNameHistoryTxt= (TextView) view.findViewById(R.id.fileNameHistoryTxt);
            statusHistoryTxt= (TextView) view.findViewById(R.id.statusHistoryTxt);
            fileLogoHistory= (ImageView) view.findViewById(R.id.fileLogoHistory);
            downloadHistoryLinear= (ImageView) view.findViewById(R.id.downloadHistoryLinear);
            shareHistoryLinear= (ImageView) view.findViewById(R.id.shareHistoryLinear);
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
            clickHistoryLinear= (LinearLayout) view.findViewById(R.id.clickHistoryLinear);
        }
    }


    public HomeWorkHistoryListAdapterNew(Context mContext, ArrayList<HistoryModel> mWorksheetList) {
        this.mContext = mContext;
        this.mWorksheetList = mWorksheetList;
    }

    @Override
    public HomeWorkHistoryListAdapterNew.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_homework_history_list_new, parent, false);

        return new HomeWorkHistoryListAdapterNew.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeWorkHistoryListAdapterNew.MyViewHolder holder, final int position) {
        System.out.println("Schooll---" + AppPreferenceManager.getSchoolSelection(mContext));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
        }
        holder.fileNameHistoryTxt.setText(mWorksheetList.get(position).getTitle());
        if (mWorksheetList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.statusHistoryTxt.setText("Submitted");
           // holder.statusImg.setImageResource(R.drawable.homework_pending);
        }
        else if (mWorksheetList.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.statusHistoryTxt.setText("Redo");
           // holder.statusImg.setImageResource(R.drawable.homework_redo);
        }
        else if (mWorksheetList.get(position).getStatus().equalsIgnoreCase("3"))
        {
            holder.statusHistoryTxt.setText("Approved");
           // holder.statusImg.setImageResource(R.drawable.homework_done);
        }
        if (mWorksheetList.get(position).getType().equalsIgnoreCase("ppt")||mWorksheetList.get(position).getType().equalsIgnoreCase("pptx"))
        {
            holder.fileLogoHistory.setImageResource(R.drawable.powerpoint);
        }
        else if (mWorksheetList.get(position).getType().equalsIgnoreCase("xlxs")||mWorksheetList.get(position).getType().equalsIgnoreCase("xlx"))

        {
            holder.fileLogoHistory.setImageResource(R.drawable.excel);
        }
        else if (mWorksheetList.get(position).getType().equalsIgnoreCase("jpg")||mWorksheetList.get(position).getType().equalsIgnoreCase("png")||mWorksheetList.get(position).getType().equalsIgnoreCase("jpeg")||mWorksheetList.get(position).getType().equalsIgnoreCase("JPG")||mWorksheetList.get(position).getType().equalsIgnoreCase("JPEG")||mWorksheetList.get(position).getType().equalsIgnoreCase("PNG"))
        {
            holder.fileLogoHistory.setImageResource(R.drawable.image);
        }
        else if (mWorksheetList.get(position).getType().equalsIgnoreCase("doc")||mWorksheetList.get(position).getType().equalsIgnoreCase("docx"))
        {
            holder.fileLogoHistory.setImageResource(R.drawable.word);
        }
        else if (mWorksheetList.get(position).getType().equalsIgnoreCase("pdf"))
        {
            holder.fileLogoHistory.setImageResource(R.drawable.pdfimg);
        }
        holder.clickHistoryLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWorksheetList.get(position).getType().equalsIgnoreCase("pdf"))
                {
                    Intent browserIntent = new Intent(mContext, PDFTest.class);
                    browserIntent.putExtra("pdf_url", mWorksheetList.get(position).getFile()); // url
                    System.out.println("url print"+mWorksheetList.get(position).getFile());
                    browserIntent.putExtra("title", mWorksheetList.get(position).getTitle());  // name
                    browserIntent.putExtra("filename", mWorksheetList.get(position).getFile()); //name
                    mContext.startActivity(browserIntent);
                }else {
                    Intent intent = new Intent(mContext, PdfReaderActivity.class);
                    intent.putExtra("pdf_url",mWorksheetList.get(position).getFile());
                    intent.putExtra("title",mWorksheetList.get(position).getTitle());
                    intent.putExtra("type",mWorksheetList.get(position).getType());
                    mContext.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mWorksheetList.size();
    }


}
