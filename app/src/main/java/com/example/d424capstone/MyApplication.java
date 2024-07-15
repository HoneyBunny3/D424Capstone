package com.example.d424capstone;

import android.app.Application;

import com.example.d424capstone.database.Repository;

public class MyApplication extends Application {

    private static MyApplication instance;
    private Repository repository;
    private int currentUserID; // Add this field to store the current user ID

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        repository = new Repository(this);  // Initialize the repository
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public Repository getRepository() {
        return repository;
    }

    public int getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(int userID) {
        this.currentUserID = userID;
    }
}