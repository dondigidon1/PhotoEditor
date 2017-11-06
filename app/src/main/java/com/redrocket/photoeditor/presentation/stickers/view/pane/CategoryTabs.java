package com.redrocket.photoeditor.presentation.stickers.view.pane;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.redrocket.photoeditor.R;

import java.util.List;

/**
 * Вью отображающая вкладки категорий
 */
public class CategoryTabs extends LinearLayout {

    private static final String TAG = "CategoryTabs";

    private static final int NO_SELECT_INDEX = -1;

    private int mSelectedIndex = NO_SELECT_INDEX;

    private OnTabClickListener mListener;

    public CategoryTabs(@NonNull Context context) {
        this(context, null);
    }

    public CategoryTabs(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryTabs(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(OnTabClickListener listener) {
        mListener = listener;
    }

    /**
     * Установить категории.
     *
     * @param icons Набор иконок категорий.
     */
    public void setTabs(List<Integer> icons) {
        removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < icons.size(); i++) {
            int icon = icons.get(i);

            View view = inflater.inflate(R.layout.view_sticker_category_tab, this, false);
            ((ImageView) view.findViewById(R.id.image_icon)).setImageResource(icon);

            addView(view);

            final int tabIndex = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleTabClick(tabIndex);
                }
            });
        }
    }

    /**
     * Установить категоию выбранной.
     *
     * @param index Индекс категории.
     */
    public void setSelected(int index) {
        if (index == mSelectedIndex) {
            return;
        }

        int animDuration = getResources().getInteger(R.integer.sticker_category_tabs_anim_duration);

        View newSelected = getChildAt(index);
        if (newSelected != null) {
            newSelected.findViewById(R.id.highlight).animate().alpha(1f).setDuration(animDuration);
        }

        View oldSelected = getChildAt(mSelectedIndex);
        if (oldSelected != null) {
            oldSelected.findViewById(R.id.highlight).animate().alpha(0f).setDuration(animDuration);
        }

        mSelectedIndex = index;
    }

    private void handleTabClick(int index) {
        if (mListener != null) {
            mListener.onTabClick(index);
        }
    }

    /**
     * Слушатель панели вкладок.
     */
    public interface OnTabClickListener {
        /**
         * Произошло нажатие на вкладку.
         *
         * @param index Индекс вкладки.
         */
        void onTabClick(int index);
    }
}
