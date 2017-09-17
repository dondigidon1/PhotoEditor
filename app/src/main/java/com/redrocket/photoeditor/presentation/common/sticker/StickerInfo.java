package com.redrocket.photoeditor.presentation.common.sticker;

import android.support.annotation.DrawableRes;

/**
 * Данные стикера.
 */
public class StickerInfo {
    /** Идентификатор стикера */
    public final int id;
    /** Ресурс миниатюры стикера */
    public final @DrawableRes int miniImage;
    /** Ресурс большой версии стикера */
    public final @DrawableRes int bigImage;
    /** Имя стикера */
    public final String name;

    StickerInfo(int id, @DrawableRes int miniImage, @DrawableRes int bigImage, String name) {
        this.id = id;
        this.miniImage = miniImage;
        this.bigImage = bigImage;
        this.name = name;
    }
}
