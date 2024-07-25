package com.hearthy.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "order_table")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int orderID;
    private int userID;
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;
    private String confirmationNumber;
    private double totalPaid;
    private String purchasedItems;
    private Date orderDate;
    private String trackingNumber;
    private String carrierName;

    public Order(int userID, String cardNumber, String cardExpiry, String cardCVV, double totalPaid, String purchasedItems, Date orderDate) {
        this.userID = userID;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVV = cardCVV;
        this.totalPaid = totalPaid;
        this.purchasedItems = purchasedItems;
        this.orderDate = orderDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
}