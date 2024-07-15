package com.example.d424capstone.utilities;

public class Validator {

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean isAlphabetic(String text) {
        return text != null && text.matches("[a-zA-Z]+");
    }

    public boolean isPasswordValid(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&+=?-].*");
    }
}