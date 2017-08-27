package com.redrocket.photoeditor.presentation.crop.view;

import com.redrocket.photoeditor.presentation.MvpView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Интерфейс вью для экрана кропа.
 */
public interface CropScreenView extends MvpView {

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

    /**
     * Отобразить контрол для возврата на предыдущий экран.
     */
    void showBackControl();

    /**
     * Показать сообщение о файловой ошибке.
     */
    void showFileErrorMsg();

    /**
     * Перейти на экран гелереи.
     */
    void resetToGalleryScreen();
}
