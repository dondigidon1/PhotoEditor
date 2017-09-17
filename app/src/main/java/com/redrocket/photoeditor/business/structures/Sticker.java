package com.redrocket.photoeditor.business.structures;

import android.support.v4.util.Pair;

/**
 * Описание стикера.
 */
public class Sticker {
    /** Идентификатор стикера */
    public final int id;

    /**
     * Положение центра стикера в долях относительно длин сторон изображения.
     * center.first - положение по оси X (0f..1f)
     * center.second - положение по оси Y (0f..1f)
     * Пример:
     * Ширина изображения 100 пикселей, координата X для центра 60 пикслей,
     * значит center.first = 0.6f
     * Высота изображения 200 пикселей, координата Y для центра 40 пикселей,
     * значит center.second = 0.2f
     */
    public final Pair<Float, Float> center;

    /**
     * Ширина и высота стикера в долях относительно длин сторон изображения.
     * dims.first - ширина (0f..1f)
     * dims.second - выстота (0f..1f)
     * {@link Sticker#center}
     */
    public final Pair<Float, Float> dims;

    /** Угол поворота относительно центра в градусах*/
    public final float rotation;

    /**
     * @param id Идентификатор стикера.
     * @param center Положение центра.
     * @param dims Ширина/высота.
     * @param rotation Угол поворота.
     */
    public Sticker(int id,
                   Pair<Float, Float> center,
                   Pair<Float, Float> dims,
                   float rotation) {
        this.id = id;
        this.center = center;
        this.dims = dims;
        this.rotation = rotation;
    }
}
