package com.redrocket.photoeditor.screens.presentation.gallery.structures;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Описывает превью изображения.
 */
public class PreviewDescriptor {
    public final String imagePath;
    public final String thumbPath;

    public PreviewDescriptor(@NonNull final String imagePath,
                             @Nullable final String thumbPath) {
        this.thumbPath = thumbPath;
        this.imagePath = imagePath;
    }
}
