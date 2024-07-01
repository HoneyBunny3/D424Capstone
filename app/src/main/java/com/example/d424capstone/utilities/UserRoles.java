package com.example.d424capstone.utilities;

/**
 * Utility class containing constants for user roles.
 * This class provides a centralized place for defining user role constants
 * to avoid hardcoding strings throughout the application.
 */
public final class UserRoles {

    // Role constants
    public static final String GUEST = "GUEST";
    public static final String REGULAR = "REGULAR";
    public static final String PREMIUM = "PREMIUM";
    public static final String ADMIN = "ADMIN";

    /**
     * Private constructor to prevent instantiation.
     * This utility class is not publicly instantiable.
     */
    private UserRoles() {
        // Prevent instantiation
    }
}