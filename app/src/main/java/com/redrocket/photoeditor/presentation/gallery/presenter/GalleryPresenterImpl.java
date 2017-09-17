package com.redrocket.photoeditor.presentation.gallery.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.redrocket.photoeditor.presentation.gallery.structures.PreviewDescriptor;
import com.redrocket.photoeditor.presentation.gallery.view.GalleryScreenView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Реализация презентера для экрана галереи.
 */
public class GalleryPresenterImpl implements GalleryPresenter {
    private static final String TAG = "GalleryPresenterImpl";

    private GalleryScreenView mView;
    private Context mContext;

    @Override
    public void bindView(GalleryScreenView view, boolean isRestore) {
        mView = view;
        mContext = view.getContext();

        updateImages();
    }

    @Override
    public void destroy() {}

    @Override
    public void onSelectImage(final String image) {
        openCrop(image);
    }

    private void openCrop(final String image) {
        mView.openCropScreen(image);
    }

    private void updateImages() {
        mView.setImages(getImages());
    }

    private List<PreviewDescriptor> getImages() {
        final Map<Integer, String> imageIdToThumbPath = new HashMap<>();

        final String[] thumbProjection = {MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Thumbnails.IMAGE_ID};
        final Cursor thumbCursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(
                mContext.getContentResolver(), MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Thumbnails.MINI_KIND,
                thumbProjection);

        if (thumbCursor != null) {
            while (thumbCursor.moveToNext()) {
                final int imageId = thumbCursor.getInt(
                        thumbCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
                final String thumbPath = thumbCursor.getString(
                        thumbCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                imageIdToThumbPath.put(imageId, thumbPath);
            }

            thumbCursor.close();
        }

        final String[] imageProjection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final String imageSelection = MediaStore.Images.Media.MIME_TYPE + " = ?";
        final String[] imageSelectionArgs = new String[]{"image/jpeg"};

        final Cursor imageCursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection, imageSelection, imageSelectionArgs,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        // Важен порядок
        final Map<Integer, String> imageIdToImagePath = new LinkedHashMap<>();
        if (imageCursor != null) {
            while (imageCursor.moveToNext()) {
                final int imageId = imageCursor.getInt(
                        imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                final String imagePath = imageCursor.getString(
                        imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                imageIdToImagePath.put(imageId, imagePath);
            }

            imageCursor.close();
        }

        final List<PreviewDescriptor> imageDescriptors = new ArrayList<>();

        final Set<Integer> imageIds = imageIdToImagePath.keySet();
        for (int id : imageIds) {
            final String imagePath = imageIdToImagePath.get(id);
            final String thumbPath = imageIdToThumbPath.containsKey(id)
                    ? imageIdToThumbPath.get(id) : null;

            imageDescriptors.add(new PreviewDescriptor(imagePath, thumbPath));
        }

        return imageDescriptors;
    }
}
