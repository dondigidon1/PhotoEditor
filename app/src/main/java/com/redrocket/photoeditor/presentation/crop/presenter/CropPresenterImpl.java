package com.redrocket.photoeditor.presentation.crop.presenter;

import android.support.annotation.Nullable;

import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.presentation.crop.view.CropView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Презентер экнара для кропа.
 */
public class CropPresenterImpl implements CropPresenter {
    private static final String TAG = "CropPresenterImpl";

    private Project mProject;

    private String mPath;
    private CropArea mCropArea;
    private boolean mFromAnotherApp;

    private CropView mView;

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
    public void bindView(CropView view, boolean isRestore) {
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
    public void onNextClick(CropArea crop) {
        mCropArea = crop;
        mProject.getPicture().setCrop(crop);
        mView.openEffectScreen();
    }

    private void updateImage() {
        mView.setImage(mPath, mCropArea);
    }
}
