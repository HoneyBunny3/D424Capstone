package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "cat_table",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "userID",
                onDelete = ForeignKey.CASCADE))
public class Cat implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int catID;
    private String catName;
    private int catAge;
    private String catImage;
    private String catBio;
    private int userID;

    public Cat(int catID, String catName, int catAge, String catImage, String catBio, int userID) {
        this.catID = catID;
        this.catName = catName;
        this.catAge = catAge;
        this.catImage = catImage;
        this.catBio = catBio;
        this.userID = userID;
    }

    // Getter and setter methods

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatAge() {
        return catAge;
    }

    public void setCatAge(int catAge) {
        this.catAge = catAge;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getCatBio() {
        return catBio;
    }

    public void setCatBio(String catBio) {
        this.catBio = catBio;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}