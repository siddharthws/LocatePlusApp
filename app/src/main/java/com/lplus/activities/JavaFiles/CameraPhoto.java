//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lplus.activities.JavaFiles;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPhoto {
    private String photoPath;
    private Context context;

    public String getPhotoPath() {
        return this.photoPath;
    }

    public CameraPhoto(Context context) {
        this.context = context;
    }

    public Intent takePhotoIntent() throws IOException {
        Intent in = new Intent("android.media.action.IMAGE_CAPTURE");
        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(in.resolveActivity(this.context.getPackageManager()) != null) {
            File photoFile = this.createImageFile();
            if(photoFile != null) {
                in.putExtra("output", FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", photoFile));
            }
        }

        return in;
    }

    private File createImageFile() throws IOException {
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        this.photoPath = image.getAbsolutePath();
        return image;
    }

    public void addToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(this.photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);
    }
}
