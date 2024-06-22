package com.example.d424capstone.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.d424capstone.dao.SocialPostDAO;
import com.example.d424capstone.dao.StoreItemDAO;
import com.example.d424capstone.dao.UserDAO;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, SocialPost.class, StoreItem.class}, version = 1, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase{

    // Abstract methods to get the DAOs.
    public abstract UserDAO userDAO();
    public abstract SocialPostDAO socialPostDAO();
    public abstract StoreItemDAO storeItemDAO();

    // Singleton instance to ensure only one instance of the database is created.
    private static volatile DatabaseBuilder INSTANCE;
    // Executor service with a fixed thread pool to handle database operations asynchronously.
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Method to get the singleton instance of the database.
    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    // Create the database with Room database builder, applying fallback strategy and callback.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DatabaseBuilder.class, "hearths_stop_and_shop_database.db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Initialization logic if needed
        }
    };
}
