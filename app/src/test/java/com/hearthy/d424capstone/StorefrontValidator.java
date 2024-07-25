package com.hearthy.d424capstone;

public class StorefrontValidator {

    public boolean validateStorefront(String name, String description) {
        return name != null && !name.isEmpty() &&
                description != null && !description.isEmpty();
    }
}