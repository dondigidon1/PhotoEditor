package com.redrocket.photoeditor.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.redrocket.photoeditor.business.PictureState;
import com.redrocket.photoeditor.business.Repository;

/**
 * Реализация хранилища для изображений из проекта.
 */
public class RepositoryImpl implements Repository {
    private static final String SHARED_NAME = "ProjectManager_SHARED_NAME";
    private static final String PREF_PICTURE = "PREF_PICTURE";

    private final Gson mGson = new Gson();
    private final SharedPreferences mStorage;

    /**
     * Простой конструктор.
     *
     * @param context Контекст.
     */
    public RepositoryImpl(Context context) {
        mStorage = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void savePicture(PictureState picture) {
        String pictureJson = mGson.toJson(picture);

        SharedPreferences.Editor editor = mStorage.edit();
        editor.putString(PREF_PICTURE, pictureJson);
        editor.apply();
    }

    @Override
    public PictureState loadPicture() {
        if (mStorage.contains(PREF_PICTURE)) {
            return mGson.fromJson(mStorage.getString(PREF_PICTURE, null), PictureState.class);
        } else {
            return null;
        }
    }
}
