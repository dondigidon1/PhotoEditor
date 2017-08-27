package com.redrocket.photoeditor.presentation.common.picture;

import android.content.Context;

/**
 * Создает {@link PictureBuilder}.
 */
public class PictureFactoryImpl implements PictureFactory {
    private final Context mContext;

    public PictureFactoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public PictureBuilder getBuilder() {
        return new PictureBuilder(mContext);
    }
}
