package com.redrocket.photoeditor.screens.presentation.gallery.presenter;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.redrocket.photoeditor.screens.presentation.MvpPresenter;
import com.redrocket.photoeditor.screens.presentation.gallery.view.GalleryView;

/**
 * @TODO Class Description ...
 */
public interface GalleryPresenter extends MvpPresenter<GalleryView> {
    void onSelectImage(@NonNull final Uri image);
}
