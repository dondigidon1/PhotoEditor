package com.redrocket.photoeditor.presentation.crop.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.presentation.common.dialogs.FileErrorDialog;
import com.redrocket.photoeditor.presentation.crop.presenter.CropPresenter;
import com.redrocket.photoeditor.presentation.crop.presenter.CropPresenterImpl;
import com.redrocket.photoeditor.presentation.crop.view.custom.CropViewFacade;
import com.redrocket.photoeditor.presentation.effects.view.EffectActivity;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryActivity;
import com.redrocket.photoeditor.util.CropArea;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

/**
 * Реализация вью для экрана кропа.
 */
public class CropActivity
        extends
            AppCompatActivity
        implements
            CropScreenView,
            CropViewFacade.LoadListener,
            FileErrorDialog.OnDialogListener {

    private static final String TAG = "CropActivity";

    private static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    private static final String TAG_FILE_ERROR_DIALOG = "TAG_FILE_ERROR_DIALOG";

    private CropViewFacade mCropPicture;

    private CropPresenter mPresenter;

    /**
     * Создать интент для запуска экрана кропа.
     *
     * @param context   Контекст.
     * @param imagePath Путь к файлу с исходным изображением.
     * @return Возвращает интент.
     */
    public static Intent getCallingIntent(Context context, String imagePath) {
        Intent callingIntent = new Intent(context, CropActivity.class);
        callingIntent.putExtra(EXTRA_IMAGE_PATH, imagePath);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        mCropPicture = new CropViewFacade((CropImageView) findViewById(R.id.crop_picture),
                savedInstanceState, this);

        mPresenter = new CropPresenterImpl(PhotoEditorApplication.getProject());
        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        mPresenter.initialize(imagePath, false);
        mPresenter.bindView(this, savedInstanceState != null);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.close);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCropPicture.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                handleNextClick();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleNextClick() {
        try {
            mPresenter.onNextClick(getCrop());
        } catch (IOException e) {
            onFileError();
        }
    }

    @Override
    public void onFileError() {
        mPresenter.onFileError();
    }

    @Override
    public void onDismiss() {
        mPresenter.onCloseFileErrorMsg();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setImage(String path, CropArea crop) {
        mCropPicture.setImage(path);
        mCropPicture.setCropRect(new RectF(crop.left, crop.top, crop.right, crop.bottom));
    }

    @Override
    public void showBackControl() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showFileErrorMsg() {
        FileErrorDialog dialog = new FileErrorDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), TAG_FILE_ERROR_DIALOG);
    }

    @Override
    public void openEffectScreen() {
        Intent intent = new Intent(this, EffectActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_enter, R.anim.slide_out_enter);
    }

    @Override
    public void resetToGalleryScreen() {
        startActivity(GalleryActivity.getCallingIntent(this));
    }

    private CropArea getCrop() throws IOException {
        RectF rect = mCropPicture.getCropRect();
        return new CropArea(rect.left, rect.top, rect.right, rect.bottom);
    }
}
