package com.redrocket.photoeditor.screens.presentation.gallery.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * @TODO Class Description ...
 */
public class SquareFrameLayout extends FrameLayout {
    private static final String TAG = "SquareFrameLayout";
    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        Log.i(TAG, "onMeasure: "+getMeasuredWidth()+ " "+getMeasuredHeight());
    }
}
