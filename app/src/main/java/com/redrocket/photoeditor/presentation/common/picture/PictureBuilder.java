package com.redrocket.photoeditor.presentation.common.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.common.effect.EffectFactory;
import com.redrocket.photoeditor.presentation.common.sticker.StickersTable;
import com.redrocket.photoeditor.util.BitmapUtils;
import com.redrocket.photoeditor.util.CropArea;

import java.io.IOException;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Класс для создания изображения.
 * Можно применять разные параметры для модификации изображения.
 */
public class PictureBuilder {
    private static final String TAG = "PictureBuilder";

    private static final int NO_EFFECT = -1;
    private static final int NO_BLUR_RADIUS = -1;

    private enum ScaleMode {
        SCALE_INSIDE, // остаться внутри границ
        SCALE_FIll    // выйти за границы
    }

    private final Context mContext;
    private final StickersTable mStickersTable;
    private final EffectFactory mEffectsFactory;

    private String mPath;
    private CropArea mCrop = new CropArea();
    private int mEffect = NO_EFFECT;
    private List<Sticker> mStickers = null;
    private int mBlurRadius = NO_BLUR_RADIUS;

    private ScaleMode mScaleMode;
    private int mWidthBound;
    private int mHeightBound;

    PictureBuilder(Context context,
                   StickersTable stickersTable,
                   EffectFactory effectsFactory) {
        mContext = context;
        mStickersTable = stickersTable;
        mEffectsFactory = effectsFactory;
    }

    public PictureBuilder image(@NonNull String path) {
        mPath = path;
        return this;
    }

    public PictureBuilder crop(@NonNull CropArea crop) {
        mCrop = crop;
        return this;
    }

    public PictureBuilder effect(@IntRange(from = 0) int effect) {
        mEffect = effect;
        return this;
    }

    public PictureBuilder stickers(@NonNull List<Sticker> stickers) {
        mStickers = stickers;
        return this;
    }

    public PictureBuilder blur(@IntRange(from = 0) int radius) {
        mBlurRadius = radius;
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
        Bitmap bitmap = getCropped(
                mPath,
                new RectF(mCrop.left, mCrop.top, mCrop.right, mCrop.bottom),
                mScaleMode,
                mWidthBound,
                mHeightBound);

        if (mEffect != NO_EFFECT) {
            GPUImageFilter filter = mEffectsFactory.getEffect(mEffect).filter;
            bitmap = getFiltered(mContext, bitmap, filter);
        }

        if (mStickers != null) {
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            for (Sticker sticker : mStickers) {
                int imageResId = mStickersTable.getStickerById(sticker.id).bigImage;
                drawSticker(canvas, sticker, imageResId, paint);
            }
        }

        if (mBlurRadius != NO_BLUR_RADIUS) {
            bitmap = BitmapUtils.getBlurred(mContext, bitmap, mBlurRadius);
        }

        return bitmap;
    }

    private static Bitmap getCropped(
            String path,
            RectF crop,
            ScaleMode scaleMode,
            int widthBound,
            int heightBound) throws IOException {

        int rotation = BitmapUtils.getRotation(path);
        RectF rotatedCrop = BitmapUtils.rotateRect(crop, -rotation, 0.5f, 0.5f);

        Pair<Integer, Integer> srcDims = BitmapUtils.getDims(path);
        int srcWidth = srcDims.first;
        int srcHeight = srcDims.second;

        int regionLeft = (int) (rotatedCrop.left * srcWidth);
        int regionTop = (int) (rotatedCrop.top * srcHeight);
        int regionRight = (int) (rotatedCrop.right * srcWidth);
        int regionBottom = (int) (rotatedCrop.bottom * srcHeight);
        Rect regionRect = new Rect(regionLeft, regionTop, regionRight, regionBottom);

        int minWidth = rotation % 180 == 0 ? widthBound : heightBound;
        int minHeight = rotation % 180 == 0 ? heightBound : widthBound;

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (scaleMode != null) {
            options.inSampleSize = BitmapUtils.calcInSampleSize(
                    regionRect.width(), regionRect.height(),
                    minWidth, minHeight);
            options.inMutable = true;
        }

        BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(path, false);
        Bitmap bitmap = decoder.decodeRegion(regionRect, options);

        if (scaleMode != null) {
            bitmap = scale(bitmap, scaleMode, minWidth, minHeight);
        }

        bitmap = BitmapUtils.getRotated(bitmap, rotation);

        return bitmap;
    }

    private static Bitmap getFiltered(Context context, Bitmap bitmap, GPUImageFilter filter) {
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setFilter(filter);
        Bitmap effectedBitmap = gpuImage.getBitmapWithFilterApplied(bitmap);
        bitmap.recycle();
        bitmap = effectedBitmap;

        return bitmap;
    }

    private void drawSticker(Canvas canvas, Sticker sticker, int imageResId, Paint paint) {
        Pair<Integer, Integer> srcDims = BitmapUtils.getDims(mContext.getResources(), imageResId);

        int widthBound = Math.round(canvas.getWidth() * sticker.dims.first);
        int heightBound = Math.round(canvas.getHeight() * sticker.dims.second);

        Pair<Integer, Integer> resultDims = compDimsAfterScale(
                ScaleMode.SCALE_INSIDE,
                srcDims.first,
                srcDims.second,
                widthBound,
                heightBound);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = BitmapUtils.calcInSampleSize(
                srcDims.first,
                srcDims.second,
                resultDims.first,
                resultDims.second);
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), imageResId, options);
        image = scale(image, ScaleMode.SCALE_INSIDE, resultDims.first, resultDims.second);

        float stickerCenterX = sticker.center.first * canvas.getWidth();
        float stickerCenterY = sticker.center.second * canvas.getHeight();
        float stickerWidth = sticker.dims.first * canvas.getWidth();
        float stickerHeight = sticker.dims.second * canvas.getHeight();

        float left = stickerCenterX - stickerWidth / 2f;
        float top = stickerCenterY - stickerHeight / 2f;

        Matrix matrix = new Matrix();
        matrix.postScale(
                stickerWidth / image.getWidth(),
                stickerHeight / image.getHeight());
        matrix.postRotate(sticker.rotation, stickerWidth / 2f, stickerHeight / 2f);
        matrix.postTranslate(left, top);

        canvas.drawBitmap(image, matrix, paint);
    }

    private static Bitmap scale(Bitmap src, ScaleMode mode,
                                int boundWidth, int boundHeight) {
        Pair<Integer, Integer> resultDims = compDimsAfterScale(mode,
                src.getWidth(), src.getHeight(),
                boundWidth, boundHeight);

        Bitmap scaled = Bitmap.createScaledBitmap(src, resultDims.first, resultDims.second, true);

        if (scaled != src) {
            src.recycle();
        }

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
            resultHeight = Math.round(resultWidth / srcRatio);
        } else {
            resultHeight = boundHeight;
            resultWidth = Math.round(resultHeight * srcRatio);
        }

        return new Pair<>(resultWidth, resultHeight);
    }
}
