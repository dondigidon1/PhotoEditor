package com.redrocket.photoeditor.presentation.common.sticker;

import android.support.annotation.DrawableRes;

import com.redrocket.photoeditor.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранит данные о имеющихся в приложении стикерах.
 */
public class StickersTable {
    private final List<StickerInfo> mStickers = new ArrayList<>();
    private final Map<Integer, StickerInfo> mStickerById = new HashMap<>();

    private int mLastId;

    public StickersTable() {
        add(R.drawable.emoji_smile, R.drawable.emoji_smile, "smile");
        add(R.drawable.emoji_ghost, R.drawable.emoji_ghost, "ghost");
        add(R.drawable.emoji_french_fries, R.drawable.emoji_french_fries, "french_fries");
        add(R.drawable.emoji_rocket, R.drawable.emoji_rocket, "rocket");
        add(R.drawable.emoji_smile, R.drawable.emoji_smile, "smile");
        add(R.drawable.emoji_ghost, R.drawable.emoji_ghost, "ghost");
        add(R.drawable.emoji_french_fries, R.drawable.emoji_french_fries, "french_fries");
        add(R.drawable.emoji_rocket, R.drawable.emoji_rocket, "rocket");
        add(R.drawable.emoji_smile, R.drawable.emoji_smile, "smile");
        add(R.drawable.emoji_ghost, R.drawable.emoji_ghost, "ghost");
        add(R.drawable.emoji_french_fries, R.drawable.emoji_french_fries, "french_fries");
        add(R.drawable.emoji_rocket, R.drawable.emoji_rocket, "rocket");
        add(R.drawable.emoji_smile, R.drawable.emoji_smile, "smile");
        add(R.drawable.emoji_ghost, R.drawable.emoji_ghost, "ghost");
        add(R.drawable.emoji_french_fries, R.drawable.emoji_french_fries, "french_fries");
        add(R.drawable.emoji_rocket, R.drawable.emoji_rocket, "rocket");
    }

    private void add(@DrawableRes int miniImage, @DrawableRes int bigImage, String name) {
        int id = mLastId++;
        StickerInfo sticker = new StickerInfo(id, miniImage, bigImage, name);

        mStickerById.put(id, sticker);
        mStickers.add(sticker);
    }

    /**
     * Получить все стикеры.
     *
     * @return Возращает коллекцию стикеров.
     */
    public List<StickerInfo> getStickers() {
        return Collections.unmodifiableList(mStickers);
    }

    /**
     * Получить стикер по идентификатору.
     *
     * @param id Идентификатор стикера.
     * @return Возвращает стикер.
     */
    public StickerInfo getStickerById(int id) {
        return mStickerById.get(id);
    }
}
