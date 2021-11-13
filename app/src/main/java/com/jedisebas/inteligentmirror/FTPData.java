package com.jedisebas.inteligentmirror;

/**
 * Class contain data required to make FTP connection.
 */

public enum FTPData {
    PORT(21), USER("user"), PASSWORD("user"), DIRECTORY_IN_MIRROR("/home/user/upload123/");

    private int port;
    private String str;

    FTPData(int port) {
        this.port = port;
    }

    FTPData(String str) {
        this.str = str;
    }

    int getPort() { return port; }

    String get() { return str; }
}
