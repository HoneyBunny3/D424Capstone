package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.PremiumStoreItem;

import java.util.List;

@Dao
public interface PremiumStoreItemDAO {

    @Insert
    long insert(PremiumStoreItem item);

    @Update
    void update(PremiumStoreItem item);

    @Delete
    void delete(PremiumStoreItem item);

    @Query("SELECT * FROM premium_store_item_table WHERE premiumStoreItemId = :premiumStoreItemId")
    PremiumStoreItem getItemById(int premiumStoreItemId);

    @Query("SELECT * FROM premium_store_item_table")
    List<PremiumStoreItem> getAllItems();
}