package com.burhanrashid52.photoediting;

import static com.burhanrashid52.photoediting.activitys.EditImageActivity.FILE_PROVIDER_AUTHORITY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ImageShareHelper {
    private static ImageShareHelper INSTANCE;


    public static ImageShareHelper getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new ImageShareHelper(context);
        }

        return INSTANCE;
    }
    Context context;
    private ImageShareHelper(Context context) {
        this.context = context;
    }

    public void shareImage(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.app_name));

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // setting type to image
        intent.setType("image/*");

        // calling startactivity() to share
       context. startActivity(Intent.createChooser(intent, "Share Via"));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file);
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

}
