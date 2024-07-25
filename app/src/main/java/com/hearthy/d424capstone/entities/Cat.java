package com.hearthy.d424capstone.entities;

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
    private String name;
    private int age;
    private String bio;
    private String image;
    private int userID;

    public Cat(int catID, String name, int age, String image, String bio, int userID) {
        this.catID = catID;
        this.name = name;
        this.age = age;
        this.image = image;
        this.bio = bio;
        this.userID = userID;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}