package com.redrocket.photoeditor.presentation.effects.view;

import com.redrocket.photoeditor.presentation.MvpView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Интерфейс вью для экрана эффектов.
 */
public interface EffectScreenView extends MvpView {
    /**
     * Установить изображение.
     * @param path Путь к исходному изображению.
     * @param crop Область кропа.
     */
    void setImage(String path, CropArea crop);

    /**
     * Применить эффект к изображению.
     * @param effectId Идентификатор эффекта.
     */
    void setEffect(int effectId);

    /**
     * Установить выбранный эффект.
     *
     * @param index Индекс выбранного эффекта.
     */
    void setSelectedEffectIndex(int index);

    /**
     * Установить превью эффектов.
     *
     * @param effectIds Набор идентификаторов эффектов.
     */
    void setPreviewEffects(int[] effectIds);

    /**
     * Открыть экран стикеров.
     */
    void openStickerScreen();

    /**
     * Вернуться на экран галереи.
     */
    void resetToGalleryScreen();

    /**
     * Показать сообщение с файловой ошибкой.
     */
    void showFileErrorMsg();
}
