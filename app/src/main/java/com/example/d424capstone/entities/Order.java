package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int orderId;
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;

    public Order(String cardNumber, String cardExpiry, String cardCVV) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVV = cardCVV;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }
}