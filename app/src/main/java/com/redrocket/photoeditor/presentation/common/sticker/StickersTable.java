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
        add(R.drawable.sticker_1, R.drawable.big_sticker_1, "sticker_1");
        add(R.drawable.sticker_2, R.drawable.big_sticker_2, "sticker_2");
        add(R.drawable.sticker_3, R.drawable.big_sticker_3, "sticker_3");
        add(R.drawable.sticker_4, R.drawable.big_sticker_4, "sticker_4");
        add(R.drawable.sticker_5, R.drawable.big_sticker_5, "sticker_5");
        add(R.drawable.sticker_6, R.drawable.big_sticker_6, "sticker_6");
        add(R.drawable.sticker_7, R.drawable.big_sticker_7, "sticker_7");
        add(R.drawable.sticker_8, R.drawable.big_sticker_8, "sticker_8");
        add(R.drawable.sticker_9, R.drawable.big_sticker_9, "sticker_9");
        add(R.drawable.sticker_10, R.drawable.big_sticker_10, "sticker_10");
        add(R.drawable.sticker_11, R.drawable.big_sticker_11, "sticker_11");
        add(R.drawable.sticker_12, R.drawable.big_sticker_12, "sticker_12");
        add(R.drawable.sticker_13, R.drawable.big_sticker_13, "sticker_13");
        add(R.drawable.sticker_14, R.drawable.big_sticker_14, "sticker_14");
        add(R.drawable.sticker_15, R.drawable.big_sticker_15, "sticker_15");
        add(R.drawable.sticker_16, R.drawable.big_sticker_16, "sticker_16");
        add(R.drawable.sticker_17, R.drawable.big_sticker_17, "sticker_17");
        add(R.drawable.sticker_18, R.drawable.big_sticker_18, "sticker_18");
        add(R.drawable.sticker_19, R.drawable.big_sticker_19, "sticker_19");
        add(R.drawable.sticker_20, R.drawable.big_sticker_20, "sticker_20");
        add(R.drawable.sticker_21, R.drawable.big_sticker_21, "sticker_21");
        add(R.drawable.sticker_22, R.drawable.big_sticker_22, "sticker_22");
        add(R.drawable.sticker_23, R.drawable.big_sticker_23, "sticker_23");
        add(R.drawable.sticker_24, R.drawable.big_sticker_24, "sticker_24");
        add(R.drawable.sticker_25, R.drawable.big_sticker_25, "sticker_25");
        add(R.drawable.sticker_26, R.drawable.big_sticker_26, "sticker_26");
        add(R.drawable.sticker_27, R.drawable.big_sticker_27, "sticker_27");
        add(R.drawable.sticker_28, R.drawable.big_sticker_28, "sticker_28");
        add(R.drawable.sticker_29, R.drawable.big_sticker_29, "sticker_29");
        add(R.drawable.sticker_30, R.drawable.big_sticker_30, "sticker_30");
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
