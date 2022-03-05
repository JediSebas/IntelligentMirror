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
    private String email, password;

    public JDBCLogin(String email, String password) {
        t = new Thread(this);
        this.email = email;
        this.password = password;
    }

    @Override
    public void run() {
        String QUERY = "SELECT id, name, lastname, password, emailpassword, nick FROM user WHERE email=\""+email+"\"";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("DRIVER STILL WORKS BTW");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(ConnectionData.DB_URL, ConnectionData.USER, ConnectionData.PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            rs.next();
            int n1 = rs.getInt("id");
            String n2 = rs.getString("name");
            String n3 = rs.getString("lastname");
            String n4 = rs.getString("password");
            String n5 = rs.getString("emailpassword");
            String n6 = rs.getString("nick");

            Loggeduser.id = n1;
            Loggeduser.name = n2;
            Loggeduser.lastname = n3;
            Loggeduser.password = n4;
            Loggeduser.emailPassword = n5;
            Loggeduser.nick = n6;

            MainActivity.setLoginOk(n4.equals(password));
            System.out.println(Loggeduser.getLoggeduser());
        } catch (SQLException throwables) {
            System.out.println("HERE IS SOMETHING WRONG");
            throwables.printStackTrace();
        }
    }
}
