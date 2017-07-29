package com.redrocket.photoeditor.business;

import android.support.annotation.Nullable;

/**
 * Интерфейс для хранения изображения.
 */
public interface Repository {
    /**
     * Сохранить в постоянную память изображение.
     *
     * @param picture Изображение.
     */
    void savePicture(@Nullable PictureState picture);

    /**
     * Загрузить изображение из постоянной памяти.
     *
     * @return Возвращает изображение, либо null.
     */
    @Nullable
    PictureState loadPicture();
}
