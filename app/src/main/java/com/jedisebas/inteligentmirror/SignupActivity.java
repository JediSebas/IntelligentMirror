package com.jedisebas.inteligentmirror;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Class let create a new account.
 */

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    Uri imgUri;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText name, lastname, password, confpassword, email, ip;
        Button chose, signup;

        name = findViewById(R.id.signnameEt);
        lastname = findViewById(R.id.signlastnameEt);
        password = findViewById(R.id.signpasswdEt);
        confpassword = findViewById(R.id.signconfEt);
        email = findViewById(R.id.signemailEt);
        ip = findViewById(R.id.signaddressEt);
        chose = findViewById(R.id.signpicBtn);
        signup = findViewById(R.id.signupBtn);
        img = findViewById(R.id.signImg);

        // Chose picture Button
        chose.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(
                    SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                if (ActivityCompat.checkSelfPermission(
                        SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        });

        // Signup Button
        signup.setOnClickListener(v -> {
            String nameS = name.getText().toString().trim();
            String lastnameS = lastname.getText().toString().trim();
            String passwordS = password.getText().toString().trim();
            String confpasswordS = confpassword.getText().toString().trim();
            String emailS = email.getText().toString().trim();
            String ipS = ip.getText().toString().trim();

            if (nameS.isEmpty() || lastnameS.isEmpty() || passwordS.isEmpty()
                    || confpasswordS.isEmpty() || emailS.isEmpty() || ipS.isEmpty()) {
                Toast.makeText(getBaseContext(), "Not all data", Toast.LENGTH_LONG).show();
            } else {
                if (passwordS.equals(confpasswordS)) {
                    String nick = Nickname.changeProfileName((name + " " + lastname).toLowerCase());
                    JDBCSignup jdbcSignup = new JDBCSignup(nick, nameS, lastnameS, passwordS, emailS, ipS);
                    jdbcSignup.t.start();
                    Toast.makeText(getBaseContext(), "Creating account", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                    confpassword.setHighlightColor(Color.RED);
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgUri = data.getData();
        String filePath= PathUtil.getPath(getBaseContext(), imgUri);
        System.out.println(filePath);
        img.setImageURI(data.getData());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}