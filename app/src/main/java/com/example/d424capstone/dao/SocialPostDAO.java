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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SocialPost socialPost);

    @Update
    void update(SocialPost socialPost);

    @Query("DELETE FROM social_post_table WHERE socialPostID = :socialPostID")
    void delete(int socialPostID);

    @Query("SELECT * FROM social_post_table WHERE socialPostID = :socialPostID")
    SocialPost getSocialPostByID(int socialPostID);

    @Query("SELECT * FROM social_post_table ORDER BY socialPostID ASC")
    List<SocialPost> getAllSocialPosts();

    @Query("SELECT * FROM social_post_table ORDER BY likes DESC LIMIT 1")
    SocialPost getMostLikedPost();
}