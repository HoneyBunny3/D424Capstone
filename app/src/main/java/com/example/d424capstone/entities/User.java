package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a User in the database.
 */
@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
    private String storefrontName;
    private String storefrontContactEmail;

    /**
     * Default constructor required by Room.
     */
    public User() {}

    /**
     * Constructor to initialize the User entity.
     *
     * @param userID                  The unique ID of the user.
     * @param firstName               The first name of the user.
     * @param lastName                The last name of the user.
     * @param email                   The email address of the user.
     * @param phoneNumber             The phone number of the user.
     * @param password                The password of the user.
     * @param role                    The role of the user.
     * @param storefrontName          The name of the storefront for premium users.
     * @param storefrontContactEmail  The contact email for the storefront.
     */

    public User(int userID, String firstName, String lastName, String userName, String email, String phoneNumber, String password, String role, String storefrontName, String storefrontContactEmail) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.storefrontName = storefrontName;
        this.storefrontContactEmail = storefrontContactEmail;
    }

    /**
     * Constructor for regular users (without storefront fields).
     */
    public User(int userID, String firstName, String lastName, String email, String phoneNumber, String password, String role) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    // Getter and setter methods

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStorefrontName() {
        return storefrontName;
    }

    public void setStorefrontName(String storefrontName) {
        this.storefrontName = storefrontName;
    }

    public String getStorefrontContactEmail() {
        return storefrontContactEmail;
    }

    /**
     * Sets the storefront contact email.
     *
     * @param storefrontContactEmail The storefront contact email.
     */
    public void setStorefrontContactEmail(String storefrontContactEmail) {
        this.storefrontContactEmail = storefrontContactEmail;
    }
}