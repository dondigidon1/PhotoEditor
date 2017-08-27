package com.redrocket.photoeditor.presentation.common.effect;

import android.support.annotation.NonNull;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Данные эффекта.
 */
public class Effect {
    public final GPUImageFilter filter;
    public final String name;

    /**
     * Конструктор.
     * @param filter Фильтр.
     * @param name Название эффекта.
     */
    Effect(@NonNull GPUImageFilter filter, @NonNull String name) {
        this.filter = filter;
        this.name = name;
    }
}
