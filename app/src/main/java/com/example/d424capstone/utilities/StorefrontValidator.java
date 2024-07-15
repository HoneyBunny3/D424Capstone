package com.example.d424capstone.utilities;

public class StorefrontValidator {

    public boolean validateStorefront(String name, String description) {
        return name != null && !name.isEmpty() &&
                description != null && !description.isEmpty();
    }
}