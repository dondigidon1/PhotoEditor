package com.redrocket.photoeditor.presentation.save.presenter;

import android.support.annotation.NonNull;

import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.save.remover.FileManager;
import com.redrocket.photoeditor.presentation.save.saver.PictureSaver;
import com.redrocket.photoeditor.presentation.save.view.SaveScreenView;
import com.redrocket.photoeditor.util.CropArea;

import java.util.List;

/**
 * Реализация презентера для экрана сохранения.
 */
public class SaveScreenPresenterImpl
    implements
        SaveScreenPresenter,
        PictureSaver.PictureSaverListener {

    private static final String TAG = "SaveScreenPresenterImpl";

    private final Project mProject;
    private final PictureSaver mSaver;
    private final FileManager mManager;

    private SaveScreenView mView;

    public SaveScreenPresenterImpl(@NonNull Project project,
                                   @NonNull PictureSaver saver,
                                   @NonNull FileManager manager) {
        mProject = project;
        mSaver = saver;
        mManager = manager;
    }

    @Override
    public void bindView(@NonNull SaveScreenView view, boolean isRestore) {
        mView = view;

        String path = mProject.getPicture().getPath();
        CropArea crop = mProject.getPicture().getCrop();
        int effect = mProject.getPicture().getEffect();
        List<Sticker> stickers = mProject.getPicture().getStickers();

        if (!isRestore) {
            mView.setPreview(path, crop, effect, stickers);
            mView.switchToLoading();
            mSaver.save(path, crop, effect, stickers, this);
        } else {
            boolean isSaved = mProject.getPicture().getSavedPath() != null;
            if (!isSaved) {
                mSaver.save(path, crop, effect, stickers, this);
            }
        }
    }

    @Override
    public void destroy() {}

    @Override
    public void onError() {
        mView.showFileErrorMsg();
    }

    //region SaveScreenView
    @Override
    public void onSuccess(@NonNull String path) {
        mProject.getPicture().setSavedPath(path);
        mView.switchToResult();
    }

    @Override
    public void onFileError() {
        mView.showFileErrorMsg();
    }

    @Override
    public void onCloseFileErrorMsg() {
        mView.resetToGalleryScreen();
    }

    @Override
    public void onBackClick() {
        if (mSaver.isSaving()) {
            mView.showCloseSavingDialog();
        }else {
            mView.closeScreen();
        }
    }

    @Override
    public void onStopSavingClick() {
        if (mSaver.isSaving()) {
            mSaver.stopSaving();
        }

        mView.closeScreen();
    }

    @Override
    public void onShareClick() {
        if (mManager.contains(mProject.getPicture().getSavedPath())) {
            mView.shareImage(mProject.getPicture().getSavedPath());
        } else {
            mView.showFileErrorMsg();
        }
    }

    @Override
    public void onFinishClick() {
        mView.resetToGalleryScreen();
    }
    //endregion
}
