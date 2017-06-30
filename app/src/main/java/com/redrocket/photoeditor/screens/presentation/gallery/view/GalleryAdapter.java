package com.redrocket.photoeditor.screens.presentation.gallery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.redrocket.photoeditor.R;
import com.redrocket.photoeditor.screens.presentation.gallery.structures.PreviewDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @TODO Class Description ...
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PreviewHolder> {
    private static final String TAG = "GalleryAdapter";
    private static final int PRELOAD_COUNT = 9;

    private final Context mContext;

    private List<PreviewDescriptor> mItems = new ArrayList<>();
    private Map<String,Integer> mThumbPathToRotation = new HashMap<>();

    GalleryAdapter(Context context) {
        mContext = context;
    }

    void setItems(final List<PreviewDescriptor> items) {
        mItems = items;
        for (PreviewDescriptor item : items) {

            if (item.thumbPath == null)
                continue;

            final int imageRotation = getRotation(item.imagePath);
            final int thumbRotation = getRotation(item.thumbPath);

            final int previewRotation = imageRotation - thumbRotation;
            mThumbPathToRotation.put(item.thumbPath,previewRotation);
        }
        notifyDataSetChanged();
    }

    @Override
    public PreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext).
                inflate(R.layout.gallery_item_preview, parent, false);

        return new PreviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PreviewHolder holder, int position) {
        createRequest(position)
                .placeholder(R.color.steelGray)
                .priority(Priority.HIGH)
                .into(holder.preview);

//        final int preloadPos = position + PRELOAD_COUNT;
//        if (preloadPos >= 0 && preloadPos < mItems.size()
//                && holder.preview.getWidth() > 0
//                && holder.preview.getHeight() > 0) {
//            createRequest(preloadPos)
//                    .priority(Priority.LOW)
//                    .preload(holder.preview.getWidth(), holder.preview.getHeight());
//        }
    }

    private BitmapRequestBuilder<String, Bitmap> createRequest(final int position) {
        final String previewPath = mItems.get(position).thumbPath != null ?
                mItems.get(position).thumbPath :
                mItems.get(position).imagePath;

        BitmapRequestBuilder<String,Bitmap> builder = Glide
                .with(mContext)
                .load(previewPath)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT);

        final List<BitmapTransformation> transforms = new ArrayList<>();
        transforms.add(new CenterCrop(mContext));

        if (mThumbPathToRotation.containsKey(previewPath)) {
            final int diffRotation = mThumbPathToRotation.get(previewPath);
            transforms.add(new BitmapTransformation(mContext) {
                @Override
                protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                           int outWidth, int outHeight) {
                    final Matrix matrix = new Matrix();
                    matrix.postRotate(diffRotation);

                    final Bitmap rotateBitmap = Bitmap.createBitmap(toTransform, 0, 0,
                            toTransform.getWidth(), toTransform.getHeight(), matrix, true);

                    return rotateBitmap;
                }

                @Override
                public String getId() {
                    return "rotation";
                }
            });
        }

        builder = builder
                    .transform(transforms.toArray(new BitmapTransformation[transforms.size()]))
                    .crossFade();

        return builder;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void handlePreviewClick(final int pos){
        Log.i(TAG, "handlePreviewClick: "+pos);
    }

    class PreviewHolder extends RecyclerView.ViewHolder {

        private ImageView preview;

        public PreviewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.button_preview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePreviewClick(getAdapterPosition());
                }
            });

            preview = (ImageView) itemView.findViewById(R.id.image_preview);
        }
    }

    private static int getRotation(final String path) {
        final ExifInterface exif;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }
}
