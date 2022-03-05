package com.jedisebas.inteligentmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class provide sign in the app and anchor to the SignUpActivity.
 * @author JediSebas
 */

public class MainActivity extends AppCompatActivity {

    private static boolean loginOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText email, password, ip;
        TextView signup;
        Button login;

        email = findViewById(R.id.logemailEt);
        password = findViewById(R.id.logpasswdEt);
        ip = findViewById(R.id.logaddressEt);
        signup = findViewById(R.id.logsignupTv);
        login = findViewById(R.id.loginBtn);



        // anchor to SignUpActivity
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Log in button
        login.setOnClickListener(v -> {
            String emailS = email.getText().toString().trim();
            String passwordS = password.getText().toString().trim();
            String ipS = ip.getText().toString().trim();

            if (emailS.isEmpty() || passwordS.isEmpty() || ipS.isEmpty()) {
                Toast.makeText(getBaseContext(), "Not all data", Toast.LENGTH_LONG).show();
            } else {
                Loggeduser.ip = ipS;
                JDBCLogin jdbcLogin = new JDBCLogin(emailS, passwordS, ipS);
                jdbcLogin.t.start();
                try {
                    jdbcLogin.t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (loginOk){
                    Loggeduser.email = emailS;
                    Loggeduser.isLogged = true;
                    Intent intent;
                    intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    static void setLoginOk(boolean b) {
        loginOk = b;
    }
}