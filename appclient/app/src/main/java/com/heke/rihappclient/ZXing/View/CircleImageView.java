package com.heke.rihappclient.ZXing.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * Created by wgmhx on 2017/3/18.
 */

/**
        * 圆形图片控件
        * @author Kevin
        */
public class CircleImageView extends ImageView {

    private static final int DEFAULT_SIZE = 72;
    private static final int DEFAULT_RADIUS_DELTA = 8;
    private static final int DEFAULT_STOKE_WIDTH = 4;
    public static final int TYPE_FILL = 0;
    public static final int TYPE_STROKE = 1;
    private int mRadiusDelta = DEFAULT_RADIUS_DELTA;
    private float mRadius;
    private int mCircleX;
    private int mCircleY;
    private Paint mPaint;
    private int mDisableColor = Color.GRAY;
    private int mNormalColor = Color.WHITE;
    private int mPressedColor = Color.BLUE;
    private int mCircleType = TYPE_STROKE;
    private int mStrokeWidth = DEFAULT_STOKE_WIDTH;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        setBackgroundColors(Color.WHITE, getResources().getColor(android.R.color.holo_blue_bright));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (widthMode != MeasureSpec.EXACTLY) {
                widthSize = drawable.getIntrinsicWidth() + getPaddingLeft() + getPaddingRight();
            }

            if (heightMode != MeasureSpec.EXACTLY) {
                heightSize = drawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();
            }
        } else {
            widthSize = heightSize = DEFAULT_SIZE;
        }

        int width = widthSize - getPaddingLeft() - getPaddingRight();
        int height = heightSize - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        mRadius = Math.max(0, size - mStrokeWidth) / 2;

        mCircleX = width / 2 + getPaddingLeft();
        mCircleY = height / 2 + getPaddingTop();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRadius == 0) {
            super.onDraw(canvas);
            return;
        }

        drawCircleBackground(canvas);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int padding = mRadiusDelta * 2;
            int maxRadus = (int) mRadius;
            drawable.setBounds(mCircleX - maxRadus + padding, mCircleY - maxRadus + padding,
                    mCircleX + maxRadus - padding, mCircleY + maxRadus - padding);
            drawable.draw(canvas);
        }
    }

    private void drawCircleBackground(Canvas canvas) {
        mPaint.setColor(isEnabled() ? (isPressed() ? mPressedColor : mNormalColor) : mDisableColor);

        if (mCircleType == TYPE_STROKE && !isPressed()) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }

        canvas.drawCircle(mCircleX, mCircleY, mRadius, mPaint);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        postInvalidate();
    }

    /**
     * 设置圆形背景颜色
     * @param normal 常态颜色
     * @param pressed 检查状态颜色
     */
    public void setBackgroundColors(int normal, int pressed) {
        mNormalColor = normal;
        mPressedColor = pressed;
        postInvalidate();
    }

    /**
     * 设置圆形背景类型
     * @param type 类型：实心{@link #TYPE_FILL}、空心{@link #TYPE_STROKE}
     */
    public void setCircleType(int type) {
        if (mCircleType == type) return;

        if (type == TYPE_STROKE) {
            mCircleType = type;
            mStrokeWidth = DEFAULT_STOKE_WIDTH;
        } else {
            mCircleType = TYPE_FILL;
            mStrokeWidth = 0;
        }

        postInvalidate();
    }

    /**
     * 设置空心圆宽度，当圆形背景类型为空心{@link #TYPE_STROKE}时才生效
     * @param width 宽度
     */
    public void setStrokeWidth(int width) {
        if (mCircleType == TYPE_FILL) {
            mStrokeWidth = 0;
            return;
        }

        if (mStrokeWidth == width) return;
        mStrokeWidth = width;
        postInvalidate();
    }
}

