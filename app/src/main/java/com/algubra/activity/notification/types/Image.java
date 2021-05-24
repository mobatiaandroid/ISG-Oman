package com.algubra.activity.notification.types;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.activity.specialmessages.model.SpecialMessageModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class Image extends Activity{
    String pushNotificationDetail;
    Bundle extras;
    ArrayList<NotificationModel> specialMessageModelArrayList=new ArrayList<>();
    int position;
    private Context mContext=this;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private boolean mErrorFlag = false;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,imageview;
    RotateAnimation anim;
    TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert_image);
        extras = getIntent().getExtras();
        if (extras != null) {
            specialMessageModelArrayList = (ArrayList<NotificationModel>) extras
                    .getSerializable("array");
            position=extras.getInt("position");

        }
        initUI();

    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        imageview= (ImageView) findViewById(R.id.img);
        description= (TextView) findViewById(R.id.description);
        headermanager = new HeaderManager(Image.this, "Notification Details");
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }           back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(!specialMessageModelArrayList.get(position).getUrl().equals("")) {
//        Picasso.with(mContext).load(newsLetterModelArrayList.get(position).getNewsImage().replace(" ", "%20")).fit().centerCrop().placeholder(R.drawable.noimage)
//                .error(R.drawable.noimage)
//                .into(img);
            loadImage(mContext, specialMessageModelArrayList.get(position).getUrl().replaceAll(" ", "%20"), imageview);
        }

        description.setText(specialMessageModelArrayList.get(position).getMessage());
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
}
