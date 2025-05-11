// app/src/main/java/com/example/testresultapp/util/PhotoManager.java
package com.example.testresultapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoManager {
    private final Context context;
    private String currentPhotoPath;

    public PhotoManager(Context context) {
        this.context = context;
    }

    public Intent createTakePhotoIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Vytvoření souboru pro uložení fotografie
        File photoFile = createImageFile();

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(
                    context,
                    "com.example.testresultapp.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            return takePictureIntent;
        }

        return null;
    }

    private File createImageFile() throws IOException {
        // Vytvoření jedinečného jména souboru
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Uložení cesty k souboru
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }
}