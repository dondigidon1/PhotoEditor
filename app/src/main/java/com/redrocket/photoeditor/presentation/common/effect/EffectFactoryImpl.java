package com.redrocket.photoeditor.presentation.common.effect;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.redrocket.photoeditor.R;

import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;

/**
 * Фабрика эффектов.
 */
public class EffectFactoryImpl implements EffectFactory {

    private final Resources mResources;

    public EffectFactoryImpl(Context context) {
        mResources = context.getResources();
    }

    @NonNull
    public Effect getEffect(int id) {
        switch (id) {
            case 0:
                return new Effect(new GPUImageFilter(),
                        mResources.getString(R.string.effect_original_name_text));
            case 1:
                return new Effect(new GPUImageGrayscaleFilter(),
                        mResources.getString(R.string.effect_grayscale_name_text));
            case 2:
                return new Effect(new GPUImageColorInvertFilter(),
                        mResources.getString(R.string.effect_color_invert_name_text));
            case 3:
                return new Effect(new GPUImageSepiaFilter(),
                        mResources.getString(R.string.effect_sepia_name_text));
            case 4:
                return new Effect(new GPUImageHueFilter(),
                        mResources.getString(R.string.effect_hue_name_text));
            default:
                throw new IllegalArgumentException();
        }
    }
}
