package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_item_table")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private int cartItemID;
    private String name;
    private double itemPrice;
    private int quantity;

    public CartItem(int cartItemID, String name, double itemPrice, int quantity) {
        this.cartItemID = cartItemID;
        this.name = name;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}