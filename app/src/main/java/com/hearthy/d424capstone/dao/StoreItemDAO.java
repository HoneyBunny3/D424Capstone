package com.hearthy.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hearthy.d424capstone.entities.StoreItem;

import java.util.List;

@Dao
public interface StoreItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(StoreItem storeItem);  // Ensure it returns long

    @Update
    void update(StoreItem storeItem);

    @Query("DELETE FROM store_item_table WHERE storeItemID = :storeItemID")
    void delete(int storeItemID);

    @Query("SELECT * FROM store_item_table WHERE storeItemID = :storeItemID")
    StoreItem getStoreItemByID(int storeItemID);

    @Query("SELECT * FROM store_item_table ORDER BY storeItemID ASC")
    List<StoreItem> getAllStoreItems();

    @Query("SELECT * FROM store_item_table WHERE name LIKE :query OR description LIKE :query")
    List<StoreItem> searchStoreItems(String query);
}