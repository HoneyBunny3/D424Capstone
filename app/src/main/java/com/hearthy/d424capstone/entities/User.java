package com.hearthy.d424capstone.entities;

import androidx.databinding.adapters.Converters;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hearthy.d424capstone.Converters;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(tableName = "user_table")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String role;

    @TypeConverters(Converters.class)
    private Set<Integer> likedPosts;

    public User(int userID, String firstName, String lastName, String email, String phone, String password, String role) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.likedPosts = new HashSet<>();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Set<Integer> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(Set<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public void addLikedPost(int postId) {
        this.likedPosts.add(postId);
    }

    public boolean hasLikedPost(int postId) {
        return this.likedPosts.contains(postId);
    }
}