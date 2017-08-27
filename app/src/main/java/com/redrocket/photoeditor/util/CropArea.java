package com.redrocket.photoeditor.util;

import java.io.Serializable;

/**
 * Описывает область кропа.
 */
public class CropArea implements Serializable {
    private static final float MIN_FRACTION = 0f;
    private static final float MAX_FRACTION = 1f;

    public final float left;
    public final float top;
    public final float right;
    public final float bottom;

    /**
     * Конструктор с указанием границ кропа.
     * Пример:
     * CropArea(0.5f, 0.5f, 1f, 1f) создаст область
     * выделяющую правую нижнюю часть прямоугольника.
     *
     * @param left   Начало кропа слева в долях.
     * @param top    Начало кропа сверху в долях.
     * @param right  Конец кропа справа в долях.
     * @param bottom Конец кропа снизу в долях.
     */
    public CropArea(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Конструктор выделяющий всю область изображения.
     */
    public CropArea() {
        this.left = MIN_FRACTION;
        this.top = MIN_FRACTION;
        this.right = MAX_FRACTION;
        this.bottom = MAX_FRACTION;
    }

    /**
     * Получить ширину области в долях от ширины изображения.
     *
     * @return Возвращает долю от 0 до 1.
     */
    public float width() {
        return right - left;
    }

    /**
     * Получить высоту области в долях от высоты изображения.
     *
     * @return Возвращает долю от 0 до 1.
     */
    public float height() {
        return bottom - top;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(getClass().getSimpleName()).append(" ")
                .append("left ").append(left).append(" ")
                .append("top ").append(top).append(" ")
                .append("right ").append(right).append(" ")
                .append("bottom ").append(bottom);

        return builder.toString();
    }
}
