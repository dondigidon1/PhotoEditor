package com.redrocket.photoeditor.screens.presentation;

import android.content.Context;

/**
 * Базовый интерфейс для всех вью из паттерна MVP.
 */
public interface MvpView {
    /**
     * Получить контекст.
     *
     * @return Возвращает контекст для вью.
     */
    Context getContext();
}
