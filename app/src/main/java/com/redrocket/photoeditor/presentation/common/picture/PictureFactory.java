package com.redrocket.photoeditor.presentation.common.picture;

import android.support.annotation.NonNull;

/**
 * Фабрика изготовителей изображения.
 */
public interface PictureFactory {
    /**
     * Получить новый экземпляр изготовителя изображения.
     *
     * @return Возвращает новый экземпляр изготовителя изображения.
     */
    @NonNull
    PictureBuilder getBuilder();
}
