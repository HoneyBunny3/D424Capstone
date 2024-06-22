package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.SocialPost;

import java.util.List;

/**
 * Data Access Object (DAO) for the SocialPost entity.
 */
@Dao
public interface SocialPostDAO {

    /**
     * Inserts a new social post into the social_post_table.
     * If there is a conflict, the insertion is ignored.
     *
     * @param socialPost The social post to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SocialPost socialPost);

    /**
     * Updates an existing social post in the social_post_table.
     *
     * @param socialPost The social post to be updated.
     */
    @Update
    void update(SocialPost socialPost);

    /**
     * Deletes a social post from the social_post_table based on the socialPostID.
     *
     * @param socialPostID The ID of the social post to be deleted.
     */
    @Query("DELETE FROM social_post_table WHERE socialPostID = :socialPostID")
    void delete(int socialPostID);

    /**
     * Retrieves a social post from the social_post_table based on the socialPostID.
     *
     * @param socialPostID The ID of the social post to be retrieved.
     * @return The social post with the specified ID.
     */
    @Query("SELECT * FROM social_post_table WHERE socialPostID = :socialPostID")
    SocialPost getSocialPostByID(int socialPostID);

    /**
     * Retrieves all social posts from the social_post_table ordered by socialPostID in ascending order.
     *
     * @return A list of all social posts ordered by socialPostID.
     */
    @Query("SELECT * FROM social_post_table ORDER BY socialPostID ASC")
    List<SocialPost> getAllSocialPosts();

    /**
     * Retrieves the most liked social post from the social_post_table.
     *
     * @return The most liked social post.
     */
    @Query("SELECT * FROM social_post_table ORDER BY likes DESC LIMIT 1")
    SocialPost getMostLikedPost();
}