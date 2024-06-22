package com.example.d424capstone.entities;

import androidx.room.Entity;
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
    private String userName;
    private String email;
    private String password;
    private String role;

    /**
     * Constructor to initialize the User entity.
     *
     * @param userID    The unique ID of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param userName  The username of the user.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     * @param role      The role of the user.
     */
    public User(int userID, String firstName, String lastName, String userName, String email, String password, String role) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}