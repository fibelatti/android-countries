package com.fibelatti.countries;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;

public class AndroidApplication
        extends Application {
    public static final String TAG = AndroidApplication.class.getSimpleName();
    public static AndroidApplication app;

    public AndroidApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(true)
                        .build()
                ).build());

        LeakCanary.install(this);
        setPicassoCache();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void setPicassoCache() {
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(app, Integer.MAX_VALUE))
                .build();

        Picasso.setSingletonInstance(picasso);
    }
}
