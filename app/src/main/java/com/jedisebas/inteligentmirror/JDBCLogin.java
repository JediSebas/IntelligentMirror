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
    String nick, password, ip;
    public static String name, lastname, email;

    public JDBCLogin(String nick, String password, String ip) {
        t = new Thread(this);
        this.nick = nick;
        this.password = password;
        this.ip = ip;
    }

    @Override
    public void run() {
        String DB_URL = "jdbc:mysql://"+ip+"/mirror";
        String USER = "user";
        String PASS = "user"; // test password
        //TODO password hash
        //TODO email instead of name and lastname
        String QUERY = "SELECT nick FROM users WHERE password=\""+password+"\"";
        String QUERY2 = "SELECT name, lastname, email FROM users WHERE email=\""+password+"\"";
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
            String n = rs.getString("nick");
            MainActivity.setLoginOk(n.equals(nick));
        } catch (SQLException throwables) {
            System.out.println("HERE IS SOMETHING WRONG");
            throwables.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY2);
            rs.next();
            name = rs.getString("name");
            lastname = rs.getString("lastname");
            email = rs.getString("email");
        } catch (SQLException throwables) {
            System.out.println("HERE IS SOMETHING WRONG");
            throwables.printStackTrace();
        }
    }
}
