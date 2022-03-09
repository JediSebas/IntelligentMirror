package com.jedisebas.inteligentmirror;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
        String checkIsExist1 = "SELECT email FROM user WHERE email=\"" + email + "\"";
        String checkIsExist2 = "SELECT nick FROM user WHERE nick=\"" + nick + "\"";

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
            Statement stmt1 = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(checkIsExist1);
            ResultSet rs2 = stmt2.executeQuery(checkIsExist2);
            boolean r1 = rs1.next();
            boolean r2 = rs2.next();

            SignupActivity.isEmailTaken = false;
            SignupActivity.isNickTaken = false;

            if (r1) {
                System.out.println("Email taken");
                SignupActivity.isEmailTaken = true;
            }
            if (r2) {
                System.out.println("Nick taken");
                SignupActivity.isNickTaken = true;
            }
            if (!(r1 || r2)) {
                SignupActivity.isEmailTaken = false;
                SignupActivity.isNickTaken = false;
                Statement statement = conn.createStatement();
                statement.executeUpdate(QUERY);
            }
        } catch (SQLException throwables) {
            System.out.println("HERE IS SOMETHING WRONG");
            throwables.printStackTrace();
        }
    }
}
