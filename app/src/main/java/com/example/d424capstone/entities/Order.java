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
    private String confirmationNumber;
    private double totalPaid;
    private String purchasedItems;

    public Order(String cardNumber, String cardExpiry, String cardCVV, double totalPaid, String purchasedItems) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVV = cardCVV;
        this.totalPaid = totalPaid;
        this.purchasedItems = purchasedItems;
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

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(String purchasedItems) {
        this.purchasedItems = purchasedItems;
    }
}