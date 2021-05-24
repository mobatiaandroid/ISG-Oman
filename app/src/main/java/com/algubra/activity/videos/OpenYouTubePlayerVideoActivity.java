package com.algubra.activity.videos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.notification.model.NotificationModel;
import com.algubra.activity.videos.model.VideosModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gayatri on 22/9/16.
 */
public class OpenYouTubePlayerVideoActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    YouTubePlayerView playerView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    boolean fullScreen = false;
    Bundle extras;
    int position;
    ArrayList<VideosModel> videolist = new ArrayList<>();
    String PartOne = "AIzaSyCk";
    String PartTwo = "heO4SXViv3";
    String PartThree = "gBvtzTMl4s";
    String PartFour = "GWf7WY80Dag";

    ImageView back;
    Activity mActivity = this;
    TextView textcontent;
    YouTubePlayer player;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube);

        extras = getIntent().getExtras();

        if (extras != null) {
            position = extras.getInt("position");

            videolist = (ArrayList<VideosModel>) extras
                    .getSerializable("array");
        }
        //url = getYoutubeVideoId(url);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(mActivity, "Videos");

        if (AppPreferenceManager.getSchoolSelection(mActivity).equals("ISG")) {
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
                finish();
            }
        });
        playerView = (YouTubePlayerView) findViewById(R.id.player_view);

        // initializes the YouTube player view
        playerView.initialize(PartOne+PartTwo+PartThree+PartFour, this);
        // playerView.set
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorResult) {

        // shows dialog if user interaction may fix the error
        if (errorResult.isUserRecoverableError()) {
            errorResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            // displays the error occured during the initialization
            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String error = String.format(getString(R.string.common_error),
                    errorResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        final YouTubePlayer player_youtube, boolean wasRestored) {

        if (!wasRestored) {
            player = player_youtube;
            // Use cueVideo() method, if you don't want to play it automatically
            // loadVideo() will auto play video

            // player.loadVideo(Config.VIDEO_CODE);
            player.cueVideo(getYoutubeVideoId(videolist.get(position).getVideo_link()));

            // handling fullscreen and exit full screen

            player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean fullscreen) {
                    if (fullscreen) {
                        player.play();
                        // fltop.setVisibility(View.GONE);
                    } else {
                        player.play();
                        // fltop.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {

            // initializes the YouTube player view
            getYouTubePlayerProvider().initialize(PartOne+PartTwo+PartThree+PartFour, this);
        }
    }

    // Returns Player view defined in xml file
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.player_view);
    }

    /**
     *
     */
    private final class EventListener implements
            YouTubePlayer.PlaybackEventListener {

        /**
         * Called when video starts playing
         */
        @Override
        public void onPlaying() {
            Log.e("Status", "Playing");
        }

        /**
         * Called when video stops playing
         */
        @Override
        public void onPaused() {
            Log.e("Status", "Paused");
        }

        /**
         * Called when video stopped for a reason other than paused
         */
        @Override
        public void onStopped() {
            Log.e("Status", "Stopped");
        }

        /**
         * Called when buffering of video starts or ends
         *
         * @param b True if buffering is on, else false
         */
        @Override
        public void onBuffering(boolean b) {
        }

        /**
         * Called when jump in video happens. Reason can be either user
         * scrubbing or seek method is called explicitely
         *
         * @param i
         */
        @Override
        public void onSeekTo(int i) {
        }
    }

    private final class StateChangeListener implements
            YouTubePlayer.PlayerStateChangeListener {

        /**
         * Called when player begins loading a video. During this duration,
         * player won't accept any command that may affect the video playback
         */
        @Override
        public void onLoading() {
        }

        /**
         * Called when video is loaded. After this player can accept the
         * playback affecting commands
         *
         * @param s Video Id String
         */
        @Override
        public void onLoaded(String s) {
        }

        /**
         * Called when YouTube ad is started
         */
        @Override
        public void onAdStarted() {
        }

        /**
         * Called when video starts playing
         */
        @Override
        public void onVideoStarted() {
        }

        /**
         * Called when video is ended
         */
        @Override
        public void onVideoEnded() {
        }

        /**
         * Called when any kind of error occurs
         *
         * @param errorReason Error string showing the reason behind it
         */
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0
                && youtubeUrl.startsWith("http")
                || (youtubeUrl.startsWith("https"))) {

            String expression = "^.*((youtu.be"
                    + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var
            // regExp
            // =
            // /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        System.out.println("url---" + video_id);

        return video_id;
    }

    private String youTubeVideoId(String link) {
        String[] spltStrings = null;
        String pattern = "(?:videos\\/|v=)([\\w-]+)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(link);
        if (matcher.find()) {
            String splitString = matcher.group();
            spltStrings = splitString.split("=");
            System.out.println("videoid" + spltStrings[1]);
        }
        return spltStrings[1];
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }
}
