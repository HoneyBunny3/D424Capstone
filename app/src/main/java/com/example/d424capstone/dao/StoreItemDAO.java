package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.StoreItem;

import java.util.List;

/**
 * Data Access Object (DAO) for the StoreItem entity.
 */
@Dao
public interface StoreItemDAO {

    /**
     * Inserts a new store item into the store_item_table.
     * If there is a conflict, the insertion is ignored.
     *
     * @param storeItem The store item to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StoreItem storeItem);

    /**
     * Updates an existing store item in the store_item_table.
     *
     * @param storeItem The store item to be updated.
     */
    @Update
    void update(StoreItem storeItem);

    /**
     * Deletes a store item from the store_item_table based on the storeItemID.
     *
     * @param storeItemID The ID of the store item to be deleted.
     */
    @Query("DELETE FROM store_item_table WHERE storeItemID = :storeItemID")
    void delete(int storeItemID);

    /**
     * Retrieves a store item from the store_item_table based on the storeItemID.
     *
     * @param storeItemID The ID of the store item to be retrieved.
     * @return The store item with the specified ID.
     */
    @Query("SELECT * FROM store_item_table WHERE storeItemID = :storeItemID")
    StoreItem getStoreItemByID(int storeItemID);

    /**
     * Retrieves all store items from the store_item_table ordered by storeItemID in ascending order.
     *
     * @return A list of all store items ordered by storeItemID.
     */
    @Query("SELECT * FROM store_item_table ORDER BY storeItemID ASC")
    List<StoreItem> getAllStoreItems();

    /**
     * Retrieves the featured store item from the store_item_table.
     *
     * @return The featured store item.
     */
    @Query("SELECT * FROM store_item_table WHERE isFeatured = 1 LIMIT 1")
    StoreItem getFeaturedItem();
}