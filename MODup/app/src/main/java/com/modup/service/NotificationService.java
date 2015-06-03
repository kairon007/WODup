package com.modup.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.parse.ParseUser;

public class NotificationService extends Service {

    private ParseUser currentUser;
    private Handler mHandler;
    private String TAG = NotificationService.class.getCanonicalName();

    public NotificationService() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        checkParseUser();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifyUser();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyUser() {

    }

    private void checkParseUser() {
        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser == null) {
                        currentUser = ParseUser.getCurrentUser();
                        mHandler.postDelayed(this, 10000);
                    }
                }
            }, 10000);
        }
    }
}
