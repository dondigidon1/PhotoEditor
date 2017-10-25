package com.redrocket.photoeditor.presentation.common.effect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.redrocket.photoeditor.R;

import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

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
            case 0: {
                return new Effect(new GPUImageFilter(),
                        mResources.getString(R.string.effect_original_name_text));
            }
            case 1: {
                return new Effect(new GPUImageGrayscaleFilter(),
                        mResources.getString(R.string.effect_hammer_name_text));
            }
            case 2: {
                GPUImageGammaFilter filter = new GPUImageGammaFilter();
                filter.setGamma(3f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_logan_name_text));
            }

            case 3: {
                return new Effect(new GPUImageSepiaFilter(),
                        mResources.getString(R.string.effect_sunrise_name_text));
            }
            case 4: {
                GPUImageGammaFilter filter = new GPUImageGammaFilter();
                filter.setGamma(0.6f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_rock_name_text));
            }
            case 5: {
                GPUImageContrastFilter filter = new GPUImageContrastFilter();
                filter.setContrast(2f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_midnight_name_text));
            }
            case 6: {
                GPUImageBrightnessFilter filter = new GPUImageBrightnessFilter();
                filter.setBrightness(-0.2f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_indigo_name_text));
            }
            case 7: {
                GPUImageSepiaFilter filter = new GPUImageSepiaFilter();
                filter.setIntensity(1.4f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_amber_name_text));
            }
            case 8: {
                GPUImageMonochromeFilter filter = new GPUImageMonochromeFilter();
                filter.setIntensity(0.95f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_gold_name_text));
            }
            case 9: {
                GPUImageRGBFilter filter = new GPUImageRGBFilter();
                filter.setRed(0.5f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_spirit_name_text));
            }
            case 10: {
                GPUImageRGBFilter filter = new GPUImageRGBFilter();
                filter.setGreen(0.5f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_jamal_name_text));
            }
            case 11: {
                GPUImageRGBFilter filter = new GPUImageRGBFilter();
                filter.setBlue(0.5f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_sky_invert_name_text));
            }
            case 12: {
                GPUImageLookupFilter filter = new GPUImageLookupFilter();
                filter.setBitmap(BitmapFactory.decodeResource(mResources, R.drawable.lookup_amatorka));

                return new Effect(filter,
                        mResources.getString(R.string.effect_art_name_text));
            }
            case 13: {
                GPUImageWhiteBalanceFilter filter = new GPUImageWhiteBalanceFilter();
                filter.setTemperature(4400f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_glory_name_text));
            }
            case 14: {
                GPUImageWhiteBalanceFilter filter = new GPUImageWhiteBalanceFilter();
                filter.setTemperature(5600f);
                return new Effect(filter,
                        mResources.getString(R.string.effect_blues_name_text));
            }

            default:
                throw new IllegalArgumentException();
        }
    }
}
