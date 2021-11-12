package com.jedisebas.inteligentmirror;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCSignup implements Runnable {

    /**
     * Class provide MySQL connection and insert new account data.
     */

    public Thread t;
    String nick, name, lastname, password, email, ip;

    public JDBCSignup(String nick, String name, String lastname, String password, String email, String ip) {
        t = new Thread(this);
        this.nick = nick;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.ip = ip;
    }

    @Override
    public void run() {
        String DB_URL = "jdbc:mysql://"+ip+"/mirror";
        String USER = "user";
        String PASS = "user"; // test password
        //TODO password hash
        String QUERY = "INSERT INTO `userss` (`id`, `name`, `lastname`, `password`, `email`, `nick`) VALUES " +
                "(NULL, '"+name+"', '"+lastname+"', '"+password+"', '"+email+"', '"+nick+"');";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("DRIVER STILL WORKS BTW");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(QUERY);
        } catch (SQLException throwables) {
            System.out.println("HERE IS SOMETHING WRONG");
            throwables.printStackTrace();
        }
    }
}
