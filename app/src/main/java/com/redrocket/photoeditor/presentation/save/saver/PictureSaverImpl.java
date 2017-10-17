package com.redrocket.photoeditor.presentation.save.saver;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.common.picture.PictureBuilder;
import com.redrocket.photoeditor.presentation.common.picture.PictureFactory;
import com.redrocket.photoeditor.util.CropArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Сохраняет изображение в файловую систему.
 */
public class PictureSaverImpl implements PictureSaver {
    private static final String TAG = "PictureSaverImpl";

    private static final String SHARED_PREF_NAME = "PictureSaverImpl_SHARED_PREF_NAME";
    private static final String PREF_LAST_TARGET_FILE_NUMBER = "PREF_LAST_TARGET_FILE_NUMBER";
    private static final int FAKE_SAVING_DELAY = 3000;

    private final Context mContext;
    private final File mAppPicturesDir;
    private final String mTargetFileNameBase;
    private final SharedPreferences mPrefs;
    private final PictureFactory mPictureFactory;
    private SaveTask mSaveTask;

    /**
     * @param context Контекст.
     * @param pictureFactory Фабрика для {@link PictureBuilder}.
     */
    public PictureSaverImpl(Context context, PictureFactory pictureFactory) {
        mContext = context;
        mPrefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mPictureFactory = pictureFactory;

        String appName = context.getResources().getString(R.string.app_name);
        mAppPicturesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appName);
        mAppPicturesDir.mkdirs();
        mTargetFileNameBase = appName;
    }

    @Override
    public void save(@NonNull String path,
                     @NonNull CropArea crop,
                     int effect,
                     @NonNull List<Sticker> stickers,
                     @NonNull PictureSaverListener listener) throws IllegalStateException {

        if (mSaveTask != null) {
            throw new IllegalStateException();
        }

        File target = getNextTargetFile();

        PictureBuilder builder = mPictureFactory.getBuilder()
                .image(path)
                .crop(crop)
                .effect(effect)
                .stickers(stickers);

        mSaveTask = new SaveTask(builder, target, listener);
        new Thread(mSaveTask).start();
    }

    @Override
    public void stopSaving() throws IllegalStateException {
        if (mSaveTask == null) {
            throw  new IllegalStateException();
        }
        mSaveTask.stopWork();
    }

    @Override
    public boolean isSaving() {
        return mSaveTask != null;
    }

    private File getNextTargetFile() {
        int lastNumber = mPrefs.getInt(PREF_LAST_TARGET_FILE_NUMBER, 0);
        int targetNumber = lastNumber + 1;

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(PREF_LAST_TARGET_FILE_NUMBER, targetNumber);
        editor.apply();

        File target = new File(mAppPicturesDir, mTargetFileNameBase + " " + targetNumber + ".jpg");
        return target;
    }

    private class SaveTask implements Runnable {

        private final Handler mHandler = new Handler();
        private final PictureBuilder mPictureBuilder;
        private final File mTargetFile;
        private final PictureSaverListener mListener;
        private volatile boolean mIsWork = true;

        SaveTask(@NonNull PictureBuilder builder,
                 @NonNull File target,
                 @NonNull PictureSaverListener listener) {
            mPictureBuilder = builder;
            mTargetFile = target;
            mListener = listener;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(FAKE_SAVING_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Bitmap bitmap = mPictureBuilder.getBitmap();
                if (!mIsWork) {
                    return;
                }
                saveBitmap(bitmap, mTargetFile);
                if (!mIsWork) {
                    mTargetFile.delete();
                    return;
                }

                MediaScannerConnection.scanFile(mContext,
                                                new String[]{mTargetFile.toString()},
                                                null,
                                                null);

                handleSuccess(mTargetFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                handleError();
            }
        }

        void stopWork() {
            mIsWork = false;
            mHandler.removeCallbacksAndMessages(null);
        }

        private void saveBitmap(Bitmap bitmap, File dst) throws IOException {
            dst.createNewFile();
            OutputStream out = null;
            try {
                out = new FileOutputStream(mTargetFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new IOException();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }

        private void handleError() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onError();
                }
            });
        }

        private void handleSuccess(@NonNull final String path) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSuccess(path);
                    mSaveTask = null;
                }
            });
        }
    }
}
