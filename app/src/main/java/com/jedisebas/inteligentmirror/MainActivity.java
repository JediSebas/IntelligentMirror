package com.jedisebas.inteligentmirror;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class provide sign in the app and anchor to the SignUpActivity.
 * @author JediSebas
 */

public class MainActivity extends AppCompatActivity {

    // temporary initialized
    private static boolean loginOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText name, lastname, password, ip;
        TextView signup;
        Button login;

        name = findViewById(R.id.lognameEt);
        lastname = findViewById(R.id.loglastnameEt);
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
            String nameS = name.getText().toString().trim();
            String lastnameS = lastname.getText().toString().trim();
            String passwordS = password.getText().toString().trim();
            String ipS = ip.getText().toString().trim();

            if (nameS.isEmpty() || lastnameS.isEmpty() || passwordS.isEmpty() || ipS.isEmpty()) {
                Toast.makeText(getBaseContext(), "Not all data", Toast.LENGTH_LONG).show();
            } else {
                String nick = Nickname.changeProfileName((nameS + " " + lastnameS).toLowerCase());
                JDBCLogin jdbcLogin = new JDBCLogin(nick, passwordS, ipS);
                jdbcLogin.t.start();
                try {
                    jdbcLogin.t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (loginOk){
                    Intent intent;
                    intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    static void setLoginOk(boolean b) {
        loginOk = b;
    }
}