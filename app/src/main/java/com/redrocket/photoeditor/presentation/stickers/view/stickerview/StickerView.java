package com.redrocket.photoeditor.presentation.stickers.view.stickerview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.redrocket.photoeditor.R;

/**
 * Вьюшка для стикера.
 * Помещается внутрь {@link StickerBoard}.
 */
class StickerView extends FrameLayout implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "StickerFrame";

    private static final String BUNDLE_STICKER_ID = "BUNDLE_STICKER_ID";
    private static final String BUNDLE_IMAGE_RES_ID = "BUNDLE_IMAGE_RES_ID";
    private static final String BUNDLE_CORNER_X = "BUNDLE_CORNER_X";
    private static final String BUNDLE_CORNER_Y = "BUNDLE_CORNER_Y";
    private static final String BUNDLE_WIDTH = "BUNDLE_WIDTH";
    private static final String BUNDLE_HEIGHT = "BUNDLE_HEIGHT";
    private static final String BUNDLE_MIN_WIDTH = "BUNDLE_MIN_WIDTH";
    private static final String BUNDLE_MIN_HEIGHT = "BUNDLE_MIN_HEIGHT";
    private static final String BUNDLE_ROTATE = "BUNDLE_ROTATE";
    private static final String BUNDLE_IS_SELECTED = "BUNDLE_IS_SELECTED";

    private static final int SELF_MEASURE_SPEC = MeasureSpec.makeMeasureSpec(
            0,
            MeasureSpec.UNSPECIFIED);

    private static final int[] TMP_LOCATION = new int[2];

    private final ImageView mStickerImage;
    private final View mCrossView;
    private final View mStretchView;

    private int mStickerId;
    private int mImage;
    private int mMinStickerWidth;
    private int mMinStickerHeight;
    private float mStickerRatio;

    private final PointF mCenterOnScreen = new PointF();
    private final PointF mCenterOnParent = new PointF();

    private final StretchHandler mStretchHandler = new StretchHandler();
    private final MoveHandler mMoveHandler = new MoveHandler();

    private StickerViewListener mListener;

    StickerView(Context context,
                int minWidth, int minHeight,
                final StickerState sticker,
                StickerViewListener listener) {
        this(context);

        mStickerId = sticker.id;

        mImage = sticker.image;
        mStickerImage.setImageResource(mImage);
        mStickerRatio = computeRatio(mImage);

        mMinStickerWidth = minWidth;
        mMinStickerHeight = minHeight;

        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

                View parent = ((View) getParent());
                int widthBound = (int) (sticker.dims.first * parent.getWidth());
                int heightBound = (int) (sticker.dims.second * parent.getHeight());

                float boundRatio = (float) widthBound / (float) heightBound;
                int stickerWidth;
                int stickerHeight;

                if (mStickerRatio > boundRatio) {
                    stickerWidth = widthBound;
                    stickerHeight = (int) (stickerWidth / mStickerRatio);
                } else {
                    stickerHeight = heightBound;
                    stickerWidth = (int) (stickerHeight * mStickerRatio);
                }

                ViewGroup.LayoutParams params = mStickerImage.getLayoutParams();
                params.width = stickerWidth;
                params.height = stickerHeight;
                mStickerImage.setLayoutParams(params);

                float centerX = sticker.center.first * parent.getWidth();
                float centerY = sticker.center.second * parent.getHeight();

                measure(SELF_MEASURE_SPEC, SELF_MEASURE_SPEC);
                float cornerX = centerX - getMeasuredWidth() / 2f;
                float cornerY = centerY - getMeasuredHeight() / 2f;

                moveTo(cornerX, cornerY);

                setRotation(sticker.rotation);

                removeOnAttachStateChangeListener(this);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {}
        });

        mListener = listener;
    }

    StickerView(Context context, final Bundle state, StickerViewListener listener) {
        this(context);

        mStickerId = state.getInt(BUNDLE_STICKER_ID);

        mImage = state.getInt(BUNDLE_IMAGE_RES_ID);
        mStickerImage.setImageResource(mImage);
        mStickerRatio = computeRatio(mImage);

        mMinStickerWidth = state.getInt(BUNDLE_MIN_WIDTH);
        mMinStickerHeight = state.getInt(BUNDLE_HEIGHT);

        int stickerWidth = state.getInt(BUNDLE_WIDTH);
        int stickerHeight = state.getInt(BUNDLE_HEIGHT);
        ViewGroup.LayoutParams params = mStickerImage.getLayoutParams();
        params.width = stickerWidth;
        params.height = stickerHeight;
        mStickerImage.setLayoutParams(params);

        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                measure(SELF_MEASURE_SPEC, SELF_MEASURE_SPEC);
                float cornerX = state.getFloat(BUNDLE_CORNER_X);
                float cornerY = state.getFloat(BUNDLE_CORNER_Y);
                moveTo(cornerX, cornerY);
                removeOnAttachStateChangeListener(this);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {}
        });

        setRotation(state.getFloat(BUNDLE_ROTATE));
        setSelected(state.getBoolean(BUNDLE_IS_SELECTED));

        mListener = listener;
    }

    private StickerView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_sticker_frame, this, true);

        mStickerImage = (ImageView) findViewById(R.id.image_sticker);
        mCrossView = findViewById(R.id.button_cross);
        mStretchView = findViewById(R.id.button_stretch);

        setOnTouchListener(this);
        mStretchView.setOnTouchListener(this);
        mCrossView.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == this) {
            return mMoveHandler.touch(event);
        } else if (v == mStretchView) {
            return mStretchHandler.touch(event);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCrossView) {
            mListener.onCloseClick(StickerView.this);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mStretchView.setVisibility(selected ? VISIBLE : INVISIBLE);
        mCrossView.setVisibility(selected ? VISIBLE : INVISIBLE);
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        mCrossView.setRotation(-rotation);
        mStretchView.setRotation(-rotation);
    }

    StickerState getStickerState() {
        View parent = ((View) getParent());
        float centerX = mCenterOnParent.x / parent.getWidth();
        float centerY = mCenterOnParent.y / parent.getHeight();

        ViewGroup.LayoutParams params = mStickerImage.getLayoutParams();
        float width = (float) params.width / (float) parent.getWidth();
        float height = (float) params.height / (float) parent.getHeight();

        return new StickerState(
                mStickerId,
                mImage,
                new Pair<>(centerX, centerY),
                new Pair<>(width, height),
                getRotation());
    }

    Bundle getViewState() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_STICKER_ID, mStickerId);

        bundle.putInt(BUNDLE_IMAGE_RES_ID, mImage);

        bundle.putFloat(BUNDLE_CORNER_X, getX());
        bundle.putFloat(BUNDLE_CORNER_Y, getY());

        ViewGroup.LayoutParams params = mStickerImage.getLayoutParams();
        bundle.putInt(BUNDLE_WIDTH, params.width);
        bundle.putInt(BUNDLE_HEIGHT, params.height);

        bundle.putInt(BUNDLE_MIN_WIDTH, mMinStickerWidth);
        bundle.putInt(BUNDLE_MIN_HEIGHT, mMinStickerHeight);

        bundle.putFloat(BUNDLE_ROTATE, getRotation());

        bundle.putBoolean(BUNDLE_IS_SELECTED, isSelected());

        return bundle;
    }

    private void moveTo(float x, float y) {
        setX(x);
        setY(y);

        refreshCenterOnParent();
        refreshCenterOnScreen();
    }

    private void refreshCenterOnParent() {
        float centerX = getX() + getMeasuredWidth() / 2f;
        float centerY = getY() + getMeasuredHeight() / 2f;

        mCenterOnParent.set(centerX, centerY);
    }

    private void refreshCenterOnScreen() {
        ((ViewGroup) getParent()).getLocationOnScreen(TMP_LOCATION);

        float centerX = TMP_LOCATION[0] + getX() + getMeasuredWidth() / 2f;
        float centerY = TMP_LOCATION[1] + getY() + getMeasuredHeight() / 2f;

        mCenterOnScreen.set(centerX,centerY);
    }

    private float computeAngle(float x, float y) {
        float deltaX = x - mCenterOnScreen.x;
        float deltaY = y - mCenterOnScreen.y;

        float angle = (float) Math.toDegrees(Math.atan(deltaY / deltaX));
        if (deltaX < 0) {
            angle = 180 + angle;
        }

        return angle;
    }

    private void updateStickerSize(int width, int height) {
        ViewGroup.LayoutParams params = mStickerImage.getLayoutParams();
        params.width = width;
        params.height = height;
        mStickerImage.setLayoutParams(params);

        measure(SELF_MEASURE_SPEC, SELF_MEASURE_SPEC);

        float cornerX = mCenterOnParent.x - getMeasuredWidth() / 2f;
        float cornerY = mCenterOnParent.y - getMeasuredHeight() / 2f;

        setX(cornerX);
        setY(cornerY);
    }

    private float computeRatio(@DrawableRes int image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getContext().getResources(), image, options);

        return (float) options.outWidth / (float) options.outHeight;
    }

    interface StickerViewListener {
        void onDownTouch(StickerView sticker);

        void onCloseClick(StickerView sticker);
    }

    private class StretchHandler {
        private int mBaseWidth;
        private int mBaseHeight;
        private float mTouchDownRadius;

        private float mBaseRotation;
        private float mTouchDownAngle;

        public boolean touch(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mListener.onDownTouch(StickerView.this);

                ViewGroup.LayoutParams params = mStickerImage.getLayoutParams();
                mBaseWidth = params.width;
                mBaseHeight = params.height;

                float deltaX = event.getRawX() - mCenterOnScreen.x;
                float deltaY = event.getRawY() - mCenterOnScreen.y;
                mTouchDownRadius = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                mBaseRotation = getRotation();
                mTouchDownAngle = computeAngle(event.getRawX(), event.getRawY());
            } else {
                handleStretch(event.getRawX(), event.getRawY());
            }

            return true;
        }

        private void handleStretch(float touchX, float touchY) {
            float deltaX = touchX - mCenterOnScreen.x;
            float deltaY = touchY - mCenterOnScreen.y;

            float radius = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            float deltaRadius = radius - mTouchDownRadius;

            float deltaWidth = 2 * (float) (deltaRadius / Math.sqrt(1 / (mStickerRatio * mStickerRatio) + 1));
            float deltaHeight = deltaWidth / mStickerRatio;

            int stickerWidth = mBaseWidth + (int) deltaWidth;
            int stickerHeight = mBaseHeight + (int) deltaHeight;

            if (stickerWidth >= mMinStickerWidth || stickerHeight >= mMinStickerHeight) {
                updateStickerSize(stickerWidth, stickerHeight);
            }


            float angle = computeAngle(touchX, touchY);
            float deltaAngle = angle - mTouchDownAngle;

            setRotation(mBaseRotation + deltaAngle);
        }
    }

    private class MoveHandler {
        private float mBaseX;
        private float mBaseY;
        private float mTouchDownX;
        private float mTouchDownY;

        public boolean touch(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mListener.onDownTouch(StickerView.this);

                mBaseX = getX();
                mBaseY = getY();

                mTouchDownX = event.getRawX();
                mTouchDownY = event.getRawY();
            } else {
                handleMove(event.getRawX(), event.getRawY());
            }
            return true;
        }

        private void handleMove(float touchX, float touchY) {
            float deltaX = touchX - mTouchDownX;
            float deltaY = touchY - mTouchDownY;

            moveTo(mBaseX + deltaX, mBaseY + deltaY);
        }
    }
}
