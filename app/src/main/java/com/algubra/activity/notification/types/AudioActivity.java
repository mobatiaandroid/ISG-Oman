package com.algubra.activity.notification.types;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gayatri on 4/5/16.
 */
public class AudioActivity extends Activity implements SeekBar.OnSeekBarChangeListener, OnClickListener, Callback, OnPreparedListener, OnCompletionListener,
        OnSeekCompleteListener {
    private SeekBar seekBarProgress;
    private LinearLayout linearLayoutMediaController;
    Button btnplay;
    String action = "Play";
    RelativeLayout header;
    Bundle extrass;
    ArrayList<NotificationModel> videolist;
    int position;
    private boolean isReset = false;
    HeaderManager headermanager;
    Context mcontext = this;
    MediaPlayer player;
    private TextView textViewPlayed;
    private TextView textViewLength;
    private TextView textcontent;
    private Timer updateTimer;
    ImageView playerIamge;
    private AnimationDrawable anim;
    Bundle extras;
    ArrayList<NotificationModel> alertlist;
    private ProgressBar progressBarWait;
    private static final String TAG = "androidEx2 = VideoSample";
    //private String url = "http://www.hubharp.com/web_sound/BachGavotte.mp3";
    private String url = "";
    private boolean isplayclicked = false;
    ImageView back;
    TextView timeStamp;
    String ans;
    java.util.Date date = null;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_push_new);
        extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");

            alertlist = (ArrayList<NotificationModel>) extras
                    .getSerializable("array");

        }
        initialiseUI();
    }

    private void initialiseUI() {
        header = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(activity, "Notification");
        if (AppPreferenceManager.getSchoolSelection(mcontext).equals("ISG")) {
            headermanager.getHeader(header, 1);
        } else {
            headermanager.getHeader(header, 3);
        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);

        linearLayoutMediaController = (LinearLayout) findViewById(R.id.linearLayoutMediaController);
        btnplay = (Button) findViewById(R.id.btn_play);
        textViewPlayed = (TextView) findViewById(R.id.textViewPlayed);
        textViewLength = (TextView) findViewById(R.id.textViewLength);
        textcontent = (TextView) findViewById(R.id.txt);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBarProgress);
        playerIamge = (ImageView) findViewById(R.id.imageViewPauseIndicator);
        url = alertlist.get(position).getUrl();

        textcontent.setText(alertlist.get(position).getUrl());
        System.out.println("check url" + url);
        seekBarProgress.setProgress(0);
        seekBarProgress.setOnSeekBarChangeListener(this);
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnSeekCompleteListener(this);
        progressBarWait = (ProgressBar) findViewById(R.id.progressBarWait);

        SetOnClicks();

        //setDetails();
        if (AppUtilityMethods.isNetworkConnected(mcontext)) {
            playMp3();

        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mcontext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }

        btnplay.setOnClickListener(this);

    }

    public void playMp3() {
        if (url.equals(" ")) {
            // showToast("Please, set the video URI in HelloAndroidActivity.java in onClick(View v) method");
        } else {


            try {

                player.setDataSource(url);
                player.prepareAsync();


            } catch (IOException e) {
                //showToast("Error while playing video");
//                Log.i(TAG,
//                        "========== IllegalArgumentException ===========");
                e.printStackTrace();
            } catch (IllegalStateException e) {
                //showToast("Error while playing video");
//                Log.i(TAG,
//                        "========== IllegalStateException ===========");
                e.printStackTrace();
            }


        }
    }

    private void SetOnClicks() {
        back.setOnClickListener(this);
    }



    @Override
    public void onSeekComplete(MediaPlayer mp) {
        // TODO Auto-generated method stub
        progressBarWait.setVisibility(View.GONE);
        btnplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        mp.stop();
        btnplay.setText("Play");

        //	btnplay.setBackgroundResource(R.drawable.play);

        if (updateTimer != null) {
            updateTimer.cancel();
        }
        player.reset();
        isReset = true;


    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        int duration = mp.getDuration() / 1000; // duration in seconds

        seekBarProgress.setMax(duration);
        textViewLength.setText(AppUtilityMethods.durationInSecondsToString(duration));
        progressBarWait.setVisibility(View.GONE);

        if (!mp.isPlaying()) {
            playerIamge.setBackgroundResource(R.drawable.mic);
            btnplay.setVisibility(View.VISIBLE);
            anim = (AnimationDrawable) playerIamge.getBackground();
            anim.start();
            btnplay.setText("Play");
            //	btnplay.setBackgroundResource(R.drawable.pause);
            mp.start();
            updateMediaProgress();
            linearLayoutMediaController.setVisibility(View.VISIBLE);

        }
    }

    private void updateMediaProgress() {
        updateTimer = new Timer("progress Updater");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (player != null) {
                            seekBarProgress.setProgress(player.getCurrentPosition() / 1000);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub
        if (!fromUser) {
            textViewPlayed.setText(AppUtilityMethods.durationInSecondsToString(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        if (player.isPlaying()) {
            progressBarWait.setVisibility(View.VISIBLE);
            player.seekTo(seekBar.getProgress() * 1000);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        }
    }
}