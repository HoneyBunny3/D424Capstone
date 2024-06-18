package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.StoreItem;

import java.util.List;

@Dao
public interface StoreItemDAO {

    // Inserts a new store item into the store_item_table.
    // If there is a conflict, the insertion is ignored.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StoreItem storeItem);

    // Updates an existing store item in the store_item_table
    @Update
    void update(StoreItem storeItem);

    // Deletes a store item from the store_item_table based on the storeItemID.
    @Query("DELETE FROM store_item_table WHERE storeItemID = :storeItemID")
    void delete(int storeItemID);

    @Query("SELECT * FROM store_item_table WHERE storeItemID = :storeItemID")
    StoreItem getStoreItemByID(int storeItemID);

    // Retrieves all store items from the store_item_table ordered by storeItemID in ascending order.
    @Query("SELECT * FROM store_item_table ORDER BY storeItemID ASC")
    List<StoreItem> getAllStoreItems();

    @Query("SELECT * FROM store_item_table WHERE isFeatured = 1 LIMIT 1")
    StoreItem getFeaturedItem();
}
