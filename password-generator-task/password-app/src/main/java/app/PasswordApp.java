package app;

import pwdgen.PasswordGenerator;

public class PasswordApp {

    public static void main(String[] args) {
        int length = 16;
        String password = PasswordGenerator.generatePassword(length);
        System.out.println("Generated password (length " + length + "): " + password);
    }
}