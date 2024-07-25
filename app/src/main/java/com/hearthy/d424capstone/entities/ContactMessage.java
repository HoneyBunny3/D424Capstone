package com.hearthy.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "contact_message_table")
public class ContactMessage implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int messageID;
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String subject;
    private String message;
    private Date timestamp;

    public ContactMessage(int userID, String firstName, String lastName, String email, String subject, String message, Date timestamp) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserId(int userId) {
        this.userID = userId;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}