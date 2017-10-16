package com.redrocket.photoeditor.business.structures;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.Pair;

/**
 * Описание стикера.
 */
public class Sticker implements Parcelable {
    /**
     * Идентификатор стикера
     */
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

    /**
     * Угол поворота относительно центра в градусах
     */
    public final float rotation;

    /**
     * @param id       Идентификатор стикера.
     * @param center   Положение центра.
     * @param dims     Ширина/высота.
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(center.first);
        dest.writeFloat(center.second);
        dest.writeFloat(dims.first);
        dest.writeFloat(dims.second);
        dest.writeFloat(rotation);
    }

    public static final Parcelable.Creator<Sticker> CREATOR
            = new Parcelable.Creator<Sticker>() {
        public Sticker createFromParcel(Parcel in) {
            int id = in.readInt();
            float centerX = in.readFloat();
            float centerY = in.readFloat();
            float dimX = in.readFloat();
            float dimY = in.readFloat();
            float rotation = in.readFloat();

            return new Sticker(
                    id,
                    new Pair<>(centerX, centerY),
                    new Pair<>(dimX, dimY),
                    rotation);
        }

        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
