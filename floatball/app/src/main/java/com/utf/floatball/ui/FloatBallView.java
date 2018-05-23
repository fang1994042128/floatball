package com.utf.floatball.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.utf.floatball.MyApplication;
import com.utf.floatball.R;
import com.utf.floatball.utils.DensityUtil;
import com.utf.floatball.utils.PreferenceConstants;
import com.utf.floatball.utils.PreferencesUtils;

import java.lang.reflect.Field;

/**
 * Created by utf on 2018/4/2.
 */
public class FloatBallView extends LinearLayout {

    private int mFloatBallViewWidth = 0;
    private int mFloatBallViewHeight = 0;
    private int mStatusBarHeight = 0;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private float mInScreenX = 0f;
    private float mInScreenY = 0f;
    private float mDownInScreenX = 0f;
    private float mDownInScreenY = 0f;
    private float mInViewX = 0f;
    private float mInViewY = 0f;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private ImageView mFloatBallIcon;
    private ViewGroup mFloatBallView;
    private static final int CLICK_FLOAT_BALL_LIMIT = 10;
    private Context mContext;
    public final String STATUS_BAR_CLASS_NAME = "com.android.internal.R$dimen";
    public final String STATUS_BAR_FILE_NAME = "status_bar_height";
    public final String TAG = FloatBallView.class.getSimpleName();
    private static final int FLOAT_BALL_DEFAULT_HEIGHT = 32;

    /**
     * 悬浮球 坐落 左 右 标记
     */
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private ValueAnimator valueAnimator;
    private RotateAnimation anim;
    private Interpolator mLinearInterpolator = new LinearInterpolator();
    private int mDefaultLocation = LEFT;
    private int mHintLocation = mDefaultLocation;
    private boolean isOpenMenu = false;
    private View mRootView;
    private boolean isRotateRunning = false;
    private static final int bottomY = DensityUtil.dip2px(MyApplication.getApplicationInstance().getApplicationContext(), 120);
    private static final int topY =  DensityUtil.dip2px(MyApplication.getApplicationInstance().getApplicationContext(), 50);

    public FloatBallView(Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        View mRootView = LayoutInflater.from(context).inflate(R.layout.float_ball, null);
        mFloatBallView = mRootView.findViewById(R.id.float_ball_layout);
        mFloatBallIcon = (ImageView) mFloatBallView.findViewById(R.id.float_ball_logo);
        mScreenWidth = FloatBallViewWrapper.getInstance().getScreenWidth(context);
        mScreenHeight = FloatBallViewWrapper.getInstance().getScreenHeight(context);
        mContext = context;
        addView(mRootView);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFloatBallViewWidth = mFloatBallView.getWidth();
        mFloatBallViewHeight = mFloatBallView.getHeight();
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                 floatEventMove(event);
                break;
            case MotionEvent.ACTION_UP:
                floatEventUp(event);
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

    private void openItemMenus(MotionEvent event) {
        if (isInChangeImageZone(mFloatBallView, (int) event.getRawX(), (int)event.getRawY())) {
            openMenu();
        }
    }

    private void openMenu() {
        this.isOpenMenu = !this.isOpenMenu;
        hide();
        FloatMenuViewWrapper.getInstance().show(mContext);
        isRotateRunning = false;
//        rotateCButton(mFloatBallIcon,0,360,300);
    }

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

    private void rotateCButton(final View v, float start, float end, int duration) {
        if (anim == null) {
            anim = new RotateAnimation(start, end,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            anim.setDuration(duration);
            anim.setFillAfter(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isRotateRunning = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    hide();
                    FloatMenuViewWrapper.getInstance().show(mContext);
                    isRotateRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (!isRotateRunning) {
            v.startAnimation(anim);
        }
    }

    private void hide() {
        if (mFloatBallView.getVisibility() == VISIBLE) {
            mFloatBallView.setVisibility(INVISIBLE);
        }
    }

    public void show() {
        if (mFloatBallView.getVisibility() == INVISIBLE) {
            mFloatBallView.setVisibility(VISIBLE);
        }
    }

    public View getView() {
        return mFloatBallIcon;
    }

    private void floatEventMove(MotionEvent event) {
        mInScreenX = event.getRawX();
        mInScreenY = event.getRawY() - getStatusBarHeight();

        if (mParams == null) {
            return;
        }
//        if (mParams.y - (topY + mStatusBarHeight) < 0) {
//            mParams.y = topY + mStatusBarHeight;
//        } else if ((mScreenHeight - mParams.y) < bottomY) {
//            mParams.y = mScreenHeight - (bottomY + mStatusBarHeight);
//        } else {
//            mParams.y = (int) (mInScreenY - mInViewY);
//        }
        mParams.x = (int) (mInScreenX - mInViewX);
        mParams.y = (int) (mInScreenY - mInViewY);
        saveFloatBallPoint(mParams.x, mParams.y);
        mWindowManager.updateViewLayout(this, mParams);
    }

    private void floatEventUp(MotionEvent event) {

        if (mInScreenX < mScreenWidth / 2) {   //在左边
            mHintLocation = LEFT;
        } else {                   //在右边
            mHintLocation = RIGHT;
        }

        final int heightTop = topY + mStatusBarHeight;
        final int heightBootom = bottomY + mStatusBarHeight;

        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(64);
            valueAnimator.setInterpolator(mLinearInterpolator);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int mResetLocationValue = (int) animation.getAnimatedValue();
                    if (mParams.x > 0 && mParams.x < mScreenWidth) {
                        if (mHintLocation == LEFT) {
                            mParams.x = mParams.x - mResetLocationValue;
                        } else {
                            mParams.x = mParams.x + mResetLocationValue;
                        }
                        if (mParams.y - heightTop <= 0) {
                            mParams.y = heightTop;
                        }else if ((mScreenHeight - mParams.y) <= heightBootom) {
                            mParams.y = mScreenHeight - heightBootom;
                        }
                        try {
                            saveFloatBallPoint(mParams.x, mParams.y);
                            mWindowManager.updateViewLayout(FloatBallView.this, mParams);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (Math.abs(mParams.x) < 0) {
                        mParams.x = 0;
                    } else if (Math.abs(mParams.x) > mScreenWidth) {
                        mParams.x = mScreenWidth;
                    }
                    if (mParams.y - heightTop <= 0) {
                        mParams.y = heightTop;
                    }else if ((mScreenHeight - mParams.y) <= heightBootom) {
                        mParams.y = mScreenHeight - heightBootom;
                    }
                    saveFloatBallPoint(mParams.x, mParams.y);
                    mWindowManager.updateViewLayout(FloatBallView.this, mParams);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    if (Math.abs(mParams.x) < 0) {
                        mParams.x = 0;
                    } else if (Math.abs(mParams.x) > mScreenWidth) {
                        mParams.x = mScreenWidth;
                    }
                    saveFloatBallPoint(mParams.x, mParams.y);
                    mWindowManager.updateViewLayout(FloatBallView.this, mParams);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }

    }

    private void saveFloatBallPoint(int pointX, int pointY) {
        PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_X, pointX);
        PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_Y, pointY);
        PreferencesUtils.getInstance().putInt(PreferenceConstants.SAVE_FLOAT_POSTION, mHintLocation);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
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

    public void setFloatBallIcon() {
        if (mFloatBallIcon == null) {
            return;
        }
        mFloatBallIcon.setVisibility(VISIBLE);
        mFloatBallIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mFloatBallIcon.setImageResource(R.mipmap.ic_launcher);
    }
}
