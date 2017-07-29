package com.redrocket.photoeditor.business;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redrocket.photoeditor.util.CropArea;

/**
 * Класс представляет собой описание изображения.
 */
public class PictureState {
    private static final String TAG = "PictureState";

    private String mSrc;
    private CropArea mCrop = new CropArea();

    private transient OnUpdateListener mListener;

    /**
     * Обычный конструктор.
     *
     * @param path Путь к файлу с исходным изображением.
     */
    PictureState(@NonNull String path) {
        mSrc = path;
    }

    /**
     * Конструктор для десереализации.
     */
    PictureState() {
    }

    /**
     * Установить слушателя изменений состояния.
     *
     * @param listener Слушатель.
     */
    void setListener(@Nullable OnUpdateListener listener) {
        mListener = listener;
    }

    /**
     * Получить исходное изображение.
     *
     * @return Возвращает путь к файлу с изображением.
     */
    public String getPath() {
        return mSrc;
    }

    /**
     * Установить область обрезки.
     *
     * @param crop Прямоугольник в виде {@link CropArea}.
     */
    public void setCrop(@NonNull CropArea crop) {
        mCrop = crop;

        if (mListener != null)
            mListener.onUpdate();
    }

    /**
     * Получить область обрезки.
     *
     * @return Возвращает прямоугольник в виде {@link CropArea}.
     */
    public CropArea getCrop() {
        return mCrop;
    }

    /**
     * Слушатель изменений состояния изображения.
     */
    interface OnUpdateListener {
        /**
         * Состояние изображения изменилось.
         */
        void onUpdate();
    }
}
