package com.redrocket.photoeditor.business;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Проект.
 * Управляет сохранением данных.
 */
public class Project implements PictureState.OnUpdateListener {
    private static final String TAG = "Project";

    private final Repository mRepository;
    private PictureState mPicture;

    /**
     * Простой конструктор.
     *
     * @param repository Хранилище данных.
     */
    public Project(@NonNull Repository repository) {
        mRepository = repository;
        mPicture = mRepository.loadPicture();
    }

    @Override
    public void onUpdate() {
        savePicture();
    }

    /**
     * Начать создание нового изображения.
     *
     * @param path Путь к файлу с исходным изображением.
     */
    public void startPicture(@NonNull String path) {
        if (mPicture != null) {
            mPicture.setListener(null);
        }

        mPicture = new PictureState(path);
        mPicture.setListener(this);
        savePicture();
    }

    /**
     * Получить текущее изображение.
     *
     * @return Возвращает текущее изображение.
     */
    @Nullable
    public PictureState getPicture() {
        return mPicture;
    }

    private void savePicture() {
        mRepository.savePicture(mPicture);
    }
}