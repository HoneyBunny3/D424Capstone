package com.hearthy.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hearthy.d424capstone.entities.PremiumStorefront;

import java.util.List;

@Dao
public interface PremiumStorefrontDAO {
    @Insert
    long insert(PremiumStorefront storefront);

    @Update
    void update(PremiumStorefront storefront);

    @Delete
    void delete(PremiumStorefront storefront);

    @Query("SELECT * FROM premium_storefront_table WHERE storefrontID = :storefrontID")
    PremiumStorefront getStorefrontByID(int storefrontID);

    @Query("SELECT * FROM premium_storefront_table WHERE userID = :userID")
    List<PremiumStorefront> getStorefrontsByUserID(int userID);

    @Query("SELECT * FROM premium_storefront_table")
    List<PremiumStorefront> getAllStorefronts();
}