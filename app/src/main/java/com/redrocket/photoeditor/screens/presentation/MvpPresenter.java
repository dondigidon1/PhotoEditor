package com.redrocket.photoeditor.screens.presentation;

public interface MvpPresenter<V extends MvpView> {

    void bindView(V view);

    void resume();

    void pause();

    void destroy();
}