package com.redrocket.photoeditor.presentation.common.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Реализует отступы между превьюшками в галерее.
 */
public class ItemOffsetDecor extends RecyclerView.ItemDecoration {
    private final int mSpanCount;
    private final int mSpacing;

    /**
     * Конструктор.
     *
     * @param spanCount Количество столбцов.
     * @param spacing   Отступы между столбцами.
     */
    public ItemOffsetDecor(final int spanCount, final int spacing) {
        mSpanCount = spanCount;
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final int column = position % mSpanCount;

        outRect.left = mSpacing - column * mSpacing / mSpanCount;
        outRect.right = (column + 1) * mSpacing / mSpanCount;

        if (position < mSpanCount)
            outRect.top = mSpacing;

        outRect.bottom = mSpacing;
    }
}
