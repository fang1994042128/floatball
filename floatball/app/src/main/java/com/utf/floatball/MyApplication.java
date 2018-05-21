package com.utf.floatball;

import android.app.Application;

/**
 * Created by utf on 2018/5/18.
 */

public class MyApplication  extends Application{
    private  static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApplication getApplicationInstance() {
        if (mInstance == null) {
            synchronized (MyApplication.class) {
                if (mInstance == null) {
                    mInstance = new MyApplication();
                }
            }
        }
        return mInstance;
    }
}
