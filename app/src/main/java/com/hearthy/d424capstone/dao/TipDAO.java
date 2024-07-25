package com.hearthy.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hearthy.d424capstone.entities.Tip;

import java.util.List;

@Dao
public interface TipDAO {
    @Insert
    long insert(Tip tip);  // Change return type to long

    @Update
    void update(Tip tip);

    @Delete
    void delete(Tip tip);

    @Query("SELECT * FROM tip_table")
    List<Tip> getAllTips();
}