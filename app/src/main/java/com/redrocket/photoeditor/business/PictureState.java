package com.redrocket.photoeditor.business;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.util.CropArea;

import java.util.List;

/**
 * Класс представляет собой описание изображения.
 */
public class PictureState {
    private static final String TAG = "PictureState";

    private String mSrc;
    private CropArea mCrop = new CropArea();
    private int mEffect = 0;
    private List<Sticker> mStickers;

    private String mSavedPath = null;

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
    PictureState() {}

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
    @NonNull
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
    @Nullable
    public CropArea getCrop() {
        return mCrop;
    }

    /**
     * Установить эффект.
     *
     * @param effectId Идентификатор эффекта.
     */
    public void setEffect(int effectId) {
        mEffect = effectId;
        if (mListener != null)
            mListener.onUpdate();
    }

    /**
     * Получить эффект.
     *
     * @return Возвращает идентификатор эффекта.
     */
    public int getEffect() {
        return mEffect;
    }

    /**
     * Установить стикеры.
     *
     * @param stickers Коллекция стикеров.
     */
    public void setStickers(@NonNull List<Sticker> stickers) {
        mStickers = stickers;
        if (mListener != null) {
            mListener.onUpdate();
        }
    }

    /**
     * Получить стикеры.
     *
     * @return Возвращает коллекцию стикеров.
     */
    @Nullable
    public List<Sticker> getStickers(){
        return mStickers;
    }

    /**
     * Установить путь к файлу где сохранено изображение.
     */
    public void setSavedPath(@NonNull String path) {
        mSavedPath = path;
        if (mListener != null) {
            mListener.onUpdate();
        }
    }

    /**
     * Получить путь к файлу с сохраненным изображением.
     *
     * @return Возвращает путь к файлу.
     */
    @Nullable
    public String getSavedPath() {
        return mSavedPath;
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
