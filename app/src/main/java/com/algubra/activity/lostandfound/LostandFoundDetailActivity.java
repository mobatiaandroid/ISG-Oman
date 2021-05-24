package com.algubra.activity.lostandfound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.loadweburl.LoadImageWebViewActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.lostandfound.model.LostandFoundModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by user2 on 5/5/17.
 */
public class LostandFoundDetailActivity extends Activity {
    RecyclerView messageList;
    RelativeLayout relativeHeader;
    LinearLayout contactDescriptionLinear;
    LinearLayout contactNameLinear;
    LinearLayout contactEmailLinear;
    LinearLayout contactPhoneLinear;
    HeaderManager headermanager;
    ImageView back, addItem;
    ImageView lostImage;
    //    WebView lostImage;
    Context mContext = this;
    TextView listTxtTitle, listTxtDate, description, contactName, contactEmail, contactPhone, postedBy, postedEmail, postedPhone, descriptionHeader;
    Bundle extras;
    ArrayList<LostandFoundModel> lostFoundArray = new ArrayList<>();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfound_detail);
        initUI();
        resetDisconnectTimer();

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            lostFoundArray = (ArrayList<LostandFoundModel>) extras
                    .getSerializable("array");
            position = extras.getInt("position");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(LostandFoundDetailActivity.this, "Lost & Found");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });
        contactDescriptionLinear = (LinearLayout) findViewById(R.id.contactDescriptionLinear);
        contactNameLinear = (LinearLayout) findViewById(R.id.contactNameLinear);
        contactEmailLinear = (LinearLayout) findViewById(R.id.contactEmailLinear);
        contactPhoneLinear = (LinearLayout) findViewById(R.id.contactPhoneLinear);
        descriptionHeader = (TextView) findViewById(R.id.descriptionHeader);
        listTxtTitle = (TextView) findViewById(R.id.listTxtTitle);
        listTxtDate = (TextView) findViewById(R.id.listTxtDate);
        description = (TextView) findViewById(R.id.description);
        contactName = (TextView) findViewById(R.id.contactName);
        contactEmail = (TextView) findViewById(R.id.contactEmail);
        contactPhone = (TextView) findViewById(R.id.contactPhone);
        postedBy = (TextView) findViewById(R.id.postedBy);
        postedEmail = (TextView) findViewById(R.id.postedEmail);
        postedPhone = (TextView) findViewById(R.id.postedPhone);
        lostImage = (ImageView) findViewById(R.id.lostImage);
//        lostImage= (WebView) findViewById(R.id.lostImage);
        lostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                Intent intent = new Intent(mContext, LoadImageWebViewActivity.class);
                intent.putExtra("url", lostFoundArray.get(position).getLostImage().replaceAll(" ", "%20"));
                intent.putExtra("tab_type", "Lost & Found");
                startActivity(intent);
            }
        });
        listTxtTitle.setText(lostFoundArray.get(position).getTitle());
        if (lostFoundArray.get(position).getDescription().equalsIgnoreCase("")) {
            descriptionHeader.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            contactDescriptionLinear.setVisibility(View.GONE);
        } else {
            description.setText(lostFoundArray.get(position).getDescription());
            contactDescriptionLinear.setVisibility(View.VISIBLE);
            descriptionHeader.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
        }
        if (lostFoundArray.get(position).getContact_person_name().equalsIgnoreCase("")) {
            contactNameLinear.setVisibility(View.GONE);
        } else {
            contactName.setText(lostFoundArray.get(position).getContact_person_name());
            contactNameLinear.setVisibility(View.VISIBLE);

        }
        if (lostFoundArray.get(position).getContact_person_email().equalsIgnoreCase("")) {
            contactEmailLinear.setVisibility(View.GONE);

        } else {
            contactEmail.setText(lostFoundArray.get(position).getContact_person_email());
            contactEmailLinear.setVisibility(View.VISIBLE);


        }
        if (lostFoundArray.get(position).getContact_person_phone().equalsIgnoreCase("")) {
            contactPhoneLinear.setVisibility(View.GONE);

        } else {
            contactPhone.setText(lostFoundArray.get(position).getContact_person_phone());
            contactPhoneLinear.setVisibility(View.VISIBLE);

        }
        postedBy.setText(lostFoundArray.get(position).getPosted_user());
        postedEmail.setText(lostFoundArray.get(position).getPosted_user_email());
        postedPhone.setText(lostFoundArray.get(position).getPosted_user_contact());
        if (!lostFoundArray.get(position).getLostImage().equals("")) {
//            loadImage(mContext, lostFoundArray.get(position).getLostImage().replaceAll(" ", "%20"), lostImage);
            lostImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(lostFoundArray.get(position).getLostImage().replaceAll(" ", "%20")).fit().placeholder(R.drawable.noimage)
                    .into(lostImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            lostImage.setAdjustViewBounds(true);
                            lostImage.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError() {
//            Glide.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position).toString())).centerCrop().into(imageView);
                            lostImage.setAdjustViewBounds(true);
                            lostImage.setVisibility(View.GONE);

                        }
                    });

//            WebSettings mwebSettings = lostImage.getSettings();
//            mwebSettings.setSaveFormData(true);
//            mwebSettings.setBuiltInZoomControls(false);
//            mwebSettings.setDisplayZoomControls(false);
//            mwebSettings.setSupportZoom(false);
//            mwebSettings.setPluginState(WebSettings.PluginState.ON);
//            mwebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//            mwebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//            mwebSettings.setDomStorageEnabled(true);
//            mwebSettings.setDatabaseEnabled(true);
//            mwebSettings.setDefaultTextEncodingName("utf-8");
//            mwebSettings.setLoadsImagesAutomatically(true);
//            lostImage.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
//            lostImage.getSettings().setAppCachePath(
//                    mContext.getCacheDir().getAbsolutePath());
//            lostImage.getSettings().setAllowFileAccess(true);
//            lostImage.getSettings().setAppCacheEnabled(true);
//            lostImage.getSettings().setJavaScriptEnabled(true);
//            lostImage.getSettings()
//                    .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//            lostImage.setBackgroundColor(0X00000000);
//
//            lostImage.loadDataWithBaseURL(null, "<html><head></head><body><table style=\"width:100%; height:100%;\"><tr><td style=\"vertical-align:middle;\"><img src=\"" + lostFoundArray.get(position).getLostImage().replace(" ", "%20") + "\"></td></tr></table></body></html>", "html/css", "utf-8", null);
//            lostImage.setVisibility(View.VISIBLE);
        } else {
            lostImage.setVisibility(View.GONE);

        }
    }

   /* public static final long DISCONNECT_TIMEOUT = 300000;// 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(LostandFoundDetailActivity.this, false);
            AppPreferenceManager.setUserId(LostandFoundDetailActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(LostandFoundDetailActivity.this, false);
            AppPreferenceManager.setSchoolSelection(LostandFoundDetailActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(LostandFoundDetailActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };*/

    public void resetDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
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

    private Target mTarget;

    void loadImage(Context context, String url, final ImageView img) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                //Do something

                img.setImageBitmap(bitmap);
                lostImage.setVisibility(View.VISIBLE);

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
  @Override protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(LostandFoundDetailActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }

}
