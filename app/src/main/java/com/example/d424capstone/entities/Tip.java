package com.example.d424capstone.models;

public class Tip {
    private String title;
    private String content;
    private String source;

    public Tip(String title, String content, String source) {
        this.title = title;
        this.content = content;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }
}