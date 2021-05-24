package com.algubra.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.algubra.manager.AppPreferenceManager;


/**
 * Created by mob-dt-and007 on 26/10/16.
 */
public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("onBind", "Service onBind");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
//                    Intent intent = new Intent("com.mobatia.testsample.BootUpReceiver");
//            sendBroadcast(intent);
        AppPreferenceManager.setIsGuest(getApplicationContext(), false);
        AppPreferenceManager.setUserId(getApplicationContext(), "");
        AppPreferenceManager.setIsUserAlreadyLoggedIn(getApplicationContext(), false);
        AppPreferenceManager.setSchoolSelection(getApplicationContext(), "ISG");
    }

    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here

        stopSelf();
    }
}