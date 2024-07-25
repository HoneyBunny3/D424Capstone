package com.hearthy.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hearthy.d424capstone.entities.ContactMessage;

import java.util.List;

@Dao
public interface ContactMessageDAO {
    @Insert
    void insert(ContactMessage contactMessage);

    @Query("SELECT * FROM contact_message_table ORDER BY timestamp DESC")
    List<ContactMessage> getAllMessages();
}