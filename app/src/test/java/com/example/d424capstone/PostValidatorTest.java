package com.example.d424capstone;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PostValidatorTest {

    private final PostValidator postValidator = new PostValidator();

    @Test
    public void testValidatePost() {
        assertTrue(postValidator.validatePost("This is a post about my cat")); //correct social post format
        assertFalse(postValidator.validatePost("")); //invalid post format; missing social post content
    }
}