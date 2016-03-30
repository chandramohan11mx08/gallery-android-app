package com.gallery.app;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class GalleryApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public Context getContext(){
        return mContext;
    }
}
