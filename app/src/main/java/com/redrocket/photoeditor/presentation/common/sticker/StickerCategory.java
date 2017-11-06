package com.redrocket.photoeditor.presentation.common.sticker;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * Категория стикеров.
 */
public class StickerCategory {
    private final @DrawableRes int mIcon;
    private final String mCreditLink;
    private final List<StickerInfo> mStickers;

    /**
     * @param icon     Идентификатор ресурса с иконкой категории.
     * @param link     Ссылка на автора.
     * @param stickers Стикеры.
     */
    StickerCategory(@DrawableRes int icon, String link, @NonNull List<StickerInfo> stickers) {
        mIcon = icon;
        mCreditLink = link;
        mStickers = stickers;
    }

    /**
     * Получить стикеры.
     *
     * @return Возвращает немодифицируемый набор стикеров.
     */
    @NonNull
    public List<StickerInfo> getStickers() {
        return Collections.unmodifiableList(mStickers);
    }

    /**
     * Получить ссылку.
     *
     * @return Возвращает url в виде строки.
     */
    public String getLink() {
        return mCreditLink;
    }

    /**
     * Получить иконку категории.
     *
     * @return Возвращает
     */
    public int getIcon() {
        return mIcon;
    }
}
