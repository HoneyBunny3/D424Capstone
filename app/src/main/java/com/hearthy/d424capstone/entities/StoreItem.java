package com.hearthy.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "store_item_table")
public class StoreItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int storeItemID;
    private String name;
    private String description;
    private double price;
    private boolean isPremium;

    public StoreItem(int storeItemID, String name, String description, double price, boolean isPremium) {
        this.storeItemID = storeItemID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isPremium = isPremium;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
}