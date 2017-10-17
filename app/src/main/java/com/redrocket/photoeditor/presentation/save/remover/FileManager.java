package com.redrocket.photoeditor.presentation.save.remover;

import android.support.annotation.NonNull;

/**
 * Интерфейс доступа к файловой системе.
 */
public interface FileManager {

    /**
     * Удалить файл.
     * @param path Путь к файлу.
     */
    void remove(@NonNull String path);

    /**
     * Проверить существует ли файл.
     * @param path Путь к проверяемому файлу.
     * @return true - существует false - не существует.
     */
    boolean contains(@NonNull String path);
}
