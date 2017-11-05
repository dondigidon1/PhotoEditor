package com.redrocket.photoeditor.presentation.save.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.common.dialogs.FileErrorDialog;
import com.redrocket.photoeditor.presentation.common.picture.PictureBuilder;
import com.redrocket.photoeditor.presentation.common.picture.PictureLoader;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryActivity;
import com.redrocket.photoeditor.presentation.save.presenter.SaveScreenPresenter;
import com.redrocket.photoeditor.presentation.save.presenter.SaveScreenPresenterImpl;
import com.redrocket.photoeditor.presentation.save.remover.FileManagerImpl;
import com.redrocket.photoeditor.presentation.save.saver.PictureSaverImpl;
import com.redrocket.photoeditor.presentation.save.view.dialogs.CloseSavingDialog;
import com.redrocket.photoeditor.util.CropArea;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveActivity
    extends
        AppCompatActivity
    implements
        SaveScreenView,
        FileErrorDialog.OnDialogListener,
        CloseSavingDialog.OnDialogListener {

    private static final String TAG = "SaveActivity";

    private static final String BUNDLE_IMAGE_PATH = "BUNDLE_IMAGE_PATH";
    private static final String BUNDLE_CROP_AREA = "BUNDLE_CROP_AREA";
    private static final String BUNDLE_APPLIED_EFFECT_ID = "BUNDLE_APPLIED_EFFECT_ID";
    private static final String BUNDLE_STICKERS = "BUNDLE_STICKERS";
    private static final String BUNDLE_IS_SHOW_RESULT = "BUNDLE_IS_SHOW_RESULT";

    private static final String TAG_FILE_ERROR_DIALOG = "TAG_FILE_ERROR_DIALOG";
    private static final String TAG_CLOSE_SAVING_DIALOG = "TAG_CLOSE_SAVING_DIALOG";

    private static final int PREVIEW_BLUR_RADIUS = 16;

    private ImageView mPreviewImage;
    private ImageView mBackgroundImage;
    private View mLoader;
    private TextView mSavingText;
    private TextView mResultText;
    private View mShareButton;
    private View mFinishButton;

    private String mImagePath;
    private CropArea mCropArea;
    private int mAppliedEffectId;
    private ArrayList<Sticker> mStickers = new ArrayList<>();
    private boolean mIsShowResult = false;

    private SaveScreenPresenter mPresenter;

    private final PictureLoader.ResultHandler previewResultHandler = new PictureLoader.ResultHandler() {
        @Override
        public void onPictureReady(Bitmap picture) {
            handleBackgroundReady(picture);
        }

        @Override
        public void onError() {
            handlePictureError();
        }
    };

    private final PictureLoader.ResultHandler backgroundResultHandler = new PictureLoader.ResultHandler() {
        @Override
        public void onPictureReady(Bitmap picture) {
            handlePreviewReady(picture);
        }

        @Override
        public void onError() {
            handlePictureError();
        }
    };

    /**
     * Создать вызывающий интент.
     *
     * @param context Контекст.
     * @return Возращает вызываюший интент.
     */
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SaveActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPreviewImage = (ImageView) findViewById(R.id.image_preview);
        mBackgroundImage = (ImageView) findViewById(R.id.image_background);
        mLoader = findViewById(R.id.loader);
        mSavingText = (TextView) findViewById(R.id.text_saving);
        mResultText = (TextView) findViewById(R.id.text_result);
        mShareButton =  findViewById(R.id.button_share);
        mFinishButton = findViewById(R.id.button_finish);

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClick();
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishClick();
            }
        });

        if (savedInstanceState != null) {
            mIsShowResult = savedInstanceState.getBoolean(BUNDLE_IS_SHOW_RESULT);
            if (mIsShowResult) {
                getSupportActionBar().setTitle(getResources().getString(R.string.save_result_title));

                mLoader.setVisibility(View.INVISIBLE);
                mSavingText.setVisibility(View.INVISIBLE);

                mShareButton.setVisibility(View.VISIBLE);
                mFinishButton.setVisibility(View.VISIBLE);
                mResultText.setVisibility(View.VISIBLE);
            } else {
                mLoader.setVisibility(View.VISIBLE);
                mSavingText.setVisibility(View.VISIBLE);

                mShareButton.setVisibility(View.INVISIBLE);
                mFinishButton.setVisibility(View.INVISIBLE);
                mResultText.setVisibility(View.INVISIBLE);
            }

            mImagePath = savedInstanceState.getString(BUNDLE_IMAGE_PATH);
            mCropArea = (CropArea) savedInstanceState.getSerializable(BUNDLE_CROP_AREA);
            mAppliedEffectId = savedInstanceState.getInt(BUNDLE_APPLIED_EFFECT_ID);
            mStickers = savedInstanceState.getParcelableArrayList(BUNDLE_STICKERS);

            setPreview(mImagePath, mCropArea, mAppliedEffectId, mStickers);

            FileErrorDialog errorDialog = (FileErrorDialog) getSupportFragmentManager()
                    .findFragmentByTag(TAG_FILE_ERROR_DIALOG);
            // TODO if maybe must be in presenter or something
            if (errorDialog != null) {
                errorDialog.setListener(this);
            }

            CloseSavingDialog closeDialog = (CloseSavingDialog) getSupportFragmentManager()
                    .findFragmentByTag(TAG_CLOSE_SAVING_DIALOG);
            // TODO if maybe must be in presenter or something
            if (closeDialog != null) {
                closeDialog.setListener(this);
            }
        }

        mPresenter = new SaveScreenPresenterImpl(
                PhotoEditorApplication.getProject(),
                new PictureSaverImpl(this, PhotoEditorApplication.getPictureFactory()),
                new FileManagerImpl());
        mPresenter.bindView(this, savedInstanceState != null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_IMAGE_PATH, mImagePath);
        outState.putSerializable(BUNDLE_CROP_AREA, mCropArea);
        outState.putInt(BUNDLE_APPLIED_EFFECT_ID, mAppliedEffectId);
        outState.putParcelableArrayList(BUNDLE_STICKERS, mStickers);
        outState.putBoolean(BUNDLE_IS_SHOW_RESULT, mIsShowResult);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mPresenter.onBackClick();
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
    public Context getContext() {
        return this;
    }

    private void onShareClick() {
        mPresenter.onShareClick();
    }

    private void onFinishClick() {
        mPresenter.onFinishClick();
    }

    @Override
    public void onDismiss() {
        mPresenter.onCloseFileErrorMsg();
    }

    @Override
    public void onStopSavingClick() {
        mPresenter.onStopSavingClick();
    }

    //region SaveScreenView
    @Override
    public void switchToResult() {
        mIsShowResult = true;
        showResult();
    }

    @Override
    public void resetToGalleryScreen() {
        startActivity(GalleryActivity.getCallingIntent(this));
    }

    @Override
    public void closeScreen() {
        finish();
    }

    @Override
    public void showFileErrorMsg() {
        FileErrorDialog dialog = new FileErrorDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), TAG_FILE_ERROR_DIALOG);
    }

    @Override
    public void showCloseSavingDialog() {
        CloseSavingDialog dialog = new CloseSavingDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), TAG_CLOSE_SAVING_DIALOG);
    }

    @Override
    public void shareImage(@NonNull String path) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));

        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.save_share)));
    }

    @Override
    public void setPreview(@NonNull String path, @NonNull CropArea crop, int effect, @NonNull List<Sticker> stickers) {
        mImagePath = path;
        mCropArea = crop;
        mAppliedEffectId = effect;
        mStickers = (ArrayList<Sticker>) stickers;

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        PictureBuilder previewBuilder = PhotoEditorApplication.getPictureFactory()
                .getBuilder()
                .image(path)
                .crop(crop)
                .effect(effect)
                .stickers(stickers)
                .maxBound(display.widthPixels, display.heightPixels);

        PictureLoader previewLoader = new PictureLoader(previewBuilder);
        previewLoader.load(backgroundResultHandler);

        PictureBuilder backgroundBuilder = PhotoEditorApplication.getPictureFactory()
                .getBuilder()
                .image(path)
                .crop(crop)
                .effect(effect)
                .stickers(stickers)
                .blur(PREVIEW_BLUR_RADIUS)
                .maxBound(display.widthPixels / 2, display.heightPixels / 2);

        PictureLoader backgroundLoader = new PictureLoader(backgroundBuilder);
        backgroundLoader.load(previewResultHandler);
    }

    private void handlePreviewReady(Bitmap picture) {
        mPreviewImage.setImageBitmap(picture);
        mPreviewImage.animate().alpha(1).setDuration(getResources().
                getInteger(R.integer.save_screen_picture_fade_anim_duration));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPreviewImage.getLayoutParams();
        params.dimensionRatio = picture.getWidth() + ":" + picture.getHeight();
        mPreviewImage.setLayoutParams(params);
    }

    private void handleBackgroundReady(Bitmap picture) {
        mBackgroundImage.setImageBitmap(picture);
        mBackgroundImage.animate().alpha(1).setDuration(getResources().
                getInteger(R.integer.save_screen_picture_fade_anim_duration));
    }

    private void handlePictureError() {
        mPresenter.onFileError();
    }

    @Override
    public void switchToLoading() {
        mIsShowResult = false;

        mLoader.setVisibility(View.VISIBLE);
        mSavingText.setVisibility(View.VISIBLE);
    }
    //endregion

    private void showResult() {
        getSupportActionBar().setTitle(getResources().getString(R.string.save_result_title));

        mShareButton.setVisibility(View.VISIBLE);
        mFinishButton.setVisibility(View.VISIBLE);
        mResultText.setVisibility(View.VISIBLE);

        Animation animIn = AnimationUtils.loadAnimation(this, R.anim.save_controls_fade_in);
        mShareButton.startAnimation(animIn);
        mFinishButton.startAnimation(animIn);
        mResultText.startAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(this, R.anim.save_controls_fade_out);
        mLoader.startAnimation(animOut);
        mSavingText.startAnimation(animOut);

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mLoader.setVisibility(View.INVISIBLE);
                mSavingText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
