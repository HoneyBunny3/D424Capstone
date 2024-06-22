package com.example.d424capstone.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

/**
 * Represents a User with a list of associated Cats in the database.
 */
public class UserWithCats {
    @Embedded
    private User user;

    @Relation(
            parentColumn = "userID",
            entityColumn = "catID",
            associateBy = @Junction(UserCatCrossRef.class)
    )
    private List<AssociatedCats> cats;

    /**
     * Constructor to initialize the UserWithCats entity.
     *
     * @param user The user entity.
     * @param cats The list of associated cats.
     */
    public UserWithCats(User user, List<AssociatedCats> cats) {
        this.user = user;
        this.cats = cats;
    }

    // Getter and setter methods

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AssociatedCats> getCats() {
        return cats;
    }

    public void setCats(List<AssociatedCats> cats) {
        this.cats = cats;
    }
}