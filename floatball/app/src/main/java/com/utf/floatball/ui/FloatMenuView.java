package com.utf.floatball.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.utf.floatball.MainActivity;
import com.utf.floatball.MyApplication;
import com.utf.floatball.R;

import java.lang.reflect.Field;

/**
 * Created by utf on 2018/4/2.
 */

public class FloatMenuView extends LinearLayout {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private Context mContext;
    private ArcMenuView arcMenuLayout;
    private ImageView mFloatMenuIcon;
    private ViewGroup mFloatMenuView;
    private View mHomeMenuIcon;

    private float mInScreenX = 0f;
    private float mInScreenY = 0f;
    private float mDownInScreenX = 0f;
    private float mDownInScreenY = 0f;
    private float mInViewX = 0f;
    private float mInViewY = 0f;
    private int mStatusBarHeight = 0;
    public final String STATUS_BAR_CLASS_NAME = "com.android.internal.R$dimen";
    public final String STATUS_BAR_FILE_NAME = "status_bar_height";
    private static final int CLICK_FLOAT_BALL_LIMIT = 10;


    public FloatMenuView(Context context) {
        super(context);
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.window_menu, this);
        arcMenuLayout = findViewById(R.id.arcMenu);
        mFloatMenuView = arcMenuLayout.findViewById(R.id.float_menu_layout);
        mFloatMenuIcon = arcMenuLayout.findViewById(R.id.float_menu_logo);
        addMenuViewInfo();
    }

    private int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            try {
                Class<?> c = Class.forName(STATUS_BAR_CLASS_NAME);
                Object o = c.newInstance();
                Field field = c.getField(STATUS_BAR_FILE_NAME);
                int x = (Integer) field.get(o);
                mStatusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mStatusBarHeight;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInViewX = event.getX();
                mInViewY = event.getY();
                mDownInScreenX = event.getRawX();
                mDownInScreenY = event.getRawY() - getStatusBarHeight();
                mInScreenX = event.getRawX();
                mInScreenY = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                boolean isClick = (mInScreenX > (mDownInScreenX - CLICK_FLOAT_BALL_LIMIT)) &&
                        (mInScreenX <= (mDownInScreenX + CLICK_FLOAT_BALL_LIMIT)) &&
                        (mInScreenY > (mDownInScreenY - CLICK_FLOAT_BALL_LIMIT)) &&
                        (mInScreenY <= (mDownInScreenY + CLICK_FLOAT_BALL_LIMIT));
                if (isClick) {
                    openItemMenus(event);
                }
                break;
            default:
                break;
        }
        return true;
    }



    private void addMenuViewInfo() {
        arcMenuLayout.addMenuView(R.mipmap.ic_launcher, getContext());
        int childrenCount = arcMenuLayout.getChildCount();
        if (childrenCount == 1) {
        } else {
            mHomeMenuIcon = arcMenuLayout.getMenuView(arcMenuLayout.getChildCount() - 1);
        }
    }


    /**
     * menuItems的点击事件
     * @param event
     */
    private void openItemMenus(MotionEvent event) {
        int count = arcMenuLayout.getChildCount();
        if (isInChangeImageZone(mFloatMenuIcon, (int) event.getRawX(), (int)event.getRawY())) {
            closeMenu();
        } else if (isInChangeImageZone(mHomeMenuIcon, (int) event.getRawX(), (int)event.getRawY())) {
            openMainView(MyApplication.getApplicationInstance().getApplicationContext());
            closeMenu();
        } else if(isInChangeImageZone(arcMenuLayout, (int) event.getRawX(), (int)event.getRawY())){
            closeMenu();
        }
    }

    private void closeMenu() {
        FloatBallViewWrapper.getInstance().show();
        hide();
    }

    private void hide() {
        if (mFloatMenuView.getVisibility() == View.VISIBLE) {
            FloatMenuViewWrapper.getInstance().hide();
        }
    }


    /**
     * 判断点击的某个view
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isInChangeImageZone(View view, int x, int y) {
        if (view == null) {
            return false;
        }

        Rect mChangeImageBackgroundRect = null;
        if (null == mChangeImageBackgroundRect) {
            mChangeImageBackgroundRect = new Rect();
        }
        view.getDrawingRect(mChangeImageBackgroundRect);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        mChangeImageBackgroundRect.left = location[0];
        mChangeImageBackgroundRect.top = location[1];
        mChangeImageBackgroundRect.right = mChangeImageBackgroundRect.right + location[0];
        mChangeImageBackgroundRect.bottom = mChangeImageBackgroundRect.bottom + location[1];
        return mChangeImageBackgroundRect.contains(x, y);
    }


    /**
     * 启动app
     * @param context
     */
    private void openMainView(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
}
