package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM order_table ORDER BY orderId DESC LIMIT 1")
    Order getLatestOrder();

    @Query("SELECT * FROM order_table WHERE userID = :userID ORDER BY orderId DESC LIMIT 1")
    Order getLatestOrderForUser(int userID);

    @Query("SELECT * FROM order_table ORDER BY orderId ASC")
    List<Order> getAllOrders();
}