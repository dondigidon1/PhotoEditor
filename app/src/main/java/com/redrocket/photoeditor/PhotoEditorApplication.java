package com.redrocket.photoeditor;

import android.app.Application;
import android.support.annotation.NonNull;

import com.redrocket.photoeditor.business.Project;
import com.redrocket.photoeditor.data.RepositoryImpl;

/**
 * Приложение.
 * Содержит общие для всего приложения классы.
 */
public class PhotoEditorApplication extends Application {
    private static Project mProject;

    /**
     * Получить проект.
     *
     * @return Возвращает проект
     */
    @NonNull
    public static Project getProject() {
        return mProject;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mProject = new Project(new RepositoryImpl(getApplicationContext()));
    }
}
