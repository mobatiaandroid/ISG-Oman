package com.algubra.activity.newsletter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.loadweburl.LoadWebViewActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.newsletter.model.NewsLetterModel;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PdfReader;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.manager.JustifiedTextView;
import com.algubra.volleymanager.CustomProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class NewsLetterDetailActivity  extends Activity {
    Bundle extras;
    private Context mContext=this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ArrayList<NewsLetterModel> newsLetterModelArrayList=new ArrayList<>();
    int position;
    TextView title,createdDate;
    ImageView img,web,pdf;
    TextView jtv,weblink,pdflink;
    //LinearLayout place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newsletterdetail);
        extras = getIntent().getExtras();

        initUI();
        resetDisconnectTimer();

        setValues();
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        title= (TextView) findViewById(R.id.title);
        web= (ImageView) findViewById(R.id.webImg);
        pdf= (ImageView) findViewById(R.id.pdfImg);
        weblink= (TextView) findViewById(R.id.weblink);
        pdflink= (TextView) findViewById(R.id.pdflink);
        createdDate= (TextView) findViewById(R.id.createdDate);
        img= (ImageView) findViewById(R.id.img);

        //jtv=new JustifiedTextView();
        jtv= (TextView) findViewById(R.id.description);
//        headermanager = new HeaderManager(NewsLetterDetailActivity.this, "Newsletters");

        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager = new HeaderManager(NewsLetterDetailActivity.this, getString(R.string.phantas_magoria));
            headermanager.getHeader(relativeHeader, 1);
            pdf.setBackgroundResource(R.drawable.pdfgreen);
            web.setBackgroundResource(R.drawable.webgreen);
        } else {
            headermanager = new HeaderManager(NewsLetterDetailActivity.this, getString(R.string.i_learn));
            headermanager.getHeader(relativeHeader, 3);
            pdf.setBackgroundResource(R.drawable.pdfblue);
            web.setBackgroundResource(R.drawable.webblue);
        }           back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });

if(extras!=null){
    newsLetterModelArrayList=(ArrayList<NewsLetterModel>) extras
            .getSerializable("array");
    position=extras.getInt("position");
}

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                Intent intent =new Intent(mContext, LoadWebViewActivity.class);
                intent.putExtra("url",newsLetterModelArrayList.get(position).getNewsWebLink());
                intent.putExtra("tab_type","Newsletters");
                mContext.startActivity(intent);
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   stopDisconnectTimer();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsLetterModelArrayList.get(position).getNewsPdfLink()));
                startActivity(browserIntent);*/

                if (!(newsLetterModelArrayList.get(position).getNewsPdfLink().equalsIgnoreCase(""))) {
                    Intent browserIntent = new Intent(NewsLetterDetailActivity.this, PDFViewActivity.class);
                    browserIntent.putExtra("pdf_url",newsLetterModelArrayList.get(position).getNewsPdfLink());
                    browserIntent.putExtra("title","News Letter");
                    browserIntent.putExtra("filename",newsLetterModelArrayList.get(position).getNewsTitle());
                    startActivity(browserIntent);

                }
                else
                {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.nomoreinfo), R.drawable.exclamationicon, R.drawable.roundblue);

                }
                /*Intent intent = new Intent(NewsLetterDetailActivity.this, PdfReader.class);
                intent.putExtra("pdf_url",newsLetterModelArrayList.get(position).getNewsPdfLink());
                startActivity(intent);*/
            }
        });
    }
private  void setValues(){
    final CustomProgressBar pDialog = new CustomProgressBar(mContext,
            R.drawable.spinner);
//    pDialog.show();
    if(!newsLetterModelArrayList.get(position).getNewsPdfLink().equalsIgnoreCase("")){
        //pdflink.setText(newsLetterModelArrayList.get(position).getNewsPdfLink());
        pdf.setVisibility(View.VISIBLE);

    }else {
        pdf.setVisibility(View.GONE);
    }
    if(!newsLetterModelArrayList.get(position).getNewsWebLink().equalsIgnoreCase("")){
        //weblink.setText(newsLetterModelArrayList.get(position).getNewsWebLink());
        web.setVisibility(View.VISIBLE);

    }else {
        web.setVisibility(View.GONE);
    }
    jtv.setText(Html.fromHtml("<html><body style=\"text-align:justify\"> " + newsLetterModelArrayList.get(position).getNewsDescription() + " </body></html>"));
    title.setText(newsLetterModelArrayList.get(position).getTitle());
    createdDate.setText(AppUtilityMethods.dateParsingToDdMmmYyyy(newsLetterModelArrayList.get(position).getNewsCreatedTime()).replace(".",""));
  /*  Glide.with(mContext).load(newsLetterModelArrayList.get(position).getNewsImage()).listener(new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            pDialog.dismiss();
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            pDialog.dismiss();
            return false;
        }
    })*/
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int height = displayMetrics.heightPixels;
    int width = displayMetrics.widthPixels;
    /*Glide.with(mContext).load(newsLetterModelArrayList.get(position).getNewsImage()).centerCrop().
    placeholder(R.drawable.noimage)
            .into(img);
*/
    if(!newsLetterModelArrayList.get(position).getNewsImage().equals("")) {
//        Picasso.with(mContext).load(newsLetterModelArrayList.get(position).getNewsImage().replace(" ", "%20")).fit().centerCrop().placeholder(R.drawable.noimage)
//                .error(R.drawable.noimage)
//                .into(img);
        loadImage(mContext, newsLetterModelArrayList.get(position).getNewsImage().replaceAll(" ", "%20"), img);
    }

}
   private Target mTarget;
    void loadImage(Context context, String url, final ImageView img) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                //Do something

                img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url).error(R.drawable.noimage)
                .into(mTarget);
    }

  /*  public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(NewsLetterDetailActivity.this, false);
            AppPreferenceManager.setUserId(NewsLetterDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(NewsLetterDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(NewsLetterDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(NewsLetterDetailActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };*/

    public void resetDisconnectTimer(){
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDisconnectTimer();

    }
  @Override protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(NewsLetterDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
