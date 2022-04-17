package com.jedisebas.inteligentmirror.ui.account;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jedisebas.inteligentmirror.ConnectionData;
import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.MyFTPListener;
import com.jedisebas.inteligentmirror.PathUtil;
import com.jedisebas.inteligentmirror.R;
import com.jedisebas.inteligentmirror.ui.event.EventFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class AccountActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    private static final int IMAGE_PICK_CODE = 1000;

    private ImageView iv1, iv2, iv3, iv4;
    private ImageView[] imageTable = new ImageView[4];
    private Button pickImage, sendImage, saveIgSp;
    private EditText loginEt, passwordEt;
    private List<Uri> uris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        imageTable[0] = iv1 = findViewById(R.id.accountImage1);
        imageTable[1] = iv2 = findViewById(R.id.accountImage2);
        imageTable[2] = iv3 = findViewById(R.id.accountImage3);
        imageTable[3] = iv4 = findViewById(R.id.accountImage4);
        pickImage = findViewById(R.id.pickImageBtn);
        sendImage = findViewById(R.id.sendImageBtn);
        saveIgSp = findViewById(R.id.igspSave);
        loginEt = findViewById(R.id.accountEmailEt);
        passwordEt = findViewById(R.id.accountPasswordEt);

        sendImage.setVisibility(View.GONE);

        pickImage.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(
                    AccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                if (ActivityCompat.checkSelfPermission(
                        AccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        });

        sendImage.setOnClickListener(view -> {
            FTPSendImages ftpSendImages = new FTPSendImages();
            ftpSendImages.t.start();
            Toast.makeText(this, "Sending images...", Toast.LENGTH_SHORT).show();
        });

        saveIgSp.setOnClickListener(view -> {
            String login = loginEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();
            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Not all data", Toast.LENGTH_SHORT).show();
            } else {
                JDBCSaveIgSp jdbcSaveIgSp = new JDBCSaveIgSp(login, password);
                jdbcSaveIgSp.t.start();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Account");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void pickImageFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {

            List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();
            uris.clear();

            if (clipData != null) {
                //multiple images selected
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    uris.add(imageUri);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //single image selected
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            new Thread(() -> {
                for (int i=0; i<4; i++) {
                    int finalI = i;
                    runOnUiThread(() -> {
                        if (finalI < (bitmaps).size()) {
                            imageTable[finalI].setImageBitmap(bitmaps.get(finalI));
                        }
                    });
                }
            }).start();

            if (uris.size() < 4) {
                Toast.makeText(this, "You need choose 4 images!", Toast.LENGTH_SHORT).show();
            } else {
                sendImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private class FTPSendImages implements Runnable {

        Thread t;

        FTPSendImages() {
            t = new Thread(this);
        }

        @Override
        public void run() {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(Loggeduser.ip, ConnectionData.PORT);
                ftpClient.login(ConnectionData.USER, ConnectionData.PASS);

                ftpClient.changeDirectory(ConnectionData.DIRECTORY_IN_MIRROR);
                ftpClient.setType(FTPClient.TYPE_BINARY);
                ftpClient.setPassive(true);
                ftpClient.noop();

                for (int i=0; i<4; i++) {
                    String pathFile = PathUtil.getPath(getBaseContext(), uris.get(i));
                    File file = new File(pathFile);
                    ftpClient.upload(file, new MyFTPListener());
                    String newFileName = ConnectionData.DIRECTORY_IN_MIRROR + Loggeduser.name + "_" +
                            Loggeduser.lastname + "_" + Loggeduser.id + "_" + i + ".jpg";
                    ftpClient.rename(file.getName(), newFileName);
                }
            } catch (FTPIllegalReplyException | IOException | FTPException | FTPAbortedException | FTPDataTransferException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect(true);
                    }
                } catch (IOException | FTPIllegalReplyException | FTPException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class JDBCSaveIgSp implements Runnable {

        Thread t;
        String login, password;

        JDBCSaveIgSp(String login, String password) {
            t = new Thread(this);
            this.login = login;
            this.password = password;
        }

        @Override
        public void run() {
            String QUERY = "UPDATE `user` SET `instagram_login` = '" + login + "', `instagram_password` = '" + password + "' WHERE `user`.`id` = " + Loggeduser.id + ";";
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