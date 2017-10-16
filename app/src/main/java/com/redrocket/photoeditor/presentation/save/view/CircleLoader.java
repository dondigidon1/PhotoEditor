package com.redrocket.photoeditor.presentation.save.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.redrocket.photoeditor.R;

/**
 * Виджет бесконечного кругового загрузчика.
 */
public class CircleLoader extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = "CircleLoader";

    private static final float START_ANGLE_OFFSET = 270;

    private final int mRingWidth;
    private final float mRollerSweepAngle;

    private RectF mRingRect;

    private final Paint mRingPaint;
    private final Paint mRollerPaint;

    private float mRollerStartAngle = 0;

    private final ValueAnimator mAnimator;

    public CircleLoader(Context context) {
        this(context, null);
    }

    public CircleLoader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleLoader,
                0, 0);

        mRingWidth = typedArray.getDimensionPixelSize(R.styleable.CircleLoader_ringWidth, 20);
        float rollerWidth = typedArray.getDimensionPixelSize(R.styleable.CircleLoader_rollerWidth, 14);
        mRollerSweepAngle = typedArray.getFloat(R.styleable.CircleLoader_rollerSweepAngle, 60);
        int ringColor = typedArray.getColor(R.styleable.CircleLoader_ringColor, Color.GREEN);
        int rollerColor = typedArray.getColor(R.styleable.CircleLoader_rollerColor, Color.RED);
        int animDuration = typedArray.getInteger(R.styleable.CircleLoader_animDuration, 1000);

        typedArray.recycle();

        mRingPaint = new Paint();
        mRingPaint.setColor(ringColor);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setAntiAlias(true);

        mRollerPaint = new Paint();
        mRollerPaint.setColor(rollerColor);
        mRollerPaint.setStrokeWidth(rollerWidth);
        mRollerPaint.setStyle(Paint.Style.STROKE);
        mRollerPaint.setAntiAlias(true);
        mRollerPaint.setStrokeCap(Paint.Cap.ROUND);

        mAnimator = ValueAnimator.ofFloat(0, 360);
        mAnimator.setDuration(animDuration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimator.cancel();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = getWidth();
        int height = getHeight();
        int minSide = width < height ? width : height;
        float radius = minSide / 2f - mRingWidth / 2f;

        PointF center = new PointF(getWidth() / 2f, getHeight() / 2f);

        mRingRect = new RectF(
                center.x - radius,
                center.y - radius,
                center.x + radius,
                center.y + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mRingRect, 0, 360, false, mRingPaint);
        canvas.drawArc(
                mRingRect,
                mRollerStartAngle + START_ANGLE_OFFSET,
                mRollerSweepAngle,
                false,
                mRollerPaint);
    }

    private void setStartAngle(float angle) {
        mRollerStartAngle = angle;
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setStartAngle((float) animation.getAnimatedValue());
    }
}
