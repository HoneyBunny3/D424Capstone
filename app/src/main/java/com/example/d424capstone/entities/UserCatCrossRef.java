package com.example.d424capstone.entities;

import androidx.room.Entity;

/**
 * Entity class representing the cross-reference table between Users and Cats in the database.
 * This establishes a many-to-many relationship between users and cats.
 */
@Entity(primaryKeys = {"userID", "catID"}, tableName = "user_cat_cross_ref")
public class UserCatCrossRef {
    private int userID;
    private int catID;

    /**
     * Constructor to initialize the UserCatCrossRef entity.
     *
     * @param userID The ID of the user.
     * @param catID  The ID of the cat.
     */
    public UserCatCrossRef(int userID, int catID) {
        this.userID = userID;
        this.catID = catID;
    }

    // Getter and setter methods

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }
}