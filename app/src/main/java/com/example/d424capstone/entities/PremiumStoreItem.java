package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "premium_store_item_table")
public class PremiumStoreItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int premiumStoreItemId;
    private String name;
    private String description;
    private double price;

    public PremiumStoreItem(int premiumStoreItemId, String name, String description, double price) {
        this.premiumStoreItemId = premiumStoreItemId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getter and setter methods

    public int getPremiumStoreItemId() {
        return premiumStoreItemId;
    }

    public void setPremiumStoreItemId(int premiumStoreItemId) {
        this.premiumStoreItemId = premiumStoreItemId;
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
}