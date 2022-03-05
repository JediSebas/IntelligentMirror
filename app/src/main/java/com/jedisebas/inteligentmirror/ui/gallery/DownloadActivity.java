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

import com.jedisebas.inteligentmirror.ConnectionData;
import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.R;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DownloadActivity extends AppCompatActivity {

    static Bitmap bitmap;
    static String fileName;

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

        deleteBtn.setOnClickListener(v -> delete());

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

        File file = new File (myDir, fileName);
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
        JDBCDeleteImage deleteImage = new JDBCDeleteImage();
        deleteImage.t.start();
    }

    private class JDBCDeleteImage implements Runnable {

        Thread t;

        JDBCDeleteImage() {
            t = new Thread(this);
        }

        @Override
        public void run() {
            //TODO password hash
            String QUERY = "DELETE FROM `pictures` WHERE `name` = \"" + fileName + "\"";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("DRIVER STILL WORKS BTW");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection conn = DriverManager.getConnection(ConnectionData.DB_URL, ConnectionData.USER, ConnectionData.PASS);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(QUERY);
            } catch (SQLException throwables) {
                System.out.println("HERE IS SOMETHING WRONG");
                throwables.printStackTrace();
            }
        }
    }
}