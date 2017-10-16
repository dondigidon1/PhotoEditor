package com.redrocket.photoeditor.presentation.save.saver;

import android.support.annotation.NonNull;

import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.util.CropArea;

import java.util.List;

/**
 * Интерфейс исполнителя сохранения изображения в файловой системе.
 */
public interface PictureSaver {

    /**
     * Начать сохранение.
     *
     * @param path     Путь к оригинальному файлу с изображением.
     * @param crop     Применяемый кроп.
     * @param effect   Применяемый эффект.
     * @param stickers Применяемые стикеры.
     * @param listener Слушатель результата.
     */
    void save(@NonNull String path,
              @NonNull CropArea crop,
              int effect,
              @NonNull List<Sticker> stickers,
              @NonNull PictureSaverListener listener);

    /**
     * Определить идет ли сейчас сохранение файла.
     *
     * @return true - какой-то файл сохраняется false - не происходит сохранения.
     */
    boolean isSaving();

    /**
     * Остановить последнее начатое сохранение.
     * @throws IllegalStateException Срабатывает если в данный момент не идет сохранение.
     */
    void stopSaving() throws IllegalStateException;

    /**
     * Слушатель результата сохранения.
     */
    interface PictureSaverListener {
        /**
         * Произошла ошибка при сохранении.
         */
        void onError();

        /**
         * Изображение было успешно сохранено.
         * @param path Возвращает путь к результирующему файлу.
         */
        void onSuccess(@NonNull String path);
    }
}
