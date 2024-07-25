package com.hearthy.d424capstone;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    public void testValidateSignUpInput() {
        assertTrue(userValidator.validateSignUpInput("test@example.com", "John", "Doe", "Password1!")); //correct user sign up format
        assertFalse(userValidator.validateSignUpInput("test@", "John", "Doe", "Password1!")); //incorrect format; invalid email
        assertFalse(userValidator.validateSignUpInput("test@example.com", "John123", "Doe", "Password1!")); //incorrect format; invalid first name
        assertFalse(userValidator.validateSignUpInput("test@example.com", "John", "Doe!", "Password1!")); //incorrect format; invalid last name
        assertFalse(userValidator.validateSignUpInput("test@example.com", "John", "Doe", "pass")); //incorrect format; invalid password
    }
}