package com.example.d424capstone.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

/**
 * Represents a Cat with a list of associated Users in the database.
 */
public class CatWithUsers {
    @Embedded
    private AssociatedCats cat;

    @Relation(
            parentColumn = "catID",
            entityColumn = "userID",
            associateBy = @Junction(UserCatCrossRef.class)
    )
    private List<User> users;

    /**
     * Constructor to initialize the CatWithUsers entity.
     *
     * @param cat   The cat entity.
     * @param users The list of associated users.
     */
    public CatWithUsers(AssociatedCats cat, List<User> users) {
        this.cat = cat;
        this.users = users;
    }

    // Getter and setter methods

    public AssociatedCats getCat() {
        return cat;
    }

    public void setCat(AssociatedCats cat) {
        this.cat = cat;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}