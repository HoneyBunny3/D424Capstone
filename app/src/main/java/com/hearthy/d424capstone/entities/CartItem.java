package com.hearthy.d424capstone.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_item_table")
public class CartItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int cartItemID;
    private String name;
    private double price;
    private int quantity;

    public CartItem(int cartItemID, String name, double price, int quantity) {
        this.cartItemID = cartItemID;
        this.name = name;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    protected CartItem(Parcel in) {
        cartItemID = in.readInt();
        name = in.readString();
        price = in.readDouble();
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
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeInt(quantity);
    }
}