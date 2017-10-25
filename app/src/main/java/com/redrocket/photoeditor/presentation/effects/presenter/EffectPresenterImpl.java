package com.redrocket.photoeditor.presentation.effects.presenter;

import android.support.annotation.NonNull;

import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.presentation.effects.view.EffectScreenView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Презентер для экнара эффектов.
 */
public class EffectPresenterImpl implements EffectPresenter {
    private static final String TAG = "EffectPresenterImpl";

    private static final int[] EFFECTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    private final Project mProject;

    private EffectScreenView mView;

    public EffectPresenterImpl(Project project) {
        mProject = project;
    }

    @Override
    public void bindView(@NonNull EffectScreenView view, boolean isRestore) {
        mView = view;

        String path = mProject.getPicture().getPath();
        CropArea crop = mProject.getPicture().getCrop();
        int effect = mProject.getPicture().getEffect();

        if (!isRestore) {
            mView.setImage(path, crop);

            mView.setPreviewEffects(EFFECTS);
            int effectIndex = 0;
            for (int i = 0; i < EFFECTS.length; i++) {
                if (effect == EFFECTS[i]) {
                    effectIndex = i;
                    break;
                }
            }

            mView.setSelectedEffectIndex(effectIndex);
            mView.setEffect(effect);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onClickEffect(int index) {
        mView.setEffect(EFFECTS[index]);
    }

    @Override
    public void onConfirmEffect(int index) {
        mProject.getPicture().setEffect(EFFECTS[index]);
        mView.openStickerScreen();
    }

    @Override
    public void onFileError() {
        mView.showFileErrorMsg();
    }

    @Override
    public void onCloseFileErrorMsg() {
        mView.resetToGalleryScreen();
    }
}
