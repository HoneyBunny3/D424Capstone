package com.example.d424capstone.utilities;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    public void testValidateSignUpInput() {
        assertTrue(userValidator.validateSignUpInput("test@example.com", "John", "Doe", "Password1!"));
        assertFalse(userValidator.validateSignUpInput("test@", "John", "Doe", "Password1!"));
        assertFalse(userValidator.validateSignUpInput("test@example.com", "John123", "Doe", "Password1!"));
        assertFalse(userValidator.validateSignUpInput("test@example.com", "John", "Doe!", "Password1!"));
        assertFalse(userValidator.validateSignUpInput("test@example.com", "John", "Doe", "pass"));
    }
}