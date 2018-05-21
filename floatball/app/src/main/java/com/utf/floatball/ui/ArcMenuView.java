package com.utf.floatball.ui;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.utf.floatball.R;
import com.utf.floatball.utils.DensityUtil;
import com.utf.floatball.utils.PreferenceConstants;
import com.utf.floatball.utils.PreferencesUtils;


/**
 * Created by utf on 2018/3/28.
 */

public class ArcMenuView extends LinearLayout {

    public enum Status{
        OPEN,CLOSE
    }

    public enum Position{
        LEFT, RIGHT
    }
    private Position mPosition = Position.LEFT;
    //主菜的单按钮
    private View mCButton;
    private int pointX;
    private int pointY;

    public ArcMenuView(Context context) {
        this(context, null);
    }

    public ArcMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getPostion();
    }

    public void getPostion() {
        int postion =   PreferencesUtils.getInstance().getInt(PreferenceConstants.SAVE_FLOAT_POSTION, 0);
        mPosition = postion == 0 ? Position.LEFT : Position.RIGHT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0; i < count; i ++){
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getPostion();
        int x = PreferencesUtils.getInstance().getInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_X, 0);
        int y = PreferencesUtils.getInstance().getInt(PreferenceConstants.SAVE_FLOAT_BALL_POINT_Y, 0);

        if (x < getMeasuredWidth()/2) {
            mPosition = Position.LEFT;
        } else if (x >= getMeasuredWidth()/2) {
            mPosition = Position.RIGHT;
        }
        layoutCButton(x , y);
        layoutMenuItems(x, y);
    }

    /**
     * 布局主菜单项
     */
    private void layoutCButton(int x, int y) {
        mCButton = getChildAt(0);
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();
        int measureHeight = getMeasuredHeight()/2;
        int l = getMeasuredWidth();
        if (mPosition == Position.LEFT) {
            mCButton.layout(0, y, width, y + height);
        } else if (mPosition == Position.RIGHT) {
            mCButton.layout(getMeasuredWidth() - width, y, getMeasuredWidth(), y + height);
        }
    }


    public void addMenuView(@DrawableRes int id, Context context) {
        CircleImageView imageView = new CircleImageView(context);
        imageView.setImageResource(id);
        imageView.setBorderWidth(DensityUtil.dip2px(context, 3));
        imageView.setBorderColor(R.color.black);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height =   DensityUtil.dip2px(context, 44);
        params.width =  DensityUtil.dip2px(context, 44);
        addView(imageView, params);
    }


    public View getMenuView(int postion) {
        return getChildAt(postion);
    }


    /**
     * 确定MeunItem位置
     * @param pointX
     * @param pointY
     */
    private void layoutMenuItems(int pointX, int pointY) {
        int count = getChildCount();
        if (count <= 1) {
            return;
        }
        mCButton = getChildAt(0);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        int height = mCButton.getHeight();
        int width = mCButton.getWidth();
        float itemRadius = DensityUtil.dip2px(getContext(), 80);
        RectF area = null;
        Path path = new Path();
        if (mPosition == Position.LEFT) {
            area = new RectF(
                    width/ 2- itemRadius,
                    pointY - itemRadius + height/2,
                         width/2 + itemRadius,
                    pointY + itemRadius + height/2);
            path.addArc(area, -60, (float) (150));
        } else {
            area = new RectF(
                    measuredWidth - itemRadius - width/2,
                    pointY - itemRadius + height/2,
                    measuredWidth + itemRadius - width/2,
                    pointY+ itemRadius + height/2);
            path.addArc(area, -120, (float) (-150));
        }
        PathMeasure measure = new PathMeasure(path, false);
        float len = measure.getLength();
        int divisor = count - 1;
        float divider = len / divisor;
        for (int i = 1; i <= divisor ; i++) {
            View childrenView = getChildAt(i);
            float[] coords = new float[2];
            measure.getPosTan((i - 1) * divider , coords, null);
            int x = (int) coords[0] - childrenView.getMeasuredWidth() / 2;
            int y = (int) coords[1] - childrenView.getMeasuredHeight() / 2;
            childrenView.layout(x, y, x + childrenView.getMeasuredWidth()
                    , y + childrenView.getMeasuredHeight());
        }
    }
}
