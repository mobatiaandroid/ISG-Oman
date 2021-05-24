package com.algubra.activity.homework.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.algubra.activity.homework.OnBottomReachedListener;
import com.algubra.activity.homework.model.HomeWorkListModel;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.worksheet.model.WorksheetListModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HomeWorkListAdapter extends RecyclerView.Adapter<HomeWorkListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HomeWorkListModel> mWorksheetList;
    String dept;
    String name="";
    String typeExtention="";
    String url="";
    String  filenameshare="";
    OnBottomReachedListener onBottomReachedListener;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtDate;
        ImageView imgIcon,download,arrowImg,share;
        RelativeLayout listBackGround;
        LinearLayout operationLinear;

        public MyViewHolder(View view)
        {
            super(view);

            mTitleTxt= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtDate= (TextView) view.findViewById(R.id.listTxtDate);
            listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
            operationLinear= (LinearLayout) view.findViewById(R.id.operationLinear);
            download= (ImageView) view.findViewById(R.id.download);
            arrowImg= (ImageView) view.findViewById(R.id.arrowImg);
            share= (ImageView) view.findViewById(R.id.share);
        }
    }


    public HomeWorkListAdapter(Context mContext, ArrayList<HomeWorkListModel> mWorksheetList) {
        this.mContext = mContext;
        this.mWorksheetList = mWorksheetList;
    }

    @Override
    public HomeWorkListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_homework_list, parent, false);

        return new HomeWorkListAdapter.MyViewHolder(itemView);
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    @Override
    public void onBindViewHolder(final HomeWorkListAdapter.MyViewHolder holder, final int position) {
        if (position == mWorksheetList.size() - 1){

            onBottomReachedListener.onBottomReached(position);

        }
        System.out.println("Schooll---"+ AppPreferenceManager.getSchoolSelection(mContext));
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.login_button_bg));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg));
        }else if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")){
            holder.mTitleTxt.setTextColor(mContext.getResources().getColor(R.color.isg_int_blue));
            holder.listBackGround.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listbg_isg_int));
        }
//        if (mWorksheetList.get(position).getType().equalsIgnoreCase("pdf"))
//        {
//            holder.operationLinear.setVisibility(View.GONE);
//        }
//        else
//        {
//            holder.operationLinear.setVisibility(View.VISIBLE);
//        }

        holder.mTitleTxt.setText(mWorksheetList.get(position).getTitle());
        holder.listTxtDate.setVisibility(View.INVISIBLE);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                typeExtention=mWorksheetList.get(position).getType();
                url=mWorksheetList.get(position).getFile();
                name=mWorksheetList.get(position).getTitle();
                new HomeWorkListAdapter.DownloadPDF().execute();
            }
        });

      holder.arrowImg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (mWorksheetList.get(position).getOpen())
              {
                  holder.operationLinear.setVisibility(View.GONE);
                  mWorksheetList.get(position).setOpen(false);
              }
              else
              {
                  holder.operationLinear.setVisibility(View.VISIBLE);
                  mWorksheetList.get(position).setOpen(false);
              }
          }
      });
      holder.share.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
              name=mWorksheetList.get(position).getTitle();
              String filename = name.replace(" ", "_");
              filenameshare = filename + "."+typeExtention;
              // filenameshare=mWorksheetList.get(position).getTitle()+"."+mWorksheetList.get(position).getType();
              url=mWorksheetList.get(position).getFile();
              System.out.println("Working click");
            //  String filenameshare=mWorksheetList.get(position).getTitle()+"."+mWorksheetList.get(position).getType();
              if (AppUtilityMethods.isNetworkConnected(mContext))
              {
                  System.out.println("Working click!!!!!!!!");
                  Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                  File fileWithinMyDir = new File(getFilepath(filenameshare));
                  if(fileWithinMyDir.exists())
                  {
                      System.out.println("Working click@@@@@@@@@");
                      intentShareFile.setType("application/pdf");
                      intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
                      intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                      intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                      mContext.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                  }
                  else
                  {
                      if (AppUtilityMethods.isNetworkConnected(mContext)) {

                          new HomeWorkListAdapter.loadPDF().execute();
                          if(fileWithinMyDir.exists())
                          {
                              System.out.println("Working click@@@@@@@@@");
                              intentShareFile.setType("application/pdf");
                              intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getFilepath(filenameshare)));
                              intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                              intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                              mContext.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                          }
                      } else {
                          AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                      }
                      System.out.println("Working click!D");
                  }
              } else {
                  AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
              }
          }
      });
//        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList.get(position).getCircularDate())+" "+AppUtilityMethods.separateTime(mTimeTableList.get(position).getCircularDate()).replaceAll(".", ""));
    }
    private String getFilepath(String filename) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + filename).getPath();

    }

    @Override
    public int getItemCount() {
        return mWorksheetList.size();
    }
    public class DownloadPDF extends AsyncTask<String, Void, Void>
    {

        private Exception exception;
        private ProgressDialog dialog;
        String filename = name.replace(" ", "_");
        String fileName = filename + "."+typeExtention;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            System.out.println("filename"+fileName);
            try {

                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                 Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            System.out.println("file.exists() = " + file.exists());
            if (file.exists()) {
                Toast.makeText(mContext, "File Downloaded to download/"+fileName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public class loadPDF extends AsyncTask<String, Void, Void>
    {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(PDFViewActivity.this);
//            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
//            dialog.show();
        }

        protected Void doInBackground(String... urls)
        {
            URL u = null;
            try {
                String fileName = filenameshare;
                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            // System.out.println("file.exists() = " + file.exists());
            // pdf.fromUri(uri);

//            LoadingPdf(file);

            //pdf.fromFile(file).defaultPage(1).enableSwipe(true).load();


            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }

    }
}
