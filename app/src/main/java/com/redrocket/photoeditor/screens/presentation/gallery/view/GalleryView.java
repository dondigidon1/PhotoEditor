package com.redrocket.photoeditor.screens.presentation.gallery.view;

import android.net.Uri;

import com.redrocket.photoeditor.screens.presentation.MvpView;
import com.redrocket.photoeditor.screens.presentation.gallery.structures.PreviewDescriptor;

import java.util.List;

public interface GalleryView extends MvpView {

    void setImages(List<PreviewDescriptor> images);

    void openCrop(Uri image);
}
