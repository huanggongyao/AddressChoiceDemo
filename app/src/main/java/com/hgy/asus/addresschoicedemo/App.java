package com.hgy.asus.addresschoicedemo;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cookie.store.PersistentCookieStore;

import java.util.logging.Level;

/**
 * Created by Asus on 2017/3/14.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(this);
        initOkhttp();
    }

    private void initOkhttp() {
        OkGo.getInstance().debug("Okgo", Level.INFO,true)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3)
                .setCookieStore(new PersistentCookieStore())
                .setCertificates();
    }
}
