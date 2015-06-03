package com.modup.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.modup.model.Comment;
import com.modup.model.SingleWorkout;
import com.modup.service.NotificationService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

public class MainApplication extends Application {

    String TAG = MainApplication.class.getCanonicalName();

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(SingleWorkout.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(this, "s4j77P0ufDDbw2CTpWkSEzilGOzoBwRut1fmfp3y", "uy55I4cMMg4Dv7GY3uXRVbov1v5SLq6KAxEwi96F");
        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);


        if(!(isMyServiceRunning(NotificationService.class))) {
            startService(new Intent(this, NotificationService.class));
        }

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration.createDefault(this));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopService(){
        //TODO: FIGURE OUT HOW TO COMPLETE THIS
    }


}
