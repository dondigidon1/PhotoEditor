package com.redrocket.photoeditor.presentation.crop.view;

import com.redrocket.photoeditor.presentation.MvpView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Интерфейс вью для экрана кропа.
 */
public interface CropView extends MvpView {

    /**
     * Запустить экран эффектов.
     */
    void openEffectScreen();

    /**
     * Установаить изображение для кропа.
     *
     * @param path Путь к файлу с изображением.
     * @param crop Область кропа.
     */
    void setImage(String path, CropArea crop);
}
