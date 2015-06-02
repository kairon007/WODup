package com.modup.app;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.modup.model.Comment;
import com.modup.model.SingleWorkout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(SingleWorkout.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(this, "s4j77P0ufDDbw2CTpWkSEzilGOzoBwRut1fmfp3y", "uy55I4cMMg4Dv7GY3uXRVbov1v5SLq6KAxEwi96F");
        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration.createDefault(this));
    }
}
