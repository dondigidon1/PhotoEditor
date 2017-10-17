package com.redrocket.photoeditor.presentation;

import android.support.annotation.NonNull;

/**
 * Базовый интерфейс презентера из паттерна MVP.
 *
 * @param <V> Тип вью для презентера.
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Установить соответствующее вью.
     *
     * @param view      Вью для презентера.
     * @param isRestore Экран после воосстановления
     *                  true - восстановление false - первое создание.
     */
    void bindView(@NonNull V view, boolean isRestore);

    /**
     * Уничтожить презентер.
     */
    void destroy();
}