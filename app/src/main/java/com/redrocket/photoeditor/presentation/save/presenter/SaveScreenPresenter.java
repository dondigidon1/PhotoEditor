package com.redrocket.photoeditor.presentation.save.presenter;

import com.redrocket.photoeditor.presentation.MvpPresenter;
import com.redrocket.photoeditor.presentation.save.view.SaveScreenView;

/**
 * Интерфейс презентера для экрана сохранения.
 */
public interface SaveScreenPresenter extends MvpPresenter<SaveScreenView> {
    /**
     * Произошла ошибка при обращении к файлу в постоянной памяти.
     */
    void onFileError();

    /**
     * Диалог с файловой ошибкой закрылся.
     */
    void onCloseFileErrorMsg();

    /**
     * Пользоватьль нажал кнопку назад.
     */
    void onBackClick();

    /**
     * Пользователь нажал на кнопку остановки сохранения.
     */
    void onStopSavingClick();

    /**
     * Пользователь нажал на кнопку шаринг.
     */
    void onShareClick();

    /**
     * Пользователь нажал на кнопку завершения работы.
     */
    void onFinishClick();
}
