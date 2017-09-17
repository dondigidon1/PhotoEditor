package com.redrocket.photoeditor.presentation.stickers.view.stickerview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.Random;

/**
 * Контейнер для стикеров.
 * Отвечает за расположение, добавление, получение стикеров.
 */
public class StickerBoard extends ViewGroup implements StickerView.StickerViewListener, View.OnClickListener {
    private static final String TAG = "SimpleLayout";

    private static final int MIN_STICKER_WIDTH = 50;
    private static final int MIN_STICKER_HEIGHT = 50;
    private static final float DEFAULT_STICKER_WIDTH_BOUND = 0.2f;
    private static final float DEFAULT_STICKER_HEIGHT_BOUND = 0.2f;

    private static final int NO_SELECTED = -1;

    private final Random mRnd = new Random();
    private final Rect mTmpChildRect = new Rect();

    private boolean mLayouted = false;

    private View mSelectedSticker = null;

    public StickerBoard(Context context) {
        this(context, null);
    }

    public StickerBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClickable(true);
        setOnClickListener(this);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayouted = true;
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        int leftPos = getPaddingLeft();
        final int parentTop = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                mTmpChildRect.left = leftPos;
                mTmpChildRect.right = mTmpChildRect.left+width;

                mTmpChildRect.top = parentTop;
                mTmpChildRect.bottom = mTmpChildRect.top+height;

                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle[] childStates = new Bundle[getChildCount()];
        for (int i = 0; i < childStates.length; i++) {
            childStates[i] = ((StickerView) getChildAt(i)).getViewState();
        }

        int selectedIndex = mSelectedSticker != null ? indexOfChild(mSelectedSticker) : NO_SELECTED;

        return new SavedState(superState, childStates, selectedIndex);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        final Bundle[] childStates = savedState.childStates;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
             public void onGlobalLayout() {
                for (Bundle childState : childStates) {
                    StickerView view = new StickerView(getContext(), childState, StickerBoard.this);
                    addView(view);
                }

                if (savedState.selectedIndex != NO_SELECTED){
                    mSelectedSticker = getChildAt(savedState.selectedIndex);
                }else{
                    mSelectedSticker = null;
                }

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        selectSticker(null);
    }

    @Override
    public void onDownTouch(StickerView sticker) {
        selectSticker(sticker);
        bringChildToFront(sticker);
    }

    @Override
    public void onCloseClick(StickerView sticker) {
        if (sticker == mSelectedSticker) {
            mSelectedSticker = null;
        }
        removeView(sticker);
    }

    /**
     * Добавить новый стикер.
     *
     * @param image Ресурс с миниатюрой стикера.
     * @param id    Идентификатор стикера.
     */
    public void addNewSticker(@DrawableRes int image, int id) {
        if (!mLayouted) {
            throw new IllegalStateException();
        }

        Pair<Float, Float> center = genStickerCenter();

        StickerState sticker = new StickerState(
                id,
                image,
                center,
                new Pair<>(DEFAULT_STICKER_WIDTH_BOUND, DEFAULT_STICKER_HEIGHT_BOUND),
                0);

        StickerView view = new StickerView(
                getContext(),
                MIN_STICKER_WIDTH, MIN_STICKER_HEIGHT,
                sticker,
                this);

        addView(view);

        selectSticker(view);
    }

    /**
     * Установить начальный набор стикеров.
     *
     * @param stickers Коллекция стикеров.
     */
    public void setInitStickers(StickerState[] stickers) {
        for (StickerState sticker : stickers) {
            StickerView view = new StickerView(getContext(),
                    MIN_STICKER_WIDTH, MIN_STICKER_HEIGHT,
                    sticker,
                    this);
            addView(view);
        }
    }

    /**
     * Получить стикеры.
     *
     * @return Возвращает коллекцию стикеров.
     */
    public StickerState[] getStickers() {
        StickerState[] stickers = new StickerState[getChildCount()];
        for (int i = 0; i < stickers.length; i++) {
            stickers[i] = ((StickerView) getChildAt(i)).getStickerState();
        }

        return stickers;
    }

    /**
     * Удалить все стикеры.
     */
    public void clearStickers(){
        removeAllViews();
    }

    private void selectSticker(StickerView view) {
        if (mSelectedSticker != null) {
            mSelectedSticker.setSelected(false);
        }

        if (view != null) {
            view.setSelected(true);
            mSelectedSticker = view;
        } else {
            mSelectedSticker = null;
        }
    }

    private Pair<Float, Float> genStickerCenter() {
        float horizontalRange = 1f - DEFAULT_STICKER_WIDTH_BOUND * 2;
        float verticalRange = 1f - DEFAULT_STICKER_HEIGHT_BOUND * 2;

        return new Pair<>(
                mRnd.nextFloat() * horizontalRange + DEFAULT_STICKER_WIDTH_BOUND,
                mRnd.nextFloat() * verticalRange + DEFAULT_STICKER_HEIGHT_BOUND);
    }


    static class SavedState extends BaseSavedState {
        final Bundle[] childStates;
        final int selectedIndex;

        SavedState(Parcelable superState, Bundle[] childStates, int selectedIndex) {
            super(superState);
            this.childStates = childStates;
            this.selectedIndex = selectedIndex;
        }

        private SavedState(Parcel in) {
            super(in);

            Parcelable[] parcelables = in.readParcelableArray(Bundle.class.getClassLoader());
            childStates = new Bundle[parcelables.length];
            for (int i = 0; i < childStates.length; i++) {
                childStates[i] = (Bundle) parcelables[i];
            }

            selectedIndex = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeParcelableArray(childStates, flags);
            out.writeInt(selectedIndex);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
