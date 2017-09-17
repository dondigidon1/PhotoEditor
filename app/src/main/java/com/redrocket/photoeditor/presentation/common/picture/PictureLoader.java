package com.redrocket.photoeditor.presentation.common.picture;

import android.graphics.Bitmap;
import android.os.Handler;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Асинхронно создает изображение.
 */
public class PictureLoader {
    private final PictureBuilder mBuilder;

    /**
     * @param builder Описание требуемого изображения
     */
    public PictureLoader(PictureBuilder builder) {
        mBuilder = builder;
    }

    /**
     * Начать создание изображения.
     *
     * @param listener Обработчик результата.
     */
    public void load(final ResultHandler listener) {
        new LoadTask(listener).start();
    }

    /**
     * Интерфейс обработчика результата создания изображения.
     */
    public interface ResultHandler {
        /**
         * Изображение готово.
         *
         * @param picture Изображение.
         */
        void onPictureReady(Bitmap picture);

        /**
         * Произошла ошибка.
         */
        void onError();
    }

    private class LoadTask extends Thread {
        private final Handler mHandler = new Handler();
        private final WeakReference<ResultHandler> mListenerRef;

        LoadTask(ResultHandler listener) {
            mListenerRef = new WeakReference<>(listener);
        }

        @Override
        public void run() {
            try {
                Bitmap picture = mBuilder.getBitmap();
                handlePicture(picture);
            } catch (IOException e) {
                handlePicture(null);
            }
        }

        private void handlePicture(final Bitmap picture) {
            final ResultHandler listener = mListenerRef.get();
            if (listener == null) {
                return;
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (picture != null) {
                        listener.onPictureReady(picture);
                    } else {
                        listener.onError();
                    }
                }
            });
        }
    }
}
