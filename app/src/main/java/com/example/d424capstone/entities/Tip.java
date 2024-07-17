package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tip_table")
public class Tip {
    @PrimaryKey(autoGenerate = true)
    private int tipID;
    private String title;
    private String content;
    private String source;

    public Tip(int tipID, String title, String content, String source) {
        this.tipID = tipID;
        this.title = title;
        this.content = content;
        this.source = source;
    }

    public int getTipID() {
        return tipID;
    }

    public void setTipID(int tipID) {
        this.tipID = tipID;
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