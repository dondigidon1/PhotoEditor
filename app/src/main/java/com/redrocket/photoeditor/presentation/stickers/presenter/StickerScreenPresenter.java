package com.redrocket.photoeditor.presentation.stickers.presenter;

import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.MvpPresenter;
import com.redrocket.photoeditor.presentation.stickers.view.StickerScreenView;

/**
 * Интерфейс презентера для экрана стикеров.
 */
public interface StickerScreenPresenter extends MvpPresenter<StickerScreenView> {
    /**
     * Произошел клик по стикеру.
     *
     * @param id Идентификатор стикера.
     */
    void onPickSticker(int id);

    /**
     * Пользователь подтвердил выбор стикеров.
     *
     * @param stickers Коллекция выбранных стикеров.
     */
    void onConfirmStickers(Sticker[] stickers);

    /**
     * Произошла ошибка при обращении к файлу в постоянной памяти.
     */
    void onFileError();

    /**
     * Диалог с файловой ошибкой закрылся.
     */
    void onCloseFileErrorMsg();
}
