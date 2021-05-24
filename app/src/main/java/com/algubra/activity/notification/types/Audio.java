package com.algubra.activity.notification.types;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.SurfaceHolder.Callback;

import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gayatri on 5/5/17.
 */
public class Audio extends Activity implements SeekBar.OnSeekBarChangeListener,
        Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener,
        MediaPlayer.OnSeekCompleteListener, JsonTagConstants, URLConstants, StausCodes {
    private SeekBar seekBarProgress;
    private LinearLayout linearLayoutMediaController;
    TextView btnplay;
    String action = "play";
    int position;
    private boolean isReset = false;
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
    // private String url = "http://www.hubharp.com/web_sound/BachGavotte.mp3";
    private String url = "";
    private boolean isplayclicked = false;
    ImageView backImg;
    ImageView home;
    Context mContext;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_push);
        mContext=this;
        mActivity=this;
        extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");

            alertlist = (ArrayList<NotificationModel>) extras
                    .getSerializable("array");

        }
        initialiseUI();

    }

    /**
     * Method Name : initialiseUI Description : for initializing views Params :
     * nil Return type : Date : 21/11/2013 Author : Sabarish Kumar.S
     * */
    private void initialiseUI() {


        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        btnplay = (TextView) findViewById(R.id.btn_play);

        headermanager = new HeaderManager(mActivity, "Notification Details");


        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            btnplay.setBackgroundColor(getResources().getColor(R.color.login_button_bg));
        } else {
            headermanager.getHeader(relativeHeader, 3);
            btnplay.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));

        }
        backImg = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        backImg.setOnClickListener(this);

        linearLayoutMediaController = (LinearLayout) findViewById(R.id.linearLayoutMediaController);
        textViewPlayed = (TextView) findViewById(R.id.textViewPlayed);
        textViewLength = (TextView) findViewById(R.id.textViewLength);
        textcontent = (TextView) findViewById(R.id.txt);
        seekBarProgress = (SeekBar) findViewById(R.id.seekBarProgress);
        playerIamge = (ImageView) findViewById(R.id.imageViewPauseIndicator);
        url = alertlist.get(position).getUrl();
        textcontent.setText(alertlist.get(position).getMessage());
        System.out.println("check url" + url);
        seekBarProgress.setProgress(0);
        seekBarProgress.setOnSeekBarChangeListener(this);
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnSeekCompleteListener(this);
        progressBarWait = (ProgressBar) findViewById(R.id.progressBarWait);
        if (AppUtilityMethods.isNetworkConnected(mcontext)) {
            playMp3();

        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mcontext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
        // btnplay.setVisibility(View.VISIBLE);
        // btnplay.setBackgroundResource(R.drawable.play);
        btnplay.setOnClickListener(this);
    }

    /**
     * Method Name : setListeners Description : initiates listeners to view
     * Params : nil Return type : Date : 21/11/2013 Author : Sabarish Kumar.S
     * */

    /**
     * Method Name : playMp3 Description : Play the audio from the link Params :
     * link Return type : nil Date : 21/06/2013 Author : Jiju Induchoodan
     * */
    public void playMp3() {
        if (url.equals(" ")) {
            // showToast("Please, set the video URI in HelloAndroidActivity.java in onClick(View v) method");
        } else {

            try {

                player.setDataSource(url);
                player.prepareAsync();

            } catch (IllegalArgumentException e) {
                // showToast("Error while playing video");
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // showToast("Error while playing video");
                e.printStackTrace();
            } catch (IOException e) {
                showToast(getResources().getString(R.string.no_internet));
                // AppUtils.showToast(getResources().getString(R.string.Internet_check),
                // mcontext);
                e.printStackTrace();
            }

        }
    }

    /**
     * Method Name : showToast Description :for showing Toast Params : String
     * Return type : Date : 21/11/2013 Author : Sabarish Kumar.S
     * */
    private void showToast(final String string) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(mcontext, string, Toast.LENGTH_LONG).show();
                finish();
            }
        });
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
        btnplay.setText(getResources().getString(R.string.play));

//		 btnplay.setBackgroundResource(R.drawable.play);

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
//			anim = (AnimationDrawable) playerIamge.getBackground();
//			anim.start();
            btnplay.setText(getResources().getString(R.string.play));
            // btnplay.setBackgroundResource(R.drawable.pause);
            mp.start();
            updateMediaProgress();
            linearLayoutMediaController.setVisibility(View.VISIBLE);

        }
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
            progressBarWait.setVisibility(View.GONE);
            player.seekTo(seekBar.getProgress() * 1000);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnplay) {

            if (!isplayclicked) {

                if (player.isPlaying()) {
                    System.out.println("is come click second");
                    player.pause();
                    playerIamge.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.mic));
//					anim.stop();
                    btnplay.setText(getResources().getString(R.string.play));

                    // btnplay.setBackgroundResource(R.drawable.play);

                }

                isplayclicked = true;
            } else {

                if (player == null || isReset) {
                    if (AppUtilityMethods.isNetworkConnected(mcontext)) {
                        playMp3();
                        playerIamge
                                .setBackgroundResource(R.drawable.michover);
//						anim = (AnimationDrawable) playerIamge.getBackground();
//						anim.start();
                        isReset = false;
                    } else {
                        Toast.makeText(mcontext,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();

                    }

                } else {
                    if (AppUtilityMethods.isNetworkConnected(mcontext)) {
                        player.start();
                        playerIamge
                                .setBackgroundResource(R.drawable.michover);
//						anim = (AnimationDrawable) playerIamge.getBackground();
//						anim.start();
                        btnplay.setText(getResources()
                                .getString(R.string.pause));

                        // btnplay.setBackgroundResource(R.drawable.pause);
                    } else {
                        Toast.makeText(mcontext,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();

                    }
                }
                isplayclicked = false;
            }

        } else if (v == backImg) {
            finish();
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
                            seekBarProgress.setProgress(player
                                    .getCurrentPosition() / 1000);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (player != null) {
			/*
			 * if(player.isPlaying()) {
			 */
            player.stop();
            player.release();
            player = null;

        }

    }
}