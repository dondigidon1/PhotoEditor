package com.redrocket.photoeditor.gallery.presenter;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.redrocket.photoeditor.presentation.gallery.presenter.GalleryPresenter;
import com.redrocket.photoeditor.presentation.gallery.presenter.GalleryPresenterImpl;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryActivity;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class GalleryPresenterTest {
    @Rule
    public ActivityTestRule<GalleryActivity> mActivityTestRule =
            new ActivityTestRule<>(GalleryActivity.class);

    private GalleryView mGalleryView;

    @Before
    public void beforeEachTest() {
        mGalleryView = mock(GalleryView.class);
    }

    @Test
    public void firstTest() {
        when(mGalleryView.getContext()).thenReturn(mActivityTestRule.getActivity());
        GalleryPresenter presenter = new GalleryPresenterImpl();

        presenter.bindView(mGalleryView);
        verify(mGalleryView, times(1)).setImages(any(List.class));
    }
}