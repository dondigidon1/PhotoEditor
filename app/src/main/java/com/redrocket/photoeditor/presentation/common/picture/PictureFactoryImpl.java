package com.redrocket.photoeditor.presentation.common.picture;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Создает {@link PictureBuilder}.
 */
public class PictureFactoryImpl implements PictureFactory {
    private final Context mContext;

    public PictureFactoryImpl(Context context) {
        mContext = context;
    }

    @Override
    @NonNull
    public PictureBuilder getBuilder() {
        return new PictureBuilder(mContext);
    }
}
