package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    // Inserts a new store item into the user_table.
    // If there is a conflict, the insertion is ignored.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    // Updates an existing store item in the user_table
    @Update
    void update(User user);

    // Deletes a user from the user_table based on the userID.
    @Query("DELETE FROM user_table WHERE userID = :userID")
    void delete(int userID);

    @Query("SELECT * FROM user_table WHERE userID = :userID")
    User getUserByID(int userID);

    @Query("SELECT * FROM user_table WHERE userName = :userName LIMIT 1")
    User getUserByUsername(String userName);

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    // Retrieves all users from the user_table ordered by userID in ascending order.
    @Query("SELECT * FROM user_table ORDER BY userID ASC")
    List<User> getAllUsers();
}