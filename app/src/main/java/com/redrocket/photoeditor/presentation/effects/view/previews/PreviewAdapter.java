package com.redrocket.photoeditor.presentation.effects.view.previews;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.presentation.common.effect.Effect;
import com.redrocket.photoeditor.util.CropArea;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Адаптер для набора эффектов.
 */
public class PreviewAdapter
        extends RecyclerView.Adapter<PreviewAdapter.PreviewHolder>
        implements PreviewProvider.OnPreviewReadyListener {
    private static final String TAG = "PreviewAdapter";

    private static final int NO_SELECT = -1;

    private final Effect[] mEffects;
    private final PreviewProvider mPreviews;

    private int mSelected = NO_SELECT;

    private final OnEffectSelectListener mListener;

    /**
     * Конструктор.
     *
     * @param context  Контекст.
     * @param src      Путь к файлу с исходным изображением.
     * @param crop     Область кропа.
     * @param side     Максимальный размер стороны итоговой превьюшки.
     * @param effects  Эффекты.
     * @param listener Слушатель выбора.
     */
    public PreviewAdapter(Context context,
                          String src, CropArea crop, int side,
                          Effect[] effects,
                          OnEffectSelectListener listener) {
        mEffects = effects;
        GPUImageFilter[] filters = new GPUImageFilter[effects.length];
        for (int i = 0; i < filters.length; i++) {
            filters[i] = effects[i].filter;
        }
        mPreviews = new PreviewProvider(context, src, crop, side, filters, this);

        mListener = listener;
    }

    @Override
    public PreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View preview = inflater.inflate(R.layout.item_effect_preview, null);

        return new PreviewHolder(preview);
    }

    @Override
    public void onBindViewHolder(PreviewHolder holder, int position) {
        holder.name.setText(mEffects[position].name);

        Bitmap preview = mPreviews.getPreview(position);
        holder.preview.setImageBitmap(preview);
        if (preview != null) {
            holder.highlight.setVisibility(position == mSelected ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mEffects.length;
    }

    @Override
    public void onPreviewReady(int index) {
        notifyItemChanged(index);
    }

    @Override
    public void onFileError() {
        mListener.onFileError();
    }

    /**
     * Выбрать эффект
     *
     * @param index Индекс эффекта.
     */
    public void selectEffect(int index) {
        setSelected(index);
    }

    /**
     * Получить выбранный эффект.
     *
     * @return Возвращает индекс выбранного эффекта.
     */
    public int getSelectedIndex() {
        return mSelected;
    }

    public void recycle() {
        mPreviews.recycle();
    }

    private void handlePreviewClick(int position) {
        setSelected(position);

        mListener.onEffectSelect(position);
    }

    private void setSelected(int index) {
        if (mSelected != NO_SELECT) {
            notifyItemChanged(mSelected);
        }

        mSelected = index;
        notifyItemChanged(mSelected);
    }

    /**
     * Слушатель выбора эффекта.
     */
    public interface OnEffectSelectListener {
        /**
         * Пользователь выбрал эффект.
         *
         * @param index Индекс выбранного эффекта.
         */
        void onEffectSelect(int index);

        /**
         * Произошла ошибка при обращении к файлу в постоянной памяти.
         */
        void onFileError();
    }

    class PreviewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView preview;
        final TextView name;
        final View highlight;

        public PreviewHolder(View itemView) {
            super(itemView);
            preview = (ImageView) itemView.findViewById(R.id.image_preview);
            name = (TextView) itemView.findViewById(R.id.text_name);
            highlight = itemView.findViewById(R.id.highlight);
            itemView.findViewById(R.id.container_preview).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handlePreviewClick(getAdapterPosition());
        }
    }
}
