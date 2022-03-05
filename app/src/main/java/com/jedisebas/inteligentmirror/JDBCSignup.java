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
    String name, lastname, password, email, ip;

    public JDBCSignup(String name, String lastname, String password, String email, String ip) {
        t = new Thread(this);
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.ip = ip;
    }

    @Override
    public void run() {
        //TODO password hash
        String QUERY = "INSERT INTO `user` (`id`, `name`, `lastname`, `password`, `email`) VALUES " +
                "(NULL, '"+name+"', '"+lastname+"', '"+password+"', '"+email+"');";
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
