package com.jedisebas.inteligentmirror;

public class Loggeduser {
    public static String ip;
    public static int id;
    public static String name;
    public static String lastname;
    public static String password;
    public static String email;
    public static boolean isLogged;

    public static String getLoggeduser() {
        return "Loggeduser{\n" +
                "id='" + id + "'\n" +
                ", ip='" + ip + "'\n" +
                ", name='" + name + "'\n" +
                ", lastname='" + lastname + "'\n" +
                ", password='" + password + "'\n" +
                ", email='" + email + "'\n" +
                ", isLogged='" + isLogged + "'\n}";
    }
}
