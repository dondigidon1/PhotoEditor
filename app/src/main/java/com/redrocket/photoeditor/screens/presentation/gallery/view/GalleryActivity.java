package com.redrocket.photoeditor.screens.presentation.gallery.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.screens.presentation.gallery.presenter.GalleryPresenter;
import com.redrocket.photoeditor.screens.presentation.gallery.presenter.GalleryPresenterImpl;
import com.redrocket.photoeditor.screens.presentation.gallery.structures.PreviewDescriptor;

import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryView {

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;

    private GalleryPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final int spanCount = getResources().getInteger(R.integer.gallery_span_count);

        mRecyclerView.setLayoutManager(new GridLayoutManager(null, spanCount));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GalleryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        final int spacing = getResources().getDimensionPixelOffset(R.dimen.gallery_preview_offset);
        mRecyclerView.addItemDecoration(new OffsetItemDecorator(spanCount, spacing));

        mPresenter = new GalleryPresenterImpl();
        mPresenter.bindView(this);
    }

    @Override
    public void setImages(List<PreviewDescriptor> images) {
        mAdapter.setItems(images);
    }

    @Override
    public void openCrop(Uri image) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context getContext() {
        return this;
    }
}