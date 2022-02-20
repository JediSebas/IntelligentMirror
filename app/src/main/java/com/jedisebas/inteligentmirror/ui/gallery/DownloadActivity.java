package com.jedisebas.inteligentmirror.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Button downloadBtn = findViewById(R.id.downloadBtn);
        ImageView imageView = findViewById(R.id.gridImage);
        imageView.setImageBitmap(bitmap);

        downloadBtn.setOnClickListener(v -> {
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
        });
    }
}