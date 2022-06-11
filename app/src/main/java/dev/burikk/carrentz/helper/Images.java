package dev.burikk.carrentz.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class Images {
    public static Bitmap bitmap(
            Context context,
            Uri uri
    ) throws Exception {
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, null);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options,
            int width,
            int height
    ) {
        final int outHeight = options.outHeight;
        final int outWidth = options.outWidth;

        int inSampleSize = 1;

        if (outHeight > height || outWidth > width) {
            final int halfHeight = outHeight / 2;
            final int halfWidth = outWidth / 2;

            while ((halfHeight / inSampleSize) >= height && (halfWidth / inSampleSize) >= width) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}