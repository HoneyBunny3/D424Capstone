package com.example.d424capstone.utilities;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ValidatorTest {

    private final Validator validator = new Validator();

    @Test
    public void testIsValidEmail() {
        assertTrue(validator.isValidEmail("test@example.com"));
        assertFalse(validator.isValidEmail("test@"));
        assertFalse(validator.isValidEmail("test.com"));
    }

    @Test
    public void testIsAlphabetic() {
        assertTrue(validator.isAlphabetic("John"));
        assertFalse(validator.isAlphabetic("John123"));
    }

    @Test
    public void testIsPasswordValid() {
        assertTrue(validator.isPasswordValid("Password1!"));
        assertFalse(validator.isPasswordValid("pass"));
    }
}