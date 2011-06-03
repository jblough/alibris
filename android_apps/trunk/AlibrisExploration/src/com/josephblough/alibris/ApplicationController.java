package com.josephblough.alibris;

import com.josephblough.alibris.util.ImageLoader;

import android.app.Application;
import android.util.Log;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationController";
    
    public ImageLoader imageLoader;

    public void onCreate() {
	super.onCreate();
	
	//Do Application initialization over here
	Log.d(TAG, "onCreate");

        imageLoader = new ImageLoader(this);
    }
    
}
