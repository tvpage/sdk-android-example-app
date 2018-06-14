package com.tvpage.demo;

import android.app.Application;
import android.content.Context;

import com.tvpage.demo.utils.MyPreferencesForTvPageApp;

import com.tvpage.lib.utils.TvPageInstance;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.tvpage.demo.utils.MyPreferencesForTvPageApp.CHANNEL_ID_PREF_KEY;


/**
 * Created by MTPC-110 on 3/9/2017.
 */

public class TvPageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TvPageInstance.getInstance(this).setApiKey("1758799");
        //TSRTvPageInit.getInstance(this).setApiKey("1758929")
        MyPreferencesForTvPageApp.setPref(this, CHANNEL_ID_PREF_KEY, "66133905");

        //Register Caligraphy for font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Dosis-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

}
