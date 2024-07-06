package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "order_table")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int orderId;
    private int userId;
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;
    private String confirmationNumber;
    private double totalPaid;
    private String purchasedItems;
    private Date orderDate;

    public Order(int userId, String cardNumber, String cardExpiry, String cardCVV, double totalPaid, String purchasedItems, Date orderDate) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVV = cardCVV;
        this.totalPaid = totalPaid;
        this.purchasedItems = purchasedItems;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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