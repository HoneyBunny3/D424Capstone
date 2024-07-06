package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "social_post_table",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "userID",
                onDelete = ForeignKey.CASCADE))
public class SocialPost implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int socialPostID;
    private int userID;
    private String content;
    private int likes;

    public SocialPost(int socialPostID, int userID, String content, int likes) {
        this.socialPostID = socialPostID;
        this.userID = userID;
        this.content = content;
        this.likes = likes;
    }

    // Getter and setter methods

    public int getSocialPostID() {
        return socialPostID;
    }

    public void setSocialPostID(int socialPostID) {
        this.socialPostID = socialPostID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}