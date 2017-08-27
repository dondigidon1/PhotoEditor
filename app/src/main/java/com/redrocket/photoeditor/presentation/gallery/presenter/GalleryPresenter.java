package com.redrocket.photoeditor.presentation.gallery.presenter;


import android.support.annotation.NonNull;

import com.redrocket.photoeditor.presentation.MvpPresenter;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryScreenView;

/**
 * Интерфейс презентера для экрана галлереи.
 */
public interface GalleryPresenter extends MvpPresenter<GalleryScreenView> {
    /**
     * Пользователь выбрал изображение.
     *
     * @param image Путь к файлу с изображением.
     */
    void onSelectImage(@NonNull final String image);
}
