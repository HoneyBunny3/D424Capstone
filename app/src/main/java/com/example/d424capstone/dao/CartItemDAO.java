package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.CartItem;

import java.util.List;

@Dao
public interface CartItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Query("DELETE FROM cart_item_table WHERE cartItemID = :cartItemID")
    void delete(int cartItemID);

    @Query("SELECT * FROM cart_item_table ORDER BY cartItemID ASC")
    List<CartItem> getAllCartItems();
}