package com.redrocket.photoeditor.presentation.gallery.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.presentation.crop.view.CropActivity;
import com.redrocket.photoeditor.presentation.gallery.presenter.GalleryPresenter;
import com.redrocket.photoeditor.presentation.gallery.presenter.GalleryPresenterImpl;
import com.redrocket.photoeditor.presentation.gallery.structures.PreviewDescriptor;
import com.redrocket.photoeditor.presentation.gallery.view.grid.GalleryAdapter;
import com.redrocket.photoeditor.presentation.gallery.view.grid.OffsetItemDecorator;

import java.util.List;

/**
 * Экран галереи.
 */
public class GalleryActivity extends AppCompatActivity implements GalleryScreenView,
        GalleryAdapter.OnImageSelectListener {
    private static final String TAG = "GalleryActivity";

    private GalleryAdapter mAdapter;

    private GalleryPresenter mPresenter;


    /**
     * Создать интент для экрана галереи.
     *
     * @param context Контекст.
     * @return Возвращает интент.
     */
    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView mPreviews = (RecyclerView) findViewById(R.id.rv_previews);
        mPreviews.setHasFixedSize(true);

        final int spanCount = getResources().getInteger(R.integer.gallery_span_count);
        mPreviews.setLayoutManager(new GridLayoutManager(null, spanCount));
        final int spacing = getResources().getDimensionPixelOffset(R.dimen.gallery_preview_offset);
        mPreviews.addItemDecoration(new OffsetItemDecorator(spanCount, spacing));

        mAdapter = new GalleryAdapter(this, this);
        mPreviews.setAdapter(mAdapter);

        mPresenter = new GalleryPresenterImpl();
        mPresenter.bindView(this, savedInstanceState != null);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setImages(List<PreviewDescriptor> images) {
        mAdapter.setItems(images);
    }

    @Override
    public void openCropScreen(String image) {
        startActivity(CropActivity.getCallingIntent(this, image));
        overridePendingTransition(R.anim.open, R.anim.hold);
    }

    @Override
    public void onSelectImage(String imagePath) {
        mPresenter.onSelectImage(imagePath);
    }
}