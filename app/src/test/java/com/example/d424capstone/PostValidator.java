package com.example.d424capstone;

public class PostValidator {

    public boolean validatePost(String content) {
        return content != null && !content.isEmpty();
    }
}