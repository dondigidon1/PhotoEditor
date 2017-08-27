package com.redrocket.photoeditor.presentation.effects.view.previews;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.util.CropArea;

import java.io.IOException;
import java.lang.ref.WeakReference;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Вспомогательный класс для получения превью эффектов.
 * Класс даёт достпуп к обработанным фильтрами изображениям.
 * Оповещает о готовности изображения.
 */
class PreviewProvider {
    private static final String TAG = "PreviewProvider";

    private final Bitmap[] mPreviews;
    private final PreviewTask mTask;

    private final OnPreviewReadyListener mListener;

    /**
     * Конструктор.
     *
     * @param context  Контекст.
     * @param src      Путь к файлу с изображением для которого нужно получить превью.
     * @param srcCrop  Область кропа исходного изображения.
     * @param side     Размер стороны для превью.
     * @param filters  Фильтры для которых нужно создать набор превью.
     * @param listener Слушатель котовности превью.
     */
    PreviewProvider(Context context,
                    String src, CropArea srcCrop,
                    int side, GPUImageFilter[] filters,
                    OnPreviewReadyListener listener) {

        mPreviews = new Bitmap[filters.length];
        mListener = listener;
        mTask = new PreviewTask(context, src, srcCrop, side, filters);
        mTask.start();
    }

    /**
     * Получить превью.
     *
     * @param index Индекс фильтра.
     * @return Возвращает обработанное фильтром изображение.
     */
    Bitmap getPreview(int index) {
        return mPreviews[index];
    }

    /**
     * Освободить ресурсы.
     */
    public void recycle() {
        mTask.close();
    }

    private void handlePreview(int index, Bitmap preview) {
        mPreviews[index] = preview;
        mListener.onPreviewReady(index);
    }

    private void handleFileError() {
        mListener.onFileError();
    }

    /**
     * Слушатель готовности превью.
     */
    interface OnPreviewReadyListener {
        /**
         * Превью для фильтра готово.
         *
         * @param index Индекс фильтра.
         */
        void onPreviewReady(int index);

        /**
         * Произошла ошибка с файлом в постоянной памяти.
         */
        void onFileError();
    }

    class PreviewTask extends Thread {
        private static final String TAG = "PreviewTask";

        private final Handler mHandler = new Handler();

        private final WeakReference<Context> mContextRef;
        private final String mSrc;
        private final CropArea mCrop;
        private final int mSide;
        private final GPUImageFilter[] mFilters;

        private volatile boolean mWork = true;

        PreviewTask(Context context,
                    String src, CropArea crop,
                    int side,
                    GPUImageFilter[] filters) {
            mContextRef = new WeakReference<>(context);
            mSrc = src;
            mCrop = crop;
            mSide = side;
            mFilters = filters;
        }

        @Override
        public void run() {
            Context context = mContextRef.get();
            if (context == null || !mWork)
                return;

            try {
                Bitmap base = PhotoEditorApplication.getPictureFactory().getBuilder()
                        .image(mSrc)
                        .crop(mCrop)
                        .minBound(mSide, mSide)
                        .getBitmap();

                GPUImage gpuImage = new GPUImage(context);
                gpuImage.setImage(base);

                for (int i = 0; i < mFilters.length && mWork; i++) {
                    GPUImageFilter filter = mFilters[i];
                    gpuImage.setFilter(filter);
                    final Bitmap preview = gpuImage.getBitmapWithFilterApplied();
                    final int index = i;

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            handlePreview(index, preview);
                        }
                    });
                }

            } catch (IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        handleFileError();
                    }
                });
            }
        }

        public void close() {
            mWork = false;
        }
    }
}
