package com.jedisebas.inteligentmirror;

abstract public class Nickname {
    /**
     * Class change the name into another name which is used in database to log in.
     * @param originalNickName Original name which is name + lastname
     * @return String which is name_lastname
     */
    public static String changeProfileName(String originalNickName) {
        StringBuilder finalNickName = new StringBuilder();
        int j = 0;
        for (int i = 0; i < originalNickName.length(); i++) {
            if (originalNickName.charAt(i) == ' ') {
                if (j==0) {
                    finalNickName.append("_");
                    j++;
                }
            } else {
                finalNickName.append(originalNickName.charAt(i));
            }
        }
        return finalNickName.toString();
    }
}
