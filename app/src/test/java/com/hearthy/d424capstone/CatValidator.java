package com.hearthy.d424capstone;

public class CatValidator {

    public boolean validateCatProfile(String name, int age, String description) {
        return name != null && !name.isEmpty() &&
                age > 0 &&
                description != null && !description.isEmpty();
    }
}