package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Entity class representing a Store Item in the database.
 */
@Entity(tableName = "store_item_table")
public class StoreItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int storeItemID;
    private String name;
    private String description;
    private double itemPrice;
    private boolean isFeatured;

    /**
     * Constructor to initialize the StoreItem entity.
     *
     * @param storeItemID  The unique ID of the store item.
     * @param name         The name of the store item.
     * @param description  The description of the store item.
     * @param itemPrice    The price of the store item.
     * @param isFeatured   Whether the store item is featured.
     */
    public StoreItem(int storeItemID, String name, String description, double itemPrice, boolean isFeatured) {
        this.storeItemID = storeItemID;
        this.name = name;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isFeatured = isFeatured;
    }

    // Getter and setter methods

    public int getStoreItemID() {
        return storeItemID;
    }

    public void setStoreItemID(int storeItemID) {
        this.storeItemID = storeItemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
}