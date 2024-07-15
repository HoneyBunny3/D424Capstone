// Tip.java
package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tip_table")
public class Tip {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private String source;

    // Constructor, getters, and setters
    public Tip(int id, String title, String content, String source) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}