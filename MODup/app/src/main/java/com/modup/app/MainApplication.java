package com.modup.app;

import android.app.Application;

import com.modup.model.SingleWorkout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ParseObject.registerSubclass(SingleWorkout.class);
		Parse.initialize(this, "s4j77P0ufDDbw2CTpWkSEzilGOzoBwRut1fmfp3y", "uy55I4cMMg4Dv7GY3uXRVbov1v5SLq6KAxEwi96F");



		ImageLoader.getInstance()
				.init(ImageLoaderConfiguration.createDefault(this));
	}
}
