package com.redrocket.photoeditor.screens.presentation;

/**
 * Базовый интерфейс презентера из паттерна MVP.
 *
 * @param <V> Тип вью для презентера.
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Установить соответствующее вью.
     *
     * @param view Вью для презентера.
     */
    void bindView(V view);

    /**
     * Уничтожить презентер.
     */
    void destroy();
}