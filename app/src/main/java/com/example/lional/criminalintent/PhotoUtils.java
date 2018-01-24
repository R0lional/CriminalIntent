package com.example.lional.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;

/**
 * Created by lional on 2018/1/11.
 */

public class PhotoUtils {
    private static final String TAG = "PhotoUtils";

    public static BitmapDrawable getScaledDrawable(Activity activity, String path) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float disWidth = display.getWidth();
        float disHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        Log.d(TAG, "disWidth: " + disWidth + ", disHeight: " + disHeight);
        Log.d(TAG, "srcWidth: " + srcWidth + ", srcHeight: " + srcHeight);
        if (srcWidth > disWidth || srcHeight > disHeight) {
            if (srcWidth > disWidth) {
                inSampleSize = Math.round(srcWidth / disWidth);
            } else {
                inSampleSize = Math.round(srcHeight / disHeight);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(activity.getResources(), bitmap);
    }
}
