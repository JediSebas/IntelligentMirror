package com.jedisebas.inteligentmirror;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCLogin implements Runnable {

    /**
     * Class is used to log in account.
     */

    public Thread t;
    String nick, password, ip;

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
        String QUERY = "SELECT nick FROM userss WHERE password=\""+password+"\"";
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
    }
}
