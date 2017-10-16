package com.redrocket.photoeditor.presentation.crop.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.presentation.crop.view.CropScreenView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Презентер экнара для кропа.
 */
public class CropPresenterImpl implements CropPresenter {
    private static final String TAG = "CropPresenterImpl";

    private final Project mProject;

    private String mPath;
    private CropArea mCropArea;
    private boolean mFromAnotherApp;

    private CropScreenView mView;

    public CropPresenterImpl(Project project) {
        mProject = project;
    }

    @Override
    public void initialize(@Nullable String imagePath, boolean fromAnotherApp) {
        if (imagePath != null)
            mProject.startPicture(imagePath);

        mPath = mProject.getPicture().getPath();
        mCropArea = mProject.getPicture().getCrop();

        mFromAnotherApp = fromAnotherApp;
    }

    @Override
    public void bindView(@NonNull CropScreenView view, boolean isRestore) {
        mView = view;

        if (!isRestore)
            updateImage();

        if (!mFromAnotherApp)
            mView.showBackControl();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onNextClick(@NonNull CropArea crop) {
        mCropArea = crop;
        mProject.getPicture().setCrop(crop);
        mView.openEffectScreen();
    }

    @Override
    public void onFileError() {
        mView.showFileErrorMsg();
    }

    @Override
    public void onCloseFileErrorMsg() {
        mView.resetToGalleryScreen();
    }

    private void updateImage() {
        mView.setImage(mPath, mCropArea);
    }
}
