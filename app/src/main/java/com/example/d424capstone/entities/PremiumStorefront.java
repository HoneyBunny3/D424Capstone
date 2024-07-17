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
    private String cardNumber;
    private String expiry;
    private String cvv;

    public PremiumStorefront(int storefrontID, String name, String email, int userID, String cardNumber, String expiry, String cvv) {
        this.storefrontID = storefrontID;
        this.name = name;
        this.email = email;
        this.userID = userID;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}