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

    private final List<StickerCategory> mCategories = new ArrayList<>();

    public StickersTable() {

        List<StickerInfo> smileStickers = new ArrayList<>();
        smileStickers.add(createSticker(R.drawable.sticker_smile_1, R.drawable.big_sticker_smile_1, "sticker_8"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_2, R.drawable.big_sticker_smile_2, "sticker_9"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_3, R.drawable.big_sticker_smile_3, "sticker_10"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_4, R.drawable.big_sticker_smile_4, "sticker_11"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_5, R.drawable.big_sticker_smile_5, "sticker_12"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_6, R.drawable.big_sticker_smile_6, "sticker_13"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_7, R.drawable.big_sticker_smile_7, "sticker_14"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_8, R.drawable.big_sticker_smile_8, "sticker_8"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_9, R.drawable.big_sticker_smile_9, "sticker_9"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_10, R.drawable.big_sticker_smile_10, "sticker_10"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_11, R.drawable.big_sticker_smile_11, "sticker_11"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_12, R.drawable.big_sticker_smile_12, "sticker_12"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_13, R.drawable.big_sticker_smile_13, "sticker_13"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_14, R.drawable.big_sticker_smile_14, "sticker_14"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_15, R.drawable.big_sticker_smile_15, "sticker_13"));
        smileStickers.add(createSticker(R.drawable.sticker_smile_16, R.drawable.big_sticker_smile_16, "sticker_14"));
        StickerCategory smileCategory = new StickerCategory(
                R.drawable.sticker_smile_1,
                "https://www.freepik.com/free-vector/several-emoticons-in-flat-style_950559.htm",
                smileStickers);
        mCategories.add(smileCategory);

        List<StickerInfo> travelStickers = new ArrayList<>();
        travelStickers.add(createSticker(R.drawable.sticker_travel_1, R.drawable.big_sticker_travel_1, "sticker_8"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_2, R.drawable.big_sticker_travel_2, "sticker_9"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_3, R.drawable.big_sticker_travel_3, "sticker_10"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_4, R.drawable.big_sticker_travel_4, "sticker_11"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_5, R.drawable.big_sticker_travel_5, "sticker_12"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_6, R.drawable.big_sticker_travel_6, "sticker_13"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_7, R.drawable.big_sticker_travel_7, "sticker_14"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_8, R.drawable.big_sticker_travel_8, "sticker_8"));
        travelStickers.add(createSticker(R.drawable.sticker_travel_9, R.drawable.big_sticker_travel_9, "sticker_9"));
        StickerCategory travelCategory = new StickerCategory(
                R.drawable.sticker_travel_7,
                "https://www.freepik.com/free-vector/world-tourism-day-background-with-world-and-monuments_915534.htm",
                travelStickers);
        mCategories.add(travelCategory);

        List<StickerInfo> maskStickers = new ArrayList<>();
        maskStickers.add(createSticker(R.drawable.sticker_mask_1, R.drawable.big_sticker_mask_1, "sticker_8"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_2, R.drawable.big_sticker_mask_2, "sticker_9"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_3, R.drawable.big_sticker_mask_3, "sticker_10"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_4, R.drawable.big_sticker_mask_4, "sticker_11"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_5, R.drawable.big_sticker_mask_5, "sticker_12"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_6, R.drawable.big_sticker_mask_6, "sticker_13"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_7, R.drawable.big_sticker_mask_7, "sticker_14"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_8, R.drawable.big_sticker_mask_8, "sticker_8"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_9, R.drawable.big_sticker_mask_9, "sticker_9"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_10, R.drawable.big_sticker_mask_10, "sticker_10"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_11, R.drawable.big_sticker_mask_11, "sticker_11"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_12, R.drawable.big_sticker_mask_12, "sticker_12"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_13, R.drawable.big_sticker_mask_13, "sticker_13"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_14, R.drawable.big_sticker_mask_14, "sticker_14"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_15, R.drawable.big_sticker_mask_15, "sticker_13"));
        maskStickers.add(createSticker(R.drawable.sticker_mask_16, R.drawable.big_sticker_mask_16, "sticker_14"));
        StickerCategory maskCategory = new StickerCategory(
                R.drawable.sticker_mask_16,
                "https://www.freepik.com/free-vector/several-accessories-for-photo-booth_1039964.htm",
                maskStickers);
        mCategories.add(maskCategory);
    }

    private StickerInfo createSticker(@DrawableRes int miniImage, @DrawableRes int bigImage, String name) {
        int id = mLastId++;
        StickerInfo sticker = new StickerInfo(id, miniImage, bigImage, name);

        mStickerById.put(id, sticker);
        mStickers.add(sticker);

        return sticker;
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

    /**
     * Получить категории стикеров.
     *
     * @return Возвращает немодифицируемый набор.
     */
    public List<StickerCategory> getCategories() {
        return Collections.unmodifiableList(mCategories);
    }
}
