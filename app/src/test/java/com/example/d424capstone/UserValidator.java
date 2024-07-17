package com.example.d424capstone;

public class UserValidator {

    public boolean validateSignUpInput(String email, String firstName, String lastName, String password) {
        Validator validator = new Validator();
        return validator.isValidEmail(email) &&
                validator.isAlphabetic(firstName) &&
                validator.isAlphabetic(lastName) &&
                validator.isPasswordValid(password);
    }
}