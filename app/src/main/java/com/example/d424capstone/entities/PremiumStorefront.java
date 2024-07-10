package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "premium_storefront_table")
public class PremiumStorefront {
    @PrimaryKey(autoGenerate = true)
    private int storefrontID;
    private String name;
    private String email;
    private int userID;

    public PremiumStorefront(int storefrontID, String name, String email, int userID) {
        this.storefrontID = storefrontID;
        this.name = name;
        this.email = email;
        this.userID = userID;
    }

    // Getter and setter methods
    public int getStorefrontID() {
        return storefrontID;
    }

    public void setStorefrontID(int storefrontID) {
        this.storefrontID = storefrontID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}