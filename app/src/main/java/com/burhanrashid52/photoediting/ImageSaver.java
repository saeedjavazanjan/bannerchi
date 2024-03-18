package com.burhanrashid52.photoediting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    private static ImageSaver INSTANCE;
    private ImageSaver() {
    }

    public static ImageSaver getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ImageSaver();
        }

        return INSTANCE;
    }

    public void saveToGallery(ImageView imageView, Context context) throws IOException {
        imageView.buildDrawingCache();

        Bitmap bmp = imageView.getDrawingCache();
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String filename="bannerchi"+System.currentTimeMillis();
        File file = new File(storageLoc, filename + ".jpg");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            scanFile(context, Uri.fromFile(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }



}
