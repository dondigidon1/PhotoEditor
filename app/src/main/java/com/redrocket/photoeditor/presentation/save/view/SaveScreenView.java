package com.redrocket.photoeditor.presentation.save.view;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.MvpView;
import com.redrocket.photoeditor.util.CropArea;

import java.util.List;

/**
 * Интерфейс вью для экрана сохранения.
 */
public interface SaveScreenView extends MvpView {
    /**
     * Установить превью изображения.
     *
     * @param path     Путь к файлу с оригиналом.
     * @param crop     Применяемый кроп.
     * @param effect   Применяемый эффект.
     * @param stickers Применяемые стикеры.
     */
    void setPreview(
            @NonNull String path,
            @NonNull CropArea crop,
            @IntRange(from = 0) int effect,
            @NonNull List<Sticker> stickers);

    /**
     * Переключиться в режим выполения сохранения.
     */
    void switchToLoading();

    /**
     * Переключиться в режим отображения результата.
     */
    void switchToResult();

    /**
     * Вернуться на экран галереи.
     */
    void resetToGalleryScreen();

    /**
     * Закрыть экран.
     */
    void closeScreen();

    /**
     * Показать сообщение с файловой ошибкой.
     */
    void showFileErrorMsg();

    /**
     * Показать диалог отмены сохранения.
     */
    void showCloseSavingDialog();

    /**
     * Поделиться изображением.
     * @param path Путь к итоговому изображению.
     */
    void shareImage(@NonNull String path);
}
