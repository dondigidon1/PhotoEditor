package com.redrocket.photoeditor.presentation.stickers.presenter;

import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.stickers.view.StickerScreenView;
import com.redrocket.photoeditor.util.CropArea;

/**
 * Презентер для экнара стикеров.
 */
public class StickerScreenPresenterImpl implements StickerScreenPresenter {

    private final Project mProject;

    private StickerScreenView mView;

    public StickerScreenPresenterImpl(Project project) {
        mProject = project;
    }

    @Override
    public void bindView(StickerScreenView view, boolean isRestore) {
        mView = view;

        String path = mProject.getPicture().getPath();
        CropArea crop = mProject.getPicture().getCrop();
        int effect = mProject.getPicture().getEffect();

        if (!isRestore) {
            mView.setImage(path, crop, effect);
        }
    }

    @Override
    public void destroy() {}

    @Override
    public void onPickSticker(int id) {
        mView.addNewSticker(id);
    }

    @Override
    public void onConfirmStickers(Sticker[] stickers) {
        mProject.getPicture().setStickers(stickers);
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
