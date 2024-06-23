package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing an Associated Cat in the database.
 */
@Entity(tableName = "associated_cats")
public class AssociatedCats {
    @PrimaryKey(autoGenerate = true)
    private int catID;
    private String catName;
    private int catAge;
    private String imageUri;
    private String catBio;

    /**
     * Constructor to initialize the AssociatedCats entity.
     *
     * @param catID   The unique ID of the cat.
     * @param catName The name of the cat.
     * @param catAge  The age of the cat.
     * @param imageUri The URI of the cat's image.
     * @param catBio The bio of the cat.
     */
    public AssociatedCats(int catID, String catName, int catAge, String imageUri, String catBio) {
        this.catID = catID;
        this.catName = catName;
        this.catAge = catAge;
        this.imageUri = imageUri;
        this.catBio = catBio;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCatBio() {
        return catBio;
    }

    public void setCatBio(String catBio) {
        this.catBio = catBio;
    }
}