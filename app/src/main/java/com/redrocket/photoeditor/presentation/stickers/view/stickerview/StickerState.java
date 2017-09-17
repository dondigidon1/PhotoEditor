package com.redrocket.photoeditor.presentation.stickers.view.stickerview;

import android.support.annotation.DrawableRes;
import android.support.v4.util.Pair;

/**
 * Описание стикера, которым оперирует {@link StickerBoard}.
 */
public class StickerState {
    /** Идентификатор стикера */
    public final int id;

    /** Ресурс с изображением стикера */
    public final int image;

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
     * {@link StickerState#center}
     */
    public final Pair<Float, Float> dims;

    /** Угол поворота стикера в градусах */
    public final float rotation;

    public StickerState(int id, @DrawableRes int image,
                        Pair<Float, Float> center,
                        Pair<Float, Float> dims,
                        float rotation) {
        this.id = id;
        this.image = image;
        this.center = center;
        this.dims = dims;
        this.rotation = rotation;
    }
}
