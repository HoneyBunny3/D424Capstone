package com.hearthy.d424capstone;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ValidatorTest {

    private final Validator validator = new Validator();

    @Test
    public void testIsValidEmail() {
        assertTrue(validator.isValidEmail("test@example.com")); //correct email format
        assertFalse(validator.isValidEmail("test@")); //invalid email format; missing mail server and domain
        assertFalse(validator.isValidEmail("test.com")); //invalid email format; missing local segment and '@' character
    }

    @Test
    public void testIsAlphabetic() {
        assertTrue(validator.isAlphabetic("John")); //correct first name format
        assertFalse(validator.isAlphabetic("John123")); //incorrect first name format; non-alphabetic characters used
    }

    @Test
    public void testIsPasswordValid() {
        assertTrue(validator.isPasswordValid("Password1!")); //correct password format
        assertFalse(validator.isPasswordValid("pass")); //incorrect password format; missing required capital letter, number, special character, and too short
    }
}