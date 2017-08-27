package com.redrocket.photoeditor.presentation.crop.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redrocket.photoeditor.presentation.MvpPresenter;
import com.redrocket.photoeditor.presentation.crop.view.CropScreenView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Интерфейс презентера для экрана кропа.
 */
public interface CropPresenter extends MvpPresenter<CropScreenView> {
    /**
     * Завершен выбор области кропа
     *
     * @param cropArea Область кропа.
     */
    void onNextClick(@NonNull CropArea cropArea);

    /**
     * Установить начальные параметры.
     *
     * @param imagePath Путь к файлу с исходным изображением.
     * @param fromAnotherApp Экран запущен сторонним приложение.
     */
    void initialize(@Nullable String imagePath, boolean fromAnotherApp);

    /**
     * Произошла ошибка при обращении к файлу в постоянной памяти.
     */
    void onFileError();

    /**
     * Диалог с файловой ошибкой закрылся.
     */
    void onCloseFileErrorMsg();
}
