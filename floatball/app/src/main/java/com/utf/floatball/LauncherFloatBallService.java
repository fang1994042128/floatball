package com.utf.floatball;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.utf.floatball.ui.FloatBallViewWrapper;

public class LauncherFloatBallService extends Service {
    public static final String TAG = LauncherFloatBallService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        operatingFloatBall(true);
    }

    @Override
    public void onDestroy() {
        FloatBallViewWrapper.getInstance().removeFloatBall(MyApplication.getApplicationInstance().getApplicationContext());
        super.onDestroy();
    }


    public static void startService() {
        Context context = MyApplication.getApplicationInstance().getApplicationContext();
        Intent intent = new Intent(context, LauncherFloatBallService.class);
        try {
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopService() {
        Context context = MyApplication.getApplicationInstance().getApplicationContext();
        Intent intent = new Intent(context, LauncherFloatBallService.class);
        context.stopService(intent);
    }


    private void operatingFloatBall(boolean isFloatBallShow) {
        if (isFloatBallShow) {
            FloatBallViewWrapper.getInstance().showFloatBall();
        } else {
            FloatBallViewWrapper.getInstance().hideFloatBall();
        }
    }


}
