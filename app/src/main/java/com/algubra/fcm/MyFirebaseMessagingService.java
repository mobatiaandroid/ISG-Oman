package com.algubra.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.algubra.R;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.notification.NotificationListActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.NotificationID;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by gayatri on 11/4/17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    int badgecount = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            String questionTitle = data.get("questionTitle").toString();
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            sendNotofication(remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

//        sendNotofication(remoteMessage.getNotification().getBody());
    }

    private void sendNotofication(String message) {
        ShortcutBadger.applyCount(this, badgecount); //for 1.1.4+
        AppPreferenceManager.setBadgecount(this, String.valueOf(badgecount));
//        AppController.textBadge.setText(AppPreferenceManager.getBadgecount(this));
//        AppController.textBadge.setVisibility(View.INVISIBLE);

        Intent mIntent = new Intent("badgenotify");
        Intent intent;
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
//        HomeActivity.getBadgeAPI(getApplicationContext());
        if (!(AppPreferenceManager.getUserId(getApplicationContext()).equalsIgnoreCase(""))) {
             intent = new Intent(this, NotificationListActivity.class);

        }
        else
        {
            intent = new Intent(this, LoginActivity.class);

        }
        int notId= NotificationID.getID();
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.appicon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);*/
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(notId, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.appicon);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

       /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        if (android.os.Build.VERSION.SDK_INT >= 22) {
            builder.setLargeIcon(largeIcon);
            builder.setSmallIcon(R.drawable.app_trans);
            builder.setColor(getResources().getColor(R.color.loginbtnBg));

        } else {
            builder.setSmallIcon(R.mipmap.appicon);
        }

        builder.setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
// Create the TaskStackBuilder and add the intent, which inflates the back stack

// Get the PendingIntent containing the entire back stack


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.app_name)+"_01";// The id of the channel.
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            builder.setChannelId(mChannel.getId());
            mChannel.setShowBadge(true);
            mChannel.canShowBadge();
            mChannel.enableLights(true);
            mChannel.setLightColor(getResources().getColor(R.color.split_bg));
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);

        }
        if (notificationManager != null) {
            if (message!=null)
            {
                if (!(message.equalsIgnoreCase("")) && !(message.equalsIgnoreCase("null")))
                {
                    notificationManager.notify(notId, builder.build());
                }
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String badge = data.getString("badge");
            badgecount = Integer.valueOf(badge);
            String message = data.getString("message");
//            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            sendNotofication(message);
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}