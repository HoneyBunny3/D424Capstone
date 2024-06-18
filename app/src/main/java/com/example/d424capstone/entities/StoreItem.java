package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "store_item_table")
public class StoreItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int storeItemID;
    private String name;
    private String description;
    private double itemPrice;
    private boolean isFeatured;

    //Constructor to initialize the StoreItem entity
    public StoreItem(int storeItemID, String name, String description, double itemPrice, boolean isFeatured) {
        this.storeItemID = storeItemID;
        this.name = name;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isFeatured = isFeatured;
    }

    // Getter and setter methods for storeItemID
    public int getStoreItemID() {return storeItemID;}

    public void setStoreItemID(int storeItemID) {
        this.storeItemID = storeItemID;
    }

    // Getter and setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and setter methods for itemPrice
    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    // Getter and setter methods for isFeatured
    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }
}
