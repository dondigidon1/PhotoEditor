package com.redrocket.photoeditor.presentation.stickers.view.pane;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.redrocket.photoeditor.R;

import java.util.List;

/**
 * Адаптер для превьюше стикеров.
 */
public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.StickerHolder> {
    private static final String TAG = "StickersAdapter";

    private final List<Integer> mPreviews;
    private final OnStickerClickListener mListener;

    /**
     * @param previews Коллекция ресурсов миниатюр стикеров.
     * @param listener Сшулатель нажатия на стикер.
     */
    public StickersAdapter(List<Integer> previews, OnStickerClickListener listener) {
        mPreviews = previews;
        mListener = listener;
    }

    @Override
    public StickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sticker_preview, parent, false);
        return new StickerHolder(view);
    }

    @Override
    public void onBindViewHolder(StickerHolder holder, int pos) {
        holder.preview.setImageResource(mPreviews.get(pos));
    }

    @Override
    public int getItemCount() {
        return mPreviews.size();
    }

    private void handleItemClick(int pos) {
        mListener.onStickerClick(pos);
    }

    /**
     * Слушатель кликов на стикеры.
     */
    public interface OnStickerClickListener{
        /**
         * Произошел клик на стикер.
         * @param pos Индекс стикера.
         */
        void onStickerClick(int pos);
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
}
