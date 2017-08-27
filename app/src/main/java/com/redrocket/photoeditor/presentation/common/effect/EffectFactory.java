package com.redrocket.photoeditor.presentation.common.effect;

import android.support.annotation.NonNull;

/**
 * Интерфейс фабрики эффектов {@link Effect}.
 */
public interface EffectFactory {
    /**
     * Получить эффект.
     *
     * @param id Идентификатор эффекта.
     * @return Возвращает новый экземпляр эффекта.
     */
    @NonNull
    Effect getEffect(int id);
}
