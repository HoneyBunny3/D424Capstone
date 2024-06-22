package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.d424capstone.entities.UserCatCrossRef;
import com.example.d424capstone.entities.UserWithCats;
import com.example.d424capstone.entities.CatWithUsers;

import java.util.List;

/**
 * Data Access Object (DAO) for the UserCatCrossRef entity, managing the many-to-many relationship between Users and Cats.
 */
@Dao
public interface UserCatCrossRefDAO {

    /**
     * Inserts a new UserCatCrossRef into the user_cat_cross_ref table.
     * If there is a conflict, the insertion is ignored.
     *
     * @param userCatCrossRef The UserCatCrossRef to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserCatCrossRef userCatCrossRef);

    /**
     * Retrieves a list of UserWithCats for a given userID.
     *
     * @param userID The ID of the user whose cats are to be retrieved.
     * @return A list of UserWithCats associated with the specified userID.
     */
    @Transaction
    @Query("SELECT * FROM user_table WHERE userID = :userID")
    List<UserWithCats> getCatsForUser(int userID);

    /**
     * Retrieves a list of CatWithUsers for a given catID.
     *
     * @param catID The ID of the cat whose users are to be retrieved.
     * @return A list of CatWithUsers associated with the specified catID.
     */
    @Transaction
    @Query("SELECT * FROM associated_cats WHERE catID = :catID")
    List<CatWithUsers> getUsersForCat(int catID);
}