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

    /**
     * Inserts a new user into the user_table.
     * If there is a conflict, the insertion is ignored.
     *
     * @param user The user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    /**
     * Updates an existing user in the user_table.
     *
     * @param user The user to be updated.
     */
    @Update
    void update(User user);

    /**
     * Deletes a user from the user_table based on the userID.
     *
     * @param userID The ID of the user to be deleted.
     */
    @Query("DELETE FROM user_table WHERE userID = :userID")
    void delete(int userID);

    /**
     * Retrieves a user from the user_table based on the userID.
     *
     * @param userID The ID of the user to be retrieved.
     * @return The user with the specified ID.
     */
    @Query("SELECT * FROM user_table WHERE userID = :userID")
    User getUserByID(int userID);

    /**
     * Retrieves a user from the user_table based on the userName.
     *
     * @param userName The username of the user to be retrieved.
     * @return The user with the specified username.
     */
    @Query("SELECT * FROM user_table WHERE userName = :userName")
    User getUserByUsername(String userName);

    /**
     * Retrieves a user from the user_table based on the email.
     *
     * @param email The email of the user to be retrieved.
     * @return The user with the specified email.
     */
    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUserByEmail(String email);

    /**
     * Retrieves all users from the user_table ordered by userID in ascending order.
     *
     * @return A list of all users ordered by userID.
     */
    @Query("SELECT * FROM user_table ORDER BY userID ASC")
    List<User> getAllUsers();
}