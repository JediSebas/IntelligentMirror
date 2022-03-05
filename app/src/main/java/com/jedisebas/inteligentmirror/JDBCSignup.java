package com.jedisebas.inteligentmirror;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class provide MySQL connection and insert new account data.
 */

public class JDBCSignup implements Runnable {

    public Thread t;
    String name, lastname, password, email, emailPasswd, nick;

    public JDBCSignup(String name, String lastname, String password, String email, String emailPasswd, String nick) {
        t = new Thread(this);
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.emailPasswd = emailPasswd;
        this.nick = nick;
    }

    @Override
    public void run() {
        String QUERY = "INSERT INTO `user` (`id`, `name`, `lastname`, `password`, `email`, `emailpassword`, `nick`) VALUES " +
                "(NULL, '"+name+"', '"+lastname+"', '"+password+"', '"+email+"', '"+emailPasswd+"', '"+nick+"');";
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
