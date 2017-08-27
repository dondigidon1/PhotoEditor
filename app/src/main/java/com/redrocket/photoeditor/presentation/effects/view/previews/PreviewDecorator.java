package com.redrocket.photoeditor.presentation.effects.view.previews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Декоратор для отступов между ячейками.
 */
public class PreviewDecorator extends RecyclerView.ItemDecoration {
    private static final String TAG = "PreviewDecorator";
    private final int mSpacing;

    /**
     * Конструктор.
     *
     * @param spacing Отступ в пикселях.
     */
    public PreviewDecorator(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = mSpacing;

        if (parent.getChildAdapterPosition(view) == 0)
            outRect.left = mSpacing;
    }
}
