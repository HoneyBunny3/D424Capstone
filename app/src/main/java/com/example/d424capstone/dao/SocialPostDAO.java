package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.SocialPost;

import java.util.List;

@Dao
public interface SocialPostDAO {

    // Inserts a new social post into the social_post_table.
    // If there is a conflict, the insertion is ignored.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SocialPost socialPost);

    // Updates an existing social post in the social_post_table
    @Update
    void update(SocialPost socialPost);

    // Deletes a social post from the social_post_table based on the socialPostID.
    @Query("DELETE FROM social_post_table WHERE socialPostID = :socialPostID")
    void delete(int socialPostID);

    @Query("SELECT * FROM social_post_table WHERE socialPostID = :socialPostID")
    SocialPost getSocialPostByID(int socialPostID);

    // Retrieves all social posts from the social_post_table ordered by socialPostID in ascending order.
    @Query("SELECT * FROM social_post_table ORDER BY socialPostID ASC")
    List<SocialPost> getAllSocialPosts();

    @Query("SELECT * FROM social_post_table ORDER BY likes DESC LIMIT 1")
    SocialPost getMostLikedPost();
}
