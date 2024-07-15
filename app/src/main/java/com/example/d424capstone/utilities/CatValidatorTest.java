package com.example.d424capstone.utilities;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CatValidatorTest {

    private final CatValidator catValidator = new CatValidator();

    @Test
    public void testValidateCatProfile() {
        assertTrue(catValidator.validateCatProfile("Whiskers", 2, "A friendly cat")); //correct format
        assertFalse(catValidator.validateCatProfile("", 2, "A friendly cat")); //missing name
        assertFalse(catValidator.validateCatProfile("Whiskers", -1, "A friendly cat")); //age is negative integer
        assertFalse(catValidator.validateCatProfile("Whiskers", 2, "")); //missing description
    }
}