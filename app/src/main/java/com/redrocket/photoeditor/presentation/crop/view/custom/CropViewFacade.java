package com.redrocket.photoeditor.presentation.crop.view.custom;

import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import com.redrocket.photoeditor.util.BitmapUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

/**
 * Фасад для {@link CropImageView}.
 */
public class CropViewFacade
        implements CropImageView.OnSetImageUriCompleteListener {
    private static final String TAG = "CropViewFacade";

    private static final String BUNDLE_IMAGE_PATH = "BUNDLE_IMAGE_PATH";
    private static final String BUNDLE_INIT_RECT = "BUNDLE_INIT_RECT";

    private final CropImageView mCropImageView;

    private String mPath;
    private boolean mLoadFinished = false;
    private RectF mInitRect;

    private final LoadListener mListener;

    public CropViewFacade(CropImageView cropImageView, Bundle bundle, LoadListener listener) {
        mCropImageView = cropImageView;

        if (bundle != null) {
            mPath = bundle.getString(BUNDLE_IMAGE_PATH);
            mInitRect = bundle.getParcelable(BUNDLE_INIT_RECT);
        }

        mListener = listener;

        mCropImageView.setOnSetImageUriCompleteListener(this);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (mInitRect != null && error == null) {
            mLoadFinished = true;
            applyCropRect(mInitRect);
        } else if (error != null) {
            mListener.onFileError();
        }
    }

    public void setImage(String path) {
        mPath = path;
        mCropImageView.setImageUriAsync(Uri.fromFile(new File(mPath)));
    }

    public void setCropRect(RectF rect) {
        if (mLoadFinished) {
            applyCropRect(rect);
        } else {
            mInitRect = rect;
        }
    }

    public RectF getCropRect() throws IOException {
        if (mLoadFinished) {
            Rect imageRect = mCropImageView.getWholeImageRect();
            Rect cropRect = mCropImageView.getCropRect();

            float leftRel = (float) cropRect.left / imageRect.width();
            float topRel = (float) cropRect.top / imageRect.height();
            float rightRel = (float) cropRect.right / imageRect.width();
            float bottomRel = (float) cropRect.bottom / imageRect.height();

            RectF srcRelativeRect = new RectF(leftRel, topRel, rightRel, bottomRel);

            return BitmapUtils.rotateRect(srcRelativeRect,
                    BitmapUtils.getRotation(mPath),
                    0.5f, 0.5f);
        } else {
            return mInitRect;
        }
    }

    public void saveState(Bundle bundle) {
        bundle.putString(BUNDLE_IMAGE_PATH, mPath);
        bundle.putParcelable(BUNDLE_INIT_RECT, mInitRect);
    }

    private void applyCropRect(RectF rect) {
        Rect imageRect = mCropImageView.getWholeImageRect();

        int leftAbs = (int) (rect.left * imageRect.width());
        int topAbs = (int) (rect.top * imageRect.height());
        int rightAbs = (int) (rect.right * imageRect.width());
        int bottomAbs = (int) (rect.bottom * imageRect.height());
        Rect cropRect = new Rect(leftAbs, topAbs, rightAbs, bottomAbs);

        mCropImageView.setCropRect(cropRect);
    }

    public interface LoadListener {
        void onFileError();
    }
}
