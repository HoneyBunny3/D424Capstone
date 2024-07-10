package com.example.d424capstone.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.d424capstone.dao.CartItemDAO;
import com.example.d424capstone.dao.CatDAO;
import com.example.d424capstone.dao.OrderDAO;
import com.example.d424capstone.dao.PremiumStoreItemDAO;
import com.example.d424capstone.dao.SocialPostDAO;
import com.example.d424capstone.dao.StoreItemDAO;
import com.example.d424capstone.dao.UserDAO;
import com.example.d424capstone.entities.CartItem;
import com.example.d424capstone.entities.Cat;
import com.example.d424capstone.entities.Order;
import com.example.d424capstone.entities.PremiumStoreItem;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Cat.class, StoreItem.class, CartItem.class, Order.class, SocialPost.class, PremiumStoreItem.class}, version = 4)
@TypeConverters({com.example.d424capstone.database.Converters.class})
public abstract class DatabaseBuilder extends RoomDatabase {

    public abstract UserDAO userDAO();
    public abstract CatDAO catDAO();
    public abstract StoreItemDAO storeItemDAO();
    public abstract CartItemDAO cartItemDAO();
    public abstract OrderDAO orderDAO();
    public abstract SocialPostDAO socialPostDAO();
    public abstract PremiumStoreItemDAO premiumStoreItemDAO();

    private static volatile DatabaseBuilder INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DatabaseBuilder.class, "CapstoneDatabase.db")
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
            // Initial data population logic can be added here if needed
        }
    };
}