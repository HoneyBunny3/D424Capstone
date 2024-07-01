package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.User;

import java.util.List;

/**
 * Data Access Object (DAO) for the User entity.
 */
@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Update
    void update(User user);

    @Query("DELETE FROM user_table WHERE userID = :userID")
    void delete(int userID);

    @Query("SELECT * FROM user_table WHERE userID = :userID")
    User getUserByID(int userID);

    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM user_table ORDER BY userID ASC")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE userID IN (SELECT userID FROM cat_table WHERE catID = :catID)")
    List<User> getUsersForCat(int catID);
}