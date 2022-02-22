package com.jedisebas.inteligentmirror.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.jedisebas.inteligentmirror.R;

import java.io.File;
import java.io.FileOutputStream;

public class DownloadActivity extends AppCompatActivity {

    static Bitmap bitmap;

    private Button downloadBtn, deleteBtn, downloadAndDelBtn;
    private ImageView imageView;

    private static final int PICK_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        downloadBtn = findViewById(R.id.downloadBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        downloadAndDelBtn = findViewById(R.id.downloadAndDeleteBtn);
        imageView = findViewById(R.id.gridImage);
        imageView.setImageBitmap(bitmap);

        downloadBtn.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DownloadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                download();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            delete();
        });

        downloadAndDelBtn.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DownloadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                download();
                delete();
            }
        });
    }

    private void download() {
        String directory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Mirror";
        File myDir = new File(directory);
        myDir.mkdirs();

        String fname = "Image.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MediaScannerConnection.scanFile(getBaseContext(),
                    new String[]{directory}, null,
                    (path, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    });
        }
    }

    private void delete() {
        //TODO delete
    }
}