package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.Cat;

import java.util.List;

/**
 * Data Access Object (DAO) for the Cat entity.
 */
@Dao
public interface CatDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Cat cat);  // Ensure it returns long

    @Update
    void update(Cat cat);

    @Query("DELETE FROM cat_table WHERE catID = :catID")
    void delete(int catID);

    @Query("SELECT * FROM cat_table WHERE catID = :catID")
    Cat getCatByID(int catID);

    @Query("SELECT * FROM cat_table ORDER BY catID ASC")
    List<Cat> getAllCats();

    @Query("SELECT * FROM cat_table WHERE userID = :userID ORDER BY catID ASC")
    List<Cat> getCatsForUser(int userID);
}