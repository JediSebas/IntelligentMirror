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

    public static boolean isNickTaken = false, isEmailTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText name, lastname, password, confpassword, email, emailPassword, confEmailPassword, nick, ip;
        Button chose, signup;

        name = findViewById(R.id.signnameEt);
        lastname = findViewById(R.id.signlastnameEt);
        password = findViewById(R.id.signpasswdEt);
        confpassword = findViewById(R.id.signconfEt);
        email = findViewById(R.id.signemailEt);
        emailPassword = findViewById(R.id.signemailpasswdEt);
        confEmailPassword = findViewById(R.id.signemailpasswdconfEt);
        nick = findViewById(R.id.signnickEt);
      //  ip = findViewById(R.id.signaddressEt);
        signup = findViewById(R.id.signupBtn);

        // Chose picture Button
//        chose.setOnClickListener(v -> {
//            if (ActivityCompat.checkSelfPermission(
//                    SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
//                if (ActivityCompat.checkSelfPermission(
//                        SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    pickImageFromGallery();
//                }
//            } else {
//                pickImageFromGallery();
//            }
//        });

        // Signup Button
        signup.setOnClickListener(v -> {
            String nameS = name.getText().toString().trim();
            String lastnameS = lastname.getText().toString().trim();
            String passwordS = password.getText().toString().trim();
            String confpasswordS = confpassword.getText().toString().trim();
            String emailS = email.getText().toString().trim();
            String emailPasswdS = emailPassword.getText().toString().trim();
            String confEmailPasswdS = confEmailPassword.getText().toString().trim();
            String nickS = nick.getText().toString().trim();
         //   String ipS = ip.getText().toString().trim();
        //    Loggeduser.ip = ipS;

            if (nameS.isEmpty() || lastnameS.isEmpty() || passwordS.isEmpty()
                    || confpasswordS.isEmpty() || emailS.isEmpty() || emailPasswdS.isEmpty()
                    || confEmailPasswdS.isEmpty() /*|| ipS.isEmpty() */) {
                Toast.makeText(getBaseContext(), "Not all data", Toast.LENGTH_LONG).show();
            } else {
                if (passwordS.equals(confpasswordS) && emailPasswdS.equals(confEmailPasswdS)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JDBCSignup jdbcSignup = new JDBCSignup(nameS, lastnameS, passwordS, emailS, emailPasswdS, nickS);
                            jdbcSignup.t.start();
                            try {
                                jdbcSignup.t.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isEmailTaken || isNickTaken) {
                                        if (isEmailTaken && isNickTaken) {
                                            Toast.makeText(getBaseContext(), "Email and nick are taken", Toast.LENGTH_SHORT).show();
                                        } else if (isEmailTaken) {
                                            Toast.makeText(getBaseContext(), "Email is taken", Toast.LENGTH_SHORT).show();
                                        } else if (isNickTaken) {
                                            Toast.makeText(getBaseContext(), "Nick is taken", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }).start();

                    Toast.makeText(getBaseContext(), "Creating account", Toast.LENGTH_SHORT).show();

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