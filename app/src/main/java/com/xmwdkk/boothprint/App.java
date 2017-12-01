package com.xmwdkk.boothprint;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.ysh.rn.printet.base.AppInfo;

/**
 * Created by dell on 2017/11/20.
 */

public class App extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        AppInfo.init(getApplicationContext());
//    }
    private Activity activity;

    private ActivityLifecycleCallbacks lifecycleCallback = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (!"com.tbruyelle.rxpermissions.ShadowActivity".equals(activity.getClass().getName())) {
                App.this.activity = activity;
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (!"com.tbruyelle.rxpermissions.ShadowActivity".equals(activity.getClass().getName())) {
                App.this.activity = activity;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (!"com.tbruyelle.rxpermissions.ShadowActivity".equals(activity.getClass().getName())) {
                App.this.activity = activity;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    protected static class SingletonHolder {
        public static App INSTANCE;
    }

    public App() {
        registerActivityLifecycleCallbacks(lifecycleCallback);
        SingletonHolder.INSTANCE = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //程序一启动，就将未捕获异常初始化
        //CrashUtil.getInstance().init();
//        SingletonHolder.INSTANCE = this;
        AppInfo.init(getApplicationContext());
    }

    public void onLowMemory() {
        System.gc();
        System.runFinalization();
        System.gc();
        super.onLowMemory();
    }

    public Activity getCurrentActivity() {
        return activity;
    }

    public static App get() {
        return SingletonHolder.INSTANCE;
    }
}