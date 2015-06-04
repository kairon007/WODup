package com.modup.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.modup.app.MainActivity;
import com.modup.app.R;
import com.modup.model.SingleWorkout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    private String TAG = NotificationReceiver.class.getCanonicalName();
    private Handler mHandler;
    int count = 0;
    Context context;

    public NotificationReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        mHandler = new Handler();
        initNotification(context);
    }

    private Context getContext() {
        return context;
    }

    private void initNotification(final Context context) {

        if (ParseUser.getCurrentUser() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date date = cal.getTime();

            ParseQuery<SingleWorkout> query = new ParseQuery<SingleWorkout>(
                    "SingleWorkout");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("event_date", date);
            query.setLimit(10);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<SingleWorkout>() {
                @Override
                public void done(List<SingleWorkout> list, ParseException e) {
                    //TODO: LOOP THROUGH THE LIST
                    if (list.size() != 0) {
                        notifyUser(context);
                    }
                }
            });
        } else {
            checkParseUser();
        }
    }

    private void notifyUser(Context context) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.logo);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_logo)
                        .setLargeIcon(icon)
                        .setContentTitle("WODup")
                        .setContentText("You have workouts waiting! Go get 'em!")
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void checkParseUser() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ParseUser.getCurrentUser() == null) {
                    if (count != 50) {
                        Log.e(TAG, "Attempting to retrieve Parse User");
                        count++;
                        mHandler.postDelayed(this, 5000);
                    } else {
                        Log.e(TAG, "Unable to retrieve Parse User, stopping service");
                        count = 0;
                    }
                } else {
                    Log.e(TAG, "Successfully retrieved Parse User");
                    initNotification(getContext());
                }
            }
        }, 1000);
    }
}
