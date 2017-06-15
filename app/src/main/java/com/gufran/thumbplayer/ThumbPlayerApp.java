package com.gufran.thumbplayer;

import android.app.Application;

import com.squareup.otto.ThreadEnforcer;

/**
 * Description ...
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 6/15/17
 */

public class ThumbPlayerApp extends Application {
    public static ThumbPlayerApp sInstance;
    public static EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        eventBus = new EventBus(ThreadEnforcer.MAIN);
    }
}
