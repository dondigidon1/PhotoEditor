package com.redrocket.photoeditor.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.media.ExifInterface;
import android.support.v4.util.Pair;

import java.io.IOException;

/**
 * Набор вспомогательных методов для работы с {@link android.graphics.Bitmap}
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    private BitmapUtils() {}

    public static Bitmap getRotated(Bitmap src, float rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            Bitmap rotatedBitmap = Bitmap.createBitmap(src,
                    0, 0, src.getWidth(), src.getHeight(),
                    matrix,
                    true);

            src.recycle();
            return rotatedBitmap;
        } else {
            return src;
        }
    }

    /**
     * Получить длины сторон изображения.
     *
     * @param path Путь к файлу с изображением.
     * @return Возвращает Pair.first - ширина, Pair.second - высота.
     * @throws IOException
     */
    public static Pair<Integer, Integer> getDims(String path) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        if (options.outWidth == -1 || options.outHeight == -1) {
            throw new IOException();
        }

        return new Pair<>(options.outWidth, options.outHeight);
    }

    public static Pair<Integer, Integer> getDims(Resources resources, @DrawableRes int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, resId, options);

        return new Pair<>(options.outWidth, options.outHeight);
    }

    /**
     * Повернуть прямоугольник на заданный угол относительно заданной точки.
     *
     * @param rect    Исходный прямоугольник.
     * @param degrees Нужный угол поворота.
     * @param pivotX  Координата X центра вращения.
     * @param pivotY  Координата Y центра вращения.
     * @return Возвращает новый повернутый прямоугольник.
     */
    public static RectF rotateRect(RectF rect, float degrees, float pivotX, float pivotY) {
        RectF result = new RectF(rect);

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees, pivotX, pivotY);
        matrix.mapRect(result);

        return result;
    }

    /**
     * Рассчитать подходящий {@link BitmapFactory.Options#inSampleSize}.
     *
     * @param srcWidth  Исходная ширина изображения.
     * @param srcHeight Исходная высота изображения.
     * @param reqWidth  Требуемая ширина.
     * @param reqHeight Требуемая высота.
     * @return Возвращает inSampleSize, такой что после его применения полученное изображение
     * будет иметь размеры не меньше reqWidth и reqHeight, но максимально близкие к ним.
     */
    public static int calcInSampleSize(int srcWidth, int srcHeight, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (srcHeight > reqHeight || srcWidth > reqWidth) {
            int halfHeight = srcHeight / 2;
            int halfWidth = srcWidth / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Получить угол поворота изображения исходя из {@link ExifInterface}.
     *
     * @param path Путь к файлу с изображением.
     * @return Возвращает угол в градусах.
     * @throws IOException
     */
    public static int getRotation(String path) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (exifRotation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }
}
