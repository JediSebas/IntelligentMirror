package com.jedisebas.inteligentmirror;

public class Loggeduser {
    public static String ip = "192.168.0.50";
    public static int id;
    public static String name;
    public static String lastname;
    public static String password;
    public static String email;
    public static String emailPassword;
    public static String nick;
    public static boolean isLogged;

    public static String getLoggeduser() {
        return "Loggeduser{\n" +
                "id='" + id + "'\n" +
                ", ip='" + ip + "'\n" +
                ", name='" + name + "'\n" +
                ", lastname='" + lastname + "'\n" +
                ", password='" + password + "'\n" +
                ", email='" + email + "'\n" +
                ", emailpassword='" + emailPassword + "'\n" +
                ", nick='" + nick + "'\n" +
                ", isLogged='" + isLogged + "'\n}";
    }
}
