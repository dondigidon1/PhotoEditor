package com.redrocket.photoeditor.presentation.stickers.view.pane;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.presentation.common.sticker.StickerCategory;
import com.redrocket.photoeditor.presentation.common.sticker.StickerInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Адаптер для превьюше стикеров.
 */
public class StickersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "StickersAdapter";

    public static final int STICKER_VIEW_TYPE = 0;
    public static final int CREDIT_VIEW_TYPE = 1;

    private final List<StickerCategory> mCategories;

    // хранит позиции адаптера с которых начинаются категории.
    // каждый индекс списка соответствует индексу категории.
    private final List<Integer> mCategoryStartPositions = new ArrayList<>();
    private final int mItemCount;

    private final OnStickerClickListener mListener;

    /**
     * @param listener Сшулатель нажатия на стикер.
     */
    public StickersAdapter(List<StickerCategory> categories, OnStickerClickListener listener) {
        mCategories = categories;

        int tmp = 0;
        for (StickerCategory category : mCategories) {
            mCategoryStartPositions.add(tmp);
            tmp += category.getStickers().size() + 1; // + 1 для ссылки на автора стикеров
        }
        mItemCount = tmp;

        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);

        if (item instanceof StickerInfo) {
            return STICKER_VIEW_TYPE;
        } else if (item instanceof String) {
            return CREDIT_VIEW_TYPE;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == STICKER_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_sticker_preview, parent, false);
            return new StickerHolder(view);
        } else if (viewType == CREDIT_VIEW_TYPE) {
            View view = inflater.inflate(R.layout.item_credit, parent, false);
            return new CreditHolder(view);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        Object item = getItem(pos);

        if (item instanceof StickerInfo) {
            StickerInfo sticker = (StickerInfo) item;
            StickerHolder stickerHolder = (StickerHolder) holder;
            stickerHolder.preview.setImageResource(sticker.miniImage);
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    /**
     * Получить позицию адаптера для первого элемента из категоии.
     *
     * @param index Индекс категоии.
     * @return Возвращает позицию адаптера.
     */
    public int getCategoryFirstItemPos(int index) {
        return mCategoryStartPositions.get(index);
    }

    /**
     * Получить индекс категории.
     *
     * @param pos Позиция адаптера.
     * @return Возвращает индекс категории внутри
     * которой содержится переданная позиция адаптера.
     */
    public int getCategoryIndexByItemPos(int pos) {
        int categoryIndex = Collections.binarySearch(mCategoryStartPositions, pos);
        if (categoryIndex < 0) {
            categoryIndex = -(categoryIndex + 1) - 1; //смотри доки Collections.binarySearch
        }

        return categoryIndex;
    }

    private void handleItemClick(int pos) {
        Object item = getItem(pos);
        if (item instanceof StickerInfo) {
            mListener.onStickerClick((StickerInfo) item);
        } else if (item instanceof String) {
            mListener.onCreditClick((String) item);
        } else {
            throw new IllegalStateException();
        }
    }

    // Возвращает стикер в виде StickerInfo либо ссылку в виде String.
    private Object getItem(int pos) {
        int categoryIndex = getCategoryIndexByItemPos(pos);

        StickerCategory category = mCategories.get(categoryIndex);
        int indexInside = pos - mCategoryStartPositions.get(categoryIndex);

        if (indexInside == category.getStickers().size()) {
            return category.getLink();
        } else {
            return category.getStickers().get(indexInside);
        }
    }

    /**
     * Слушатель кликов на стикеры.
     */
    public interface OnStickerClickListener {
        /**
         * Произошло нажатие на стикер.
         * @param sticker Стикер на который нажали.
         */
        void onStickerClick(StickerInfo sticker);

        /**
         * Произошло нажатие на ссылку автора.
         *
         * @param link Строка с url на автора.ц
         */
        void onCreditClick(String link);

    }

    class StickerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView preview;

        public StickerHolder(View itemView) {
            super(itemView);
            preview = (ImageView) itemView.findViewById(R.id.image_preview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handleItemClick(getAdapterPosition());
        }
    }

    class CreditHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CreditHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.text_credit).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handleItemClick(getAdapterPosition());
        }
    }
}
