package com.utf.floatball.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Created by zhangtongyu on 2016/10/19.
 */
public class DensityUtil {

    public static final float getHeightInPx(Context context) {
        final float height = (float) context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final float getWidthInPx(Context context) {
        final float width = (float) context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = (float) context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float height = (float) context.getResources().getDisplayMetrics().heightPixels;
        int widthInDp = px2dip(context, height);
        return widthInDp;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    // 获取屏幕高度和宽度
    public static Rect getScreenPx(Context context) {
        return new Rect(0, 0, context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }
}
