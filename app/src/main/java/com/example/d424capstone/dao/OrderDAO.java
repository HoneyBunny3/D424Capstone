package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.d424capstone.entities.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Order order);

    @Query("SELECT * FROM order_table ORDER BY orderId ASC")
    List<Order> getAllOrders();
}