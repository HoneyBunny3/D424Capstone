package com.hearthy.d424capstone;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class StorefrontValidatorTest {

    private final StorefrontValidator storefrontValidator = new StorefrontValidator();

    @Test
    public void testValidateStorefront() {
        assertTrue(storefrontValidator.validateStorefront("Cat Store", "A store for cat lovers")); //correct storefront format
        assertFalse(storefrontValidator.validateStorefront("", "A store for cat lovers")); //invalid format; missing storefront name
        assertFalse(storefrontValidator.validateStorefront("Cat Store", "")); //invalid format; missing storefront description
    }
}