package com.redrocket.photoeditor.presentation.effects.presenter;

import com.redrocket.photoeditor.presentation.MvpPresenter;
import com.redrocket.photoeditor.presentation.effects.view.EffectScreenView;

/**
 * Интерфейс презентера для экрана эффектов.
 */
public interface EffectPresenter extends MvpPresenter<EffectScreenView> {
    /**
     * Пользователь выбрал эффект.
     *
     * @param index Индекс эффекта.
     */
    void onClickEffect(int index);

    /**
     * Пользователь подтвердил выбор эффекта.
     *
     * @param index Индекс подтвержденного эффекта.
     */
    void onConfirmEffect(int index);

    /**
     * Произошла ошибка при обращении к файлу в постоянной памяти.
     */
    void onFileError();

    /**
     * Диалог с файловой ошибкой закрылся.
     */
    void onCloseFileErrorMsg();
}
