package com.redrocket.photoeditor.presentation.common.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.Pair;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.presentation.common.effect.EffectFactory;
import com.redrocket.photoeditor.util.BitmapUtils;
import com.redrocket.photoeditor.util.CropArea;

import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Класс для создания изображения.
 * Можно применять разные параметры для модификации изображения.
 */
public class PictureBuilder {
    private static final String TAG = "PictureBuilder";

    private static final int NO_EFFECT = -1;

    private enum ScaleMode {
        SCALE_INSIDE, // остаться внутри границ
        SCALE_FIll    // выйти за границы
    }

    private final Context mContext;

    private String mPath;
    private CropArea mCrop = new CropArea();
    private int mEffect = NO_EFFECT;

    private ScaleMode mScaleMode;
    private int mWidthBound;
    private int mHeightBound;

    public PictureBuilder(Context context) {
        mContext = context;
    }

    public PictureBuilder image(String path) {
        mPath = path;
        return this;
    }

    public PictureBuilder crop(CropArea crop) {
        mCrop = crop;
        return this;
    }

    public PictureBuilder effect(int effect) {
        mEffect = effect;
        return this;
    }

    public PictureBuilder minBound(int width, int height) {
        mScaleMode = ScaleMode.SCALE_FIll;
        mWidthBound = width;
        mHeightBound = height;
        return this;
    }

    public PictureBuilder maxBound(int width, int height) {
        mScaleMode = ScaleMode.SCALE_INSIDE;
        mWidthBound = width;
        mHeightBound = height;
        return this;
    }

    public Bitmap getBitmap() throws IOException {
        RectF crop = new RectF(mCrop.left, mCrop.top, mCrop.right, mCrop.bottom);
        int rotation = BitmapUtils.getRotation(mPath);
        RectF rotatedCrop = BitmapUtils.rotateRect(crop, -rotation, 0.5f, 0.5f);

        Pair<Integer, Integer> srcDims = BitmapUtils.getDims(mPath);
        int srcWidth = srcDims.first;
        int srcHeight = srcDims.second;

        int regionLeft = (int) (rotatedCrop.left * srcWidth);
        int regionTop = (int) (rotatedCrop.top * srcHeight);
        int regionRight = (int) (rotatedCrop.right * srcWidth);
        int regionBottom = (int) (rotatedCrop.bottom * srcHeight);
        Rect regionRect = new Rect(regionLeft, regionTop, regionRight, regionBottom);

        int minWidth = rotation % 180 == 0 ? mWidthBound : mHeightBound;
        int minHeight = rotation % 180 == 0 ? mHeightBound : mWidthBound;

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (mScaleMode != null) {
            options.inSampleSize = BitmapUtils.calcInSampleSize(
                    regionRect.width(), regionRect.height(),
                    minWidth, minHeight);
        }

        BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(mPath, true);
        Bitmap bitmap = decoder.decodeRegion(regionRect, options);

        if (mScaleMode != null) {
            bitmap = scale(bitmap, mScaleMode, minWidth, minHeight, true);
        }

        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,
                    0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    matrix,
                    true);
            bitmap.recycle();
            bitmap = rotatedBitmap;
        }

        if (mEffect != NO_EFFECT) {
            GPUImage gpuImage = new GPUImage(mContext);
            EffectFactory fabric = PhotoEditorApplication.getEffectFactory();
            GPUImageFilter filter = fabric.getEffect(mEffect).filter;
            gpuImage.setFilter(filter);
            Bitmap effectedBitmap = gpuImage.getBitmapWithFilterApplied(bitmap);
            bitmap.recycle();
            bitmap = effectedBitmap;
        }

        return bitmap;
    }

    private static Bitmap scale(Bitmap src, ScaleMode mode,
                                int boundWidth, int boundHeight,
                                boolean recycleSrc) {
        Pair<Integer, Integer> resultDims = compDimsAfterScale(mode,
                src.getWidth(), src.getHeight(),
                boundWidth, boundHeight);

        Bitmap scaled = Bitmap.createScaledBitmap(src, resultDims.first, resultDims.second, true);

        if (recycleSrc && scaled != src)
            src.recycle();

        return scaled;
    }

    private static Pair<Integer, Integer> compDimsAfterScale(ScaleMode mode,
                                                             int srcWidth, int srcHeight,
                                                             int boundWidth, int boundHeight) {

        float srcRatio = (float) srcWidth / (float) srcHeight;
        float boundRatio = (float) boundWidth / (float) boundHeight;

        int resultWidth;
        int resultHeight;

        if (mode == ScaleMode.SCALE_INSIDE && srcRatio > boundRatio
                || mode == ScaleMode.SCALE_FIll && srcRatio < boundRatio) {
            resultWidth = boundWidth;
            resultHeight = (int) (resultWidth / srcRatio);
        } else {
            resultHeight = boundHeight;
            resultWidth = (int) (resultHeight * srcRatio);
        }

        return new Pair<>(resultWidth, resultHeight);
    }
}
