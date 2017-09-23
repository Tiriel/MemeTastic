package io.github.gsantner.memetastic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.gsantner.memetastic.App;
import io.github.gsantner.memetastic.R;
import io.github.gsantner.memetastic.data.MemeLibConfig;

public class ContextUtils extends net.gsantner.opoc.util.ContextUtils {
    public ContextUtils(Context context) {
        super(context);
    }


    public static ContextUtils get() {
        return new ContextUtils(App.get());
    }


    public Bitmap scaleBitmap(Bitmap bitmap) {
        return scaleBitmap(bitmap, 300);
    }

    public Bitmap loadImageFromFilesystem(String imagePath) {
        return loadImageFromFilesystem(imagePath, MemeLibConfig.MEME_FULLSCREEN_MAX_IMAGESIZE);
    }

    public int getImmersiveUiVisibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int statusBarFlag = View.SYSTEM_UI_FLAG_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBarFlag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            return statusBarFlag
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        return 0;
    }

    public void enableImmersiveMode(final View decorViewOfActivity) {
        decorViewOfActivity.setSystemUiVisibility(getImmersiveUiVisibility());
        decorViewOfActivity.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorViewOfActivity.setSystemUiVisibility(getImmersiveUiVisibility());
                }
            }
        });
    }

    /**
     * Calculates the scaling factor to convert data size to size in pixels
     *
     * @param w width of the bitmap where a text should be written on
     * @param h height of the bitmap where a text should be written on
     * @return the size of the data in pixels
     */
    public float getScalingFactorInPixelsForWritingOnPicture(int w, int h) {
        final float fontScaler = (float) 133;
        final int raster = 50;
        int size = Math.min(w, h);
        int rest = size % raster;

        // Round
        int addl = rest >= raster / 2 ? raster - rest : -rest;

        return (size + addl) / (fontScaler);
    }

    public File getPicturesMemetasticFolder() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), _context.getString(R.string.app_name));
    }

    public File getPicturesMemetasticTemplatesCustomFolder() {
        return new File(new File(getPicturesMemetasticFolder(), "templates"), "custom");
    }
}
