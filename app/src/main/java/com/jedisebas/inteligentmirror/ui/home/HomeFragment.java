package com.jedisebas.inteligentmirror.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jedisebas.inteligentmirror.ConnectionData;
import com.jedisebas.inteligentmirror.JDBCLogin;
import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class which welcome the user
 */

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Switch vmSwitch;
    private byte status = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView hello = root.findViewById(R.id.helloTv);
        vmSwitch = root.findViewById(R.id.vmSwitch);
        hello.setText("Hello " + Loggeduser.name);

        JDBCvm jdbcvm = new JDBCvm(1);
        jdbcvm.t.start();

        try {
            jdbcvm.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vmSwitch.setChecked(status == 1);

        vmSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            JDBCvm jdbcVm = new JDBCvm(0);
            if (b) {
                jdbcVm.setVal((byte) 1);
            } else {
                jdbcVm.setVal((byte) 0);
            }
            jdbcVm.t.start();
        });

        return root;
    }

    private class JDBCvm implements Runnable {

        Thread t;
        byte val;
        int mode;

        JDBCvm(int mode) {
            t = new Thread(this);
            this.mode = mode;
        }

        void setVal(byte val) {
            this.val = val;
        }

        @Override
        public void run() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("DRIVER STILL WORKS BTW");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (mode == 0) {
                updateQuery();
            } else if (mode == 1) {
                getQuery();
            }
        }

        private void updateQuery() {
            String QUERY = "UPDATE `user` SET `vm` = b'"+val+"' WHERE `user`.`id` = " + Loggeduser.id + ";";
            try {
                Connection conn = DriverManager.getConnection(ConnectionData.DB_URL, ConnectionData.USER, ConnectionData.PASS);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(QUERY);
                System.out.println("GOOOOOOD");
            } catch (SQLException throwables) {
                System.out.println("HERE IS SOMETHING WRONG");
                throwables.printStackTrace();
            }
        }

        private void getQuery() {
            String QUERY = "SELECT `vm` FROM `user` WHERE `id` = " + Loggeduser.id + ";";
            try {
                Connection conn = DriverManager.getConnection(ConnectionData.DB_URL, ConnectionData.USER, ConnectionData.PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY);
                if (rs.next()) {
                    status = rs.getByte("vm");
                }
                System.out.println("GOOOOOOD");
            } catch (SQLException throwables) {
                System.out.println("HERE IS SOMETHING WRONG");
                throwables.printStackTrace();
            }
        }
    }
}