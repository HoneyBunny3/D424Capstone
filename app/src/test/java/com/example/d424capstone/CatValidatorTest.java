package com.example.d424capstone;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CatValidatorTest {

    private final CatValidator catValidator = new CatValidator();

    @Test
    public void testValidateCatProfile() {
        assertTrue(catValidator.validateCatProfile("Whiskers", 2, "A friendly cat")); //correct cat profile format
        assertFalse(catValidator.validateCatProfile("", 2, "A friendly cat")); //invalid name; missing cat name
        assertFalse(catValidator.validateCatProfile("Whiskers", -1, "A friendly cat")); //invalid age; cat age is negative integer
        assertFalse(catValidator.validateCatProfile("Whiskers", 2, "")); //invalid description; missing cat description
    }
}