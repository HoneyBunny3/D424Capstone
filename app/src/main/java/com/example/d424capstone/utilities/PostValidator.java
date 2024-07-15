package com.example.d424capstone.utilities;

public class PostValidator {

    public boolean validatePost(String content) {
        return content != null && !content.isEmpty();
    }
}