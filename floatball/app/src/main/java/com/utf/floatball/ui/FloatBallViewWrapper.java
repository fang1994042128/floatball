package com.utf.floatball.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.utf.floatball.MyApplication;
import com.utf.floatball.utils.DensityUtil;
import com.utf.floatball.utils.PreferenceConstants;
import com.utf.floatball.utils.PreferencesUtils;

/**
 * Created by utf on 2018/5/18.
 */

public class FloatBallViewWrapper {
    private static FloatBallView mFloatBall;
    private static WindowManager.LayoutParams mFloatBallParams;
    private static WindowManager mWindowManager;
    private int mFloatBallInScreenX = 0;
    private int mFloatBallInScreenY = 0;
    private static final int LEFT_FLOAT_BALL_POSITION = 0;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            }
        }
    };


    public static FloatBallViewWrapper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static final class SingletonHolder {
        private static final FloatBallViewWrapper sInstance = new FloatBallViewWrapper();
    }

    public int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        return DensityUtil.getScreenPx(context).width();
    }

    public int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = getWindowManager(context);
        if (windowManager == null) {
            return 0;
        }
        return windowManager.getDefaultDisplay().getHeight();
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private void createFloatBall(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (windowManager == null) {
            return;
        }
        if (mFloatBall == null) {
            mFloatBall = new FloatBallView(context);
            if (mFloatBallParams == null) {
                mFloatBallParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                    mFloatBallParams.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mFloatBallParams.type= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
                mFloatBallParams.format = PixelFormat.RGBA_8888;
                mFloatBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mFloatBallParams.gravity = Gravity.LEFT | Gravity.TOP;
                mFloatBallParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                mFloatBallParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            setFloatBallPosition(context);
            PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_X, mFloatBallParams.x);
            PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_Y, mFloatBallParams.y);
            mFloatBall.setParams(mFloatBallParams);
            if (windowManager == null) {
                return;
            }

            try {
                windowManager.addView(mFloatBall, mFloatBallParams);
            } catch(Exception e) {
                e.printStackTrace();
            }
            setFloatBallIcon();
        }
    }


    private void setFloatBallIcon() {
        if (mFloatBall == null) {
            return;
        }
        mFloatBall.setFloatBallIcon();
    }

    private void setFloatBallPosition(Context context) {
        int pointY = PreferencesUtils.getInstance().getInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_Y, 0);
        if (pointY == 0) {
            boolean isLeft = true;
            if (isLeft) {
                mFloatBallParams.x = LEFT_FLOAT_BALL_POSITION;
                PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_POSTION, 0);
            } else {
                mFloatBallParams.x = getWindowManager(context).getDefaultDisplay().getWidth();
                PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_POSTION, 1);
            }
            mFloatBallParams.y = mFloatBallInScreenY;
        } else {
            int poinX = PreferencesUtils.getInstance().getInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_X, 0);
            mFloatBallParams.x = poinX;
            mFloatBallParams.y = pointY;
        }
    }


    public void showFloatBall() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFloatBall == null) {
                    Context context = MyApplication.getApplicationInstance().getApplicationContext();
                    createFloatBall(context);
                }
                if (mFloatBall.getVisibility() != View.VISIBLE) {
                    mFloatBall.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void hideFloatBall() {
        if (mHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFloatBall == null) {
                    return;
                }
                if (mFloatBall.getVisibility() == View.VISIBLE) {
                    mFloatBall.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void show() {
        mFloatBall.show();
    }

    public void removeFloatBall(final Context context) {
        if (context == null) {
            return;
        }
        if (mHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFloatBall != null) {
                    WindowManager windowManager = getWindowManager(context);
                    windowManager.removeView(mFloatBall);
                    mFloatBall = null;
                }
            }
        });
    }
}
