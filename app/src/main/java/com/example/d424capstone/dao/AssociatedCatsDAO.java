package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.AssociatedCats;

import java.util.List;

/**
 * Data Access Object (DAO) for the AssociatedCats entity.
 */
@Dao
public interface AssociatedCatsDAO {

    /**
     * Inserts a new associated cat into the associated_cats table.
     * If there is a conflict, the insertion is ignored.
     *
     * @param associatedCat The associated cat to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(AssociatedCats associatedCat);

    /**
     * Updates an existing associated cat in the associated_cats table.
     *
     * @param associatedCat The associated cat to be updated.
     */
    @Update
    void update(AssociatedCats associatedCat);

    /**
     * Deletes an associated cat from the associated_cats table based on the catID.
     *
     * @param catID The ID of the associated cat to be deleted.
     */
    @Query("DELETE FROM associated_cats WHERE catID = :catID")
    void delete(int catID);

    /**
     * Retrieves an associated cat from the associated_cats table based on the catID.
     *
     * @param catID The ID of the associated cat to be retrieved.
     * @return The associated cat with the specified ID.
     */
    @Query("SELECT * FROM associated_cats WHERE catID = :catID")
    AssociatedCats getCatByID(int catID);

    /**
     * Retrieves all associated cats from the associated_cats table ordered by catID in ascending order.
     *
     * @return A list of all associated cats ordered by catID.
     */
    @Query("SELECT * FROM associated_cats ORDER BY catID ASC")
    List<AssociatedCats> getAllCats();
}