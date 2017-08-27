package com.redrocket.photoeditor.presentation.effects.view.previews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.redrocket.photoeditor.R;

/**
 * Вью для выделения выбранного эффекта.
 * Подсвечивает превью эффекта когда он выбран.
 */
public class HighlightView extends View {
    private static final String TAG = "HighlightView";

    private final Paint mFramePaint;
    private Rect mFrameRect;

    private Paint mGradientPaint;
    private final int mStartColor;
    private final int mEndColor;

    private int mRadius;

    public HighlightView(Context context) {
        this(context, null);
    }

    public HighlightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.HighlightView, 0, 0);
        mStartColor = typedArray.getColor(R.styleable.HighlightView_startColor, Color.RED);
        mEndColor = typedArray.getColor(R.styleable.HighlightView_endColor, Color.BLUE);
        int frameColor = typedArray.getColor(
                R.styleable.HighlightView_frameColor, Color.GREEN);
        int frameWidth = typedArray.getDimensionPixelSize(
                R.styleable.HighlightView_frameWidth, -1);
        typedArray.recycle();

        mFramePaint = new Paint();
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(frameWidth);
        mFramePaint.setColor(frameColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mGradientPaint = new Paint();
        mGradientPaint.setStyle(Paint.Style.FILL);

        int halfSide = getWidth() / 2;
        mRadius = (int) Math.sqrt(2 * halfSide * halfSide);
        mGradientPaint.setShader(new RadialGradient(getWidth() / 2, getHeight() / 2,
                mRadius, new int[]{mStartColor, mEndColor}, null, Shader.TileMode.CLAMP));

        mFrameRect = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mGradientPaint);
        canvas.drawRect(mFrameRect, mFramePaint);
    }
}
