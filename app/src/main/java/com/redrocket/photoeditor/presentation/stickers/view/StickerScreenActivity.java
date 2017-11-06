package com.redrocket.photoeditor.presentation.stickers.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.redrocket.photoeditor.PhotoEditorApplication;
import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.business.structures.Sticker;
import com.redrocket.photoeditor.presentation.common.dialogs.FileErrorDialog;
import com.redrocket.photoeditor.presentation.common.picture.PictureBuilder;
import com.redrocket.photoeditor.presentation.common.picture.PictureLoader;
import com.redrocket.photoeditor.presentation.common.sticker.StickerCategory;
import com.redrocket.photoeditor.presentation.common.sticker.StickerInfo;
import com.redrocket.photoeditor.presentation.common.sticker.StickersTable;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryActivity;
import com.redrocket.photoeditor.presentation.save.view.SaveActivity;
import com.redrocket.photoeditor.presentation.stickers.presenter.StickerScreenPresenter;
import com.redrocket.photoeditor.presentation.stickers.presenter.StickerScreenPresenterImpl;
import com.redrocket.photoeditor.presentation.stickers.view.pane.CategoryTabs;
import com.redrocket.photoeditor.presentation.stickers.view.pane.StickersAdapter;
import com.redrocket.photoeditor.presentation.stickers.view.stickerview.StickerBoard;
import com.redrocket.photoeditor.presentation.stickers.view.stickerview.StickerState;
import com.redrocket.photoeditor.util.CropArea;

import java.util.ArrayList;
import java.util.List;

public class StickerScreenActivity
        extends
            AppCompatActivity
        implements
            StickerScreenView,
            StickersAdapter.OnStickerClickListener,
            PictureLoader.ResultHandler,
            FileErrorDialog.OnDialogListener,
            CategoryTabs.OnTabClickListener {

    private static final String TAG = "StickerScreenActivity";

    private static final String BUNDLE_IMAGE_PATH = "BUNDLE_IMAGE_PATH";
    private static final String BUNDLE_CROP_AREA = "BUNDLE_CROP_AREA";
    private static final String BUNDLE_APPLIED_EFFECT_ID = "BUNDLE_APPLIED_EFFECT_ID";

    private static final String TAG_FILE_ERROR_DIALOG = "TAG_FILE_ERROR_DIALOG";

    private ImageView mPictureImage;
    private RecyclerView mPreviews;
    private StickersAdapter mAdapter;
    private StickerBoard mStickerBoard;
    private CategoryTabs mTabs;

    private final StickersTable mStickersTable = new StickersTable();

    private StickerScreenPresenter mPresenter;

    private String mImagePath;
    private CropArea mCropArea;
    private int mAppliedEffectId;

    /**
     * Создать вызывающий интент.
     *
     * @param context Контекст.
     * @return Возращает вызываюший интент.
     */
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, StickerScreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPictureImage = (ImageView) findViewById(R.id.image_picture);
        mTabs = (CategoryTabs) findViewById(R.id.tabs);
        mTabs.setListener(this);

        mStickerBoard = (StickerBoard) findViewById(R.id.sticker_board);

        mPreviews = (RecyclerView) findViewById(R.id.rv_sticker_previews);
        mPreviews.setHasFixedSize(true);
        mPreviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                handleStickerSheetScroll();
            }
        });
        int spanCount = getResources().getInteger(R.integer.sticker_span_count);
        mPreviews.setLayoutManager(new GridLayoutManager(this, spanCount,
                LinearLayoutManager.VERTICAL, false));

        showStickerCollection();

        if (savedInstanceState != null) {
            mImagePath = savedInstanceState.getString(BUNDLE_IMAGE_PATH);
            mCropArea = (CropArea) savedInstanceState.getSerializable(BUNDLE_CROP_AREA);
            mAppliedEffectId = savedInstanceState.getInt(BUNDLE_APPLIED_EFFECT_ID);

            FileErrorDialog dialog = (FileErrorDialog) getSupportFragmentManager()
                    .findFragmentByTag(TAG_FILE_ERROR_DIALOG);
            // TODO if maybe must be in presenter or something
            if (dialog != null) {
                dialog.setListener(this);
            } else {
                setImage(mImagePath, mCropArea, mAppliedEffectId);
            }
        }

        mPresenter = new StickerScreenPresenterImpl(PhotoEditorApplication.getProject());
        mPresenter.bindView(this, savedInstanceState != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sticker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                handleSaveClick();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_IMAGE_PATH, mImagePath);
        outState.putSerializable(BUNDLE_CROP_AREA, mCropArea);
        outState.putInt(BUNDLE_APPLIED_EFFECT_ID, mAppliedEffectId);
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

    @Override
    public void onPictureReady(Bitmap picture) {
        mPictureImage.setImageBitmap(picture);
        mPictureImage.animate().alpha(1).setDuration(getResources().
                getInteger(R.integer.sticker_screen_picture_fade_anim_duration));
        mStickerBoard.animate().alpha(1).setDuration(getResources().
                getInteger(R.integer.sticker_screen_picture_fade_anim_duration));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPictureImage.getLayoutParams();
        params.dimensionRatio = picture.getWidth() + ":" + picture.getHeight();
        mPictureImage.setLayoutParams(params);
    }

    @Override
    public void onError() {
        mPresenter.onFileError();
    }

    @Override
    public void onTabClick(int index) {
        int pos = mAdapter.getCategoryFirstItemPos(index);
        ((GridLayoutManager)mPreviews.getLayoutManager()).scrollToPositionWithOffset(pos,0);
    }

    @Override
    public void onStickerClick(StickerInfo sticker) {
        mPresenter.onPickSticker(sticker.id);
    }

    @Override
    public void onCreditClick(String link) {
        mPresenter.onLinkClick(link);
    }

    @Override
    public void onDismiss() {
        mPresenter.onCloseFileErrorMsg();
    }

    @Override
    public void setImage(String path, CropArea crop, int effect) {
        mImagePath = path;
        mCropArea = crop;
        mAppliedEffectId = effect;

        PictureBuilder builder = PhotoEditorApplication.getPictureFactory()
                .getBuilder()
                .image(path)
                .crop(crop)
                .effect(effect);

        PictureLoader loader = new PictureLoader(builder);
        loader.load(this);
    }

    @Override
    public void addNewSticker(int id) {
        StickerInfo sticker = mStickersTable.getStickerById(id);
        mStickerBoard.addNewSticker(sticker.bigImage, sticker.id);
    }

    @Override
    public void showFileErrorMsg() {
        FileErrorDialog dialog = new FileErrorDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), TAG_FILE_ERROR_DIALOG);
    }

    @Override
    public void openSaveScreen() {
        Intent intent = SaveActivity.getCallingIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_enter, R.anim.slide_out_enter);
    }

    @Override
    public void openLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.sticker_link_open_chooser_title)));
        }
    }

    @Override
    public void resetToGalleryScreen() {
        startActivity(GalleryActivity.getCallingIntent(this));
    }

    private void handleStickerSheetScroll() {
        int visibleItemPos = ((GridLayoutManager) mPreviews.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();

        int categoryIndex = mAdapter.getCategoryIndexByItemPos(visibleItemPos);
        mTabs.setSelected(categoryIndex);
    }

    //TODO рефактор
    private void showStickerCollection() {
        List<StickerCategory> categories = mStickersTable.getCategories();

        mAdapter = new StickersAdapter(categories, this);
        final GridLayoutManager layoutManager = (GridLayoutManager) mPreviews.getLayoutManager();
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                if (viewType == StickersAdapter.STICKER_VIEW_TYPE) {
                    return 1;
                } else if (viewType == StickersAdapter.CREDIT_VIEW_TYPE) {
                    return layoutManager.getSpanCount();
                } else {
                    throw new IllegalStateException();
                }
            }
        });

        mPreviews.setAdapter(mAdapter);

        List<Integer> icons = new ArrayList<>();
        for(StickerCategory category : categories){
            icons.add(category.getIcon());
        }

        mTabs.setTabs(icons);
    }

    private void handleSaveClick() {
        StickerState[] stickerStates = mStickerBoard.getStickers();
        List<Sticker> stickers = new ArrayList<>(stickerStates.length);

        for (StickerState stickerState : stickerStates) {
            int id = stickerState.id;
            Pair<Float, Float> dims = stickerState.dims;
            Pair<Float, Float> center = stickerState.center;
            float rotation = stickerState.rotation;
            stickers.add(new Sticker(id, center, dims, rotation));
        }

        mPresenter.onConfirmStickers(stickers);
    }
}
