package com.xmwdkk.boothprint;

import android.app.Application;

import com.ysh.rn.printet.base.AppInfo;

/**
 * Created by dell on 2017/11/20.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppInfo.init(getApplicationContext());
    }
}