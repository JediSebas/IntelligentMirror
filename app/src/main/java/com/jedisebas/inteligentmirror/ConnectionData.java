package com.jedisebas.inteligentmirror;

public class ConnectionData {
    public static String DB_URL = "jdbc:mysql://"+ Loggeduser.ip+"/mirror";
    public static final String USER = "user";
    public static final String PASS = "zaq1@WSX";

    public static final int PORT = 21;
    public static final String DIRECTORY_IN_MIRROR = "/IntelligentMirror/data/";
    public static final String DIRECTORY_WITH_IMAGES = "/var/www/html/mirror/" ;
}
