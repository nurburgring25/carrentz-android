package dev.burikk.carrentz.helper;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Retrofits {
    public static MultipartBody.Part filePart(
            String partName,
            Bitmap bitmap,
            String fileName
    ) throws Exception {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("jpeg"),
                    byteArrayOutputStream.toByteArray()
            );

            return MultipartBody.Part.createFormData(partName, fileName, requestBody);
        }
    }
}