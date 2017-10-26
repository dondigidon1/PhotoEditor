package com.redrocket.photoeditor;

import android.app.Application;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.data.RepositoryImpl;
import com.redrocket.photoeditor.presentation.common.effect.EffectFactory;
import com.redrocket.photoeditor.presentation.common.effect.EffectFactoryImpl;
import com.redrocket.photoeditor.presentation.common.picture.PictureFactory;
import com.redrocket.photoeditor.presentation.common.picture.PictureFactoryImpl;
import io.fabric.sdk.android.Fabric;

/**
 * Приложение.
 * Содержит общие для всего приложения классы.
 */
public class PhotoEditorApplication extends Application {
    private static Project mProject;
    private static PictureFactory mPictureFactory;
    private static EffectFactory mEffectFactory;

    /**
     * Получить проект.
     *
     * @return Возвращает проект
     */
    @NonNull
    public static Project getProject() {
        return mProject;
    }

    /**
     * Получить фабрику сборщиков изображения.
     *
     * @return Возвращает фабрику.
     */
    public static PictureFactory getPictureFactory() {
        return mPictureFactory;
    }

    /**
     * Получить фабрику эффектов.
     *
     * @return Возвращает фабрику.
     */
    public static EffectFactory getEffectFactory() {
        return mEffectFactory;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        mProject = new Project(new RepositoryImpl(this));
        mPictureFactory = new PictureFactoryImpl(this);
        mEffectFactory = new EffectFactoryImpl(this);
    }
}
