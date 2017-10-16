package com.redrocket.photoeditor.presentation.save.remover;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Реализация доступа к файловой системе.
 */
public class FileManagerImpl implements FileManager {
    @Override
    public void remove(@NonNull String path) {
        File file = new File(path);
        file.delete();
    }

    @Override
    public boolean contains(@NonNull String path) {
        File file = new File(path);
        return file.exists();
    }
}
