package com.redrocket.photoeditor.presentation.effects.view;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.util.CropArea;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Фоновая задача для получения отмасштабированного и обрезанного изображения.
 */
class CropTask extends AsyncTask<Void, Void, Bitmap> {
    private static final String TAG = "CropTask";

    private final String mImagePath;
    private final CropArea mCrop;
    private final int mScreenWidth;
    private final int mScreenHeight;
    private final WeakReference<OnBitmapReadyListener> mListenerRef;

    CropTask(String path, CropArea crop,
             int screenWidth, int screenHeight,
             OnBitmapReadyListener listener) {
        mImagePath = path;
        mCrop = crop;
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mListenerRef = new WeakReference<>(listener);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        try {
            return PhotoEditorApplication.getPictureFactory().getBuilder()
                    .image(mImagePath)
                    .crop(mCrop)
                    .minBound(mScreenWidth, mScreenHeight)
                    .getBitmap();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        OnBitmapReadyListener listener = mListenerRef.get();

        if (listener != null) {
            if (bitmap != null) {
                listener.onBitmapReady(bitmap);
            } else {
                listener.onError();
            }
        }
    }

    /**
     * Слушатель готовности изображения.
     */
    interface OnBitmapReadyListener {
        /**
         * Изображение готово.
         *
         * @param bitmap Изображение.
         */
        void onBitmapReady(Bitmap bitmap);

        /**
         * Неудалось загрузить изображение.
         */
        void onError();
    }
}
