package com.jedisebas.inteligentmirror;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class is used to log in account.
 */

public class JDBCLogin implements Runnable {

    public Thread t;
    private String email, password, ip;

    public JDBCLogin(String email, String password, String ip) {
        t = new Thread(this);
        this.email = email;
        this.password = password;
        this.ip = ip;
    }

    @Override
    public void run() {
        String DB_URL = "jdbc:mysql://"+ip+"/mirror";
        String USER = "user";
        String PASS = "user"; // test password
        //TODO password hash
        String QUERY = "SELECT id, name, lastname, password FROM user WHERE email=\""+email+"\"";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("DRIVER STILL WORKS BTW");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            rs.next();
            int n1 = rs.getInt("id");
            String n2 = rs.getString("name");
            String n3 = rs.getString("lastname");
            String n4 = rs.getString("password");

            Loggeduser.id = n1;
            Loggeduser.name = n2;
            Loggeduser.lastname = n3;
            Loggeduser.password = n4;

            MainActivity.setLoginOk(n4.equals(password));
        } catch (SQLException throwables) {
            System.out.println("HERE IS SOMETHING WRONG");
            throwables.printStackTrace();
        }
    }
}
