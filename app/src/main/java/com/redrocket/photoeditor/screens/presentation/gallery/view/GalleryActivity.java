package com.redrocket.photoeditor.screens.presentation.gallery.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.screens.presentation.gallery.presenter.GalleryPresenter;
import com.redrocket.photoeditor.screens.presentation.gallery.presenter.GalleryPresenterImpl;
import com.redrocket.photoeditor.screens.presentation.gallery.structures.PreviewDescriptor;

import java.util.List;

/**
 * Экран галереи.
 */
public class GalleryActivity extends AppCompatActivity implements GalleryView {

    private RecyclerView mPreviews;
    private GalleryAdapter mAdapter;

    private GalleryPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mPreviews = (RecyclerView) findViewById(R.id.rv_previews);
        mPreviews.setHasFixedSize(true);

        final int spanCount = getResources().getInteger(R.integer.gallery_span_count);
        mPreviews.setLayoutManager(new GridLayoutManager(null, spanCount));
        final int spacing = getResources().getDimensionPixelOffset(R.dimen.gallery_preview_offset);
        mPreviews.addItemDecoration(new OffsetItemDecorator(spanCount, spacing));

        mAdapter = new GalleryAdapter(this);
        mPreviews.setAdapter(mAdapter);

        mPresenter = new GalleryPresenterImpl();
        mPresenter.bindView(this);
    }

    @Override
    public void setImages(List<PreviewDescriptor> images) {
        mAdapter.setItems(images);
    }

    @Override
    public void openCrop(String image) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context getContext() {
        return this;
    }
}