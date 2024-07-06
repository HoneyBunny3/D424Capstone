package com.example.d424capstone.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_item_table")
public class CartItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int cartItemID;
    private String itemName;
    private double itemPrice;
    private int quantity;

    public CartItem(int cartItemID, String itemName, double itemPrice, int quantity) {
        this.cartItemID = cartItemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    protected CartItem(Parcel in) {
        cartItemID = in.readInt();
        itemName = in.readString();
        itemPrice = in.readDouble();
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cartItemID);
        parcel.writeString(itemName);
        parcel.writeDouble(itemPrice);
        parcel.writeInt(quantity);
    }
}