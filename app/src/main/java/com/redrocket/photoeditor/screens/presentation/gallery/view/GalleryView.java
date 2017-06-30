package com.redrocket.photoeditor.screens.presentation.gallery.view;

import com.redrocket.photoeditor.screens.presentation.MvpView;
import com.redrocket.photoeditor.screens.presentation.gallery.structures.PreviewDescriptor;

import java.util.List;

/**
 * Интерфейс вью для экрана галереи.
 */
public interface GalleryView extends MvpView {

    /**
     * Установать изображения.
     *
     * @param images Упорядоченный список с описателями изображений.
     */
    void setImages(List<PreviewDescriptor> images);

    /**
     * Запустить экран кропа для изображения.
     *
     * @param image Путь к изображению.
     */
    void openCrop(String image);
}
