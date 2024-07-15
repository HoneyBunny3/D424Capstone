package com.example.d424capstone.utilities;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class StorefrontValidatorTest {

    private final StorefrontValidator storefrontValidator = new StorefrontValidator();

    @Test
    public void testValidateStorefront() {
        assertTrue(storefrontValidator.validateStorefront("Cat Store", "A store for cat lovers"));
        assertFalse(storefrontValidator.validateStorefront("", "A store for cat lovers"));
        assertFalse(storefrontValidator.validateStorefront("Cat Store", ""));
    }
}