package com.redrocket.photoeditor.presentation.effects.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.presentation.common.FileErrorDialog;
import com.redrocket.photoeditor.presentation.common.effect.Effect;
import com.redrocket.photoeditor.presentation.common.picture.PictureBuilder;
import com.redrocket.photoeditor.presentation.common.picture.PictureLoader;
import com.redrocket.photoeditor.presentation.effects.presenter.EffectPresenter;
import com.redrocket.photoeditor.presentation.effects.presenter.EffectPresenterImpl;
import com.redrocket.photoeditor.presentation.effects.view.previews.PreviewAdapter;
import com.redrocket.photoeditor.presentation.effects.view.previews.PreviewDecorator;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryActivity;
import com.redrocket.photoeditor.presentation.stickers.view.StickerScreenActivity;
import com.redrocket.photoeditor.util.BitmapUtils;
import com.redrocket.photoeditor.util.CropArea;

import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Вью для экрана эффектов.
 */
public class EffectActivity
        extends
            AppCompatActivity
        implements
            EffectScreenView,
            FileErrorDialog.OnDialogListener,
            PictureLoader.ResultHandler {
    private static final String TAG = "EffectActivity";

    private static final String BUNDLE_IMAGE_PATH = "BUNDLE_IMAGE_PATH";
    private static final String BUNDLE_CROP_AREA = "BUNDLE_CROP_AREA";
    private static final String BUNDLE_EFFECT_IDS = "BUNDLE_EFFECT_IDS";
    private static final String BUNDLE_SELECTED_EFFECT_INDEX = "BUNDLE_SELECTED_EFFECT_INDEX";
    private static final String BUNDLE_APPLIED_EFFECT_ID = "BUNDLE_APPLIED_EFFECT_ID";

    private static final String TAG_FILE_ERROR_DIALOG = "TAG_FILE_ERROR_DIALOG";

    private GPUImageView mPictureGpuImage;
    private View mCover;
    private RecyclerView mPreviewRecycler;
    private PreviewAdapter mAdapter;

    private EffectPresenter mPresenter;

    private String mImagePath;
    private CropArea mCropArea;
    private int[] mEffectIds;
    private int mAppliedEffectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPictureGpuImage = (GPUImageView) findViewById(R.id.gpu_image_picture);
        mCover = findViewById(R.id.cover);
        mPreviewRecycler = (RecyclerView) findViewById(R.id.rv_effect_previews);
        int previewSpacing = getResources().getDimensionPixelSize(R.dimen.effect_preview_spacing);
        mPreviewRecycler.addItemDecoration(new PreviewDecorator(previewSpacing));
        ((SimpleItemAnimator) mPreviewRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        if (savedInstanceState != null) {
            mImagePath = savedInstanceState.getString(BUNDLE_IMAGE_PATH);
            mCropArea = (CropArea) savedInstanceState.getSerializable(BUNDLE_CROP_AREA);
            mAppliedEffectId = savedInstanceState.getInt(BUNDLE_APPLIED_EFFECT_ID);
            mEffectIds = savedInstanceState.getIntArray(BUNDLE_EFFECT_IDS);

            FileErrorDialog dialog = (FileErrorDialog) getSupportFragmentManager()
                    .findFragmentByTag(TAG_FILE_ERROR_DIALOG);
            // TODO if maybe must be in presenter or something
            if (dialog != null) {
                dialog.setListener(this);
            } else {
                setImage(mImagePath, mCropArea);
                setEffect(mAppliedEffectId);
                setPreviewEffects(mEffectIds);
                mAdapter.selectEffect(savedInstanceState.getInt(BUNDLE_SELECTED_EFFECT_INDEX));
            }
        }

        mPresenter = new EffectPresenterImpl(PhotoEditorApplication.getProject());
        mPresenter.bindView(this, savedInstanceState != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_effect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                mPresenter.onConfirmEffect(mAdapter.getSelectedIndex());
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_exit, R.anim.slide_out_exit);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_IMAGE_PATH, mImagePath);
        outState.putSerializable(BUNDLE_CROP_AREA, mCropArea);
        outState.putIntArray(BUNDLE_EFFECT_IDS, mEffectIds);
        outState.putInt(BUNDLE_SELECTED_EFFECT_INDEX, mAdapter.getSelectedIndex());
        outState.putInt(BUNDLE_APPLIED_EFFECT_ID, mAppliedEffectId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.recycle();
        }
    }

    @Override
    public void onPictureReady(Bitmap picture) {
        mPictureGpuImage.setImage(picture);
        mCover.animate().alpha(0).setDuration(getResources().
                getInteger(R.integer.effect_screen_cover_fade_anim_duration));
    }

    @Override
    public void onError() {
        mPresenter.onFileError();
    }

    @Override
    public void onDismiss() {
        mPresenter.onCloseFileErrorMsg();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void setImage(String path, CropArea crop) {
        mImagePath = path;
        mCropArea = crop;
        try {
            mPictureGpuImage.setRatio(compRatio(path, crop));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            PictureBuilder builder = PhotoEditorApplication.getPictureFactory().getBuilder()
                    .image(mImagePath)
                    .crop(crop)
                    .minBound(screenWidth, screenHeight);
            PictureLoader loader = new PictureLoader(builder);
            loader.load(this);

        } catch (IOException e) {
            mPresenter.onFileError();
        }
    }

    @Override
    public void setEffect(int effectId) {
        mAppliedEffectId = effectId;
        Effect effect = PhotoEditorApplication.getEffectFactory().getEffect(effectId);
        GPUImageFilter filter = effect.filter;
        mPictureGpuImage.setFilter(filter);
    }

    @Override
    public void setSelectedEffectIndex(int index) {
        mAdapter.selectEffect(index);
    }

    @Override
    public void setPreviewEffects(int[] effectIds) {
        mEffectIds = effectIds;
        Effect[] effects = new Effect[effectIds.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = PhotoEditorApplication.getEffectFactory().getEffect(effectIds[i]);
        }

        mAdapter = new PreviewAdapter(
                this,
                mImagePath,
                mCropArea,
                getResources().getDimensionPixelSize(R.dimen.effect_preview_side),
                effects,

                new PreviewAdapter.OnEffectSelectListener() {
                    @Override
                    public void onEffectSelect(int index) {
                        mPresenter.onClickEffect(index);
                    }

                    @Override
                    public void onFileError() {
                        mPresenter.onFileError();
                    }
                });
        mPreviewRecycler.setAdapter(mAdapter);
    }

    @Override
    public void openStickerScreen() {
        Intent intent = StickerScreenActivity.getCallingIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_enter, R.anim.slide_out_enter);
    }

    @Override
    public void resetToGalleryScreen() {
        startActivity(GalleryActivity.getCallingIntent(this));
    }

    @Override
    public void showFileErrorMsg() {
        FileErrorDialog dialog = new FileErrorDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), TAG_FILE_ERROR_DIALOG);
    }

    private static float compRatio(String path, CropArea crop) throws IOException {
        int rotation = BitmapUtils.getRotation(path);
        Pair<Integer, Integer> srcDims = BitmapUtils.getDims(path);

        float srcRatio = rotation % 180 == 0 ?
                (float) srcDims.first / srcDims.second :
                (float) srcDims.second / srcDims.first;

        return crop.width() / crop.height() * srcRatio;
    }
}
