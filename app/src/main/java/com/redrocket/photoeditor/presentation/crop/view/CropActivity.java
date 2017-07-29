package com.redrocket.photoeditor.presentation.crop.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.presentation.crop.presenter.CropPresenter;
import com.redrocket.photoeditor.presentation.crop.presenter.CropPresenterImpl;
import com.redrocket.photoeditor.util.CropArea;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

/**
 * Реализация вью для экрана кропа.
 */
public class CropActivity extends AppCompatActivity implements CropView {
    private static final String TAG = "CropActivity";

    private static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    private CropImageView mCropPicture;

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

        mCropPicture = (CropImageView) findViewById(R.id.crop_picture);

        mPresenter = new CropPresenterImpl(PhotoEditorApplication.getProject());
        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        mPresenter.initialize(imagePath);
        mPresenter.bindView(this, savedInstanceState != null);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.close);
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
                mPresenter.onNextClick(getCrop());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void openEffectScreen() {
    }

    @Override
    public void setImage(String path, final CropArea crop) {
        mCropPicture.setImageUriAsync(Uri.fromFile(new File(path)));
        mCropPicture.setOnSetImageUriCompleteListener(new CropImageView.OnSetImageUriCompleteListener() {
            @Override
            public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                setCrop(crop);
            }
        });
    }

    private void setCrop(CropArea crop) {
        Rect imageRect = mCropPicture.getWholeImageRect();

        int leftAbs = (int) (crop.left * imageRect.width());
        int topAbs = (int) (crop.top * imageRect.height());
        int rightAbs = (int) (crop.right * imageRect.width());
        int bottomAbs = (int) (crop.bottom * imageRect.height());
        Rect cropRect = new Rect(leftAbs, topAbs, rightAbs, bottomAbs);

        mCropPicture.setCropRect(cropRect);
    }

    private CropArea getCrop() {
        Rect imageRect = mCropPicture.getWholeImageRect();
        Rect cropRect = mCropPicture.getCropRect();

        float leftRel = (float) cropRect.left / imageRect.width();
        float topRel = (float) cropRect.top / imageRect.height();
        float rightRel = (float) cropRect.right / imageRect.width();
        float bottomRel = (float) cropRect.bottom / imageRect.height();

        return new CropArea(leftRel, topRel, rightRel, bottomRel);
    }
}
