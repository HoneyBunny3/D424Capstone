package com.hearthy.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.hearthy.d424capstone.entities.Cat;
import java.util.List;

@Dao
public interface CatDAO {

    @Insert
    long insert(Cat cat);

    @Update
    void update(Cat cat);

    @Delete
    void delete(Cat cat);

    @Query("SELECT * FROM cat_table WHERE catID = :catID")
    Cat getCatByID(int catID);

    @Query("SELECT * FROM cat_table WHERE userID = :userID")
    List<Cat> getCatsForUser(int userID);
}