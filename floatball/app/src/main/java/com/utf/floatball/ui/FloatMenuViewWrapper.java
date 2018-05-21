package com.utf.floatball.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;


/**
 * Created by utf on 2018/4/2.
 */

public class FloatMenuViewWrapper {
    public static final String TAG = FloatMenuViewWrapper.class.getSimpleName();
    private static WindowManager.LayoutParams mFloatMenuParams;
    private static WindowManager mWindowManager;
    private static FloatMenuView mMenuView;


    private static final class SingletonHolder {
        private static final FloatMenuViewWrapper sInstance = new FloatMenuViewWrapper();
    }

    public static FloatMenuViewWrapper getInstance() {
        return FloatMenuViewWrapper.SingletonHolder.sInstance;
    }

    public void createView(Context context) {

        WindowManager windowManager = getWindowManager(context);
        if (windowManager == null) {
            return;
        }
        if (mMenuView == null) {
            mMenuView = new FloatMenuView(context);
            if (mFloatMenuParams == null) {
                mFloatMenuParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                    mFloatMenuParams.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mFloatMenuParams.type= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
                mFloatMenuParams.format = PixelFormat.RGBA_8888;
                mFloatMenuParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mFloatMenuParams.gravity = Gravity.LEFT | Gravity.TOP;
                mFloatMenuParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                mFloatMenuParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            }
            mMenuView.setParams(mFloatMenuParams);
            if (windowManager == null) {
                return;
            }

            try {
                windowManager.addView(mMenuView, mFloatMenuParams);
            } catch(Exception e) {
                e.printStackTrace();
                Log.e(TAG, "windowManager addView fail!");
            }
        }
    }


    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    public void show(Context context) {
        if (mMenuView == null) {
            createView(context);
            return;
        }
    }

    public void hide() {
        if (mMenuView == null) {
            return;
        }
        try {
            mWindowManager.removeViewImmediate(mMenuView);
            mMenuView = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
