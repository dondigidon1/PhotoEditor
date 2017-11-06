package com.redrocket.photoeditor.presentation.stickers.view;

import com.redrocket.photoeditor.presentation.MvpView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Интерфейс вью для экрана стикеров.
 */
public interface StickerScreenView extends MvpView {
    /**
     * Установить изображение.
     *
     * @param path   Путь к файлу с изображением.
     * @param crop   Область обрезки изображения.
     * @param effect Эффект примененный к изображению.
     */
    void setImage(String path, CropArea crop, int effect);

    /**
     * Добавить новый стикер.
     *
     * @param id Идентификатор стикера.
     */
    void addNewSticker(int id);

    /**
     * Вернуться на экран галереи.
     */
    void resetToGalleryScreen();

    /**
     * Показать сообщение с файловой ошибкой.
     */
    void showFileErrorMsg();

    /**
     * Открыть экран сохранения.
     */
    void openSaveScreen();

    /**
     * Открыть ссылку.
     * @param link Строка с url.
     */
    void openLink(String link);
}
