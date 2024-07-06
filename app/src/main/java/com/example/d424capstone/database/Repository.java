package com.example.d424capstone.database;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.d424capstone.dao.CartItemDAO;
import com.example.d424capstone.dao.CatDAO;
import com.example.d424capstone.dao.SocialPostDAO;
import com.example.d424capstone.dao.StoreItemDAO;
import com.example.d424capstone.dao.UserDAO;
import com.example.d424capstone.entities.CartItem;
import com.example.d424capstone.entities.Cat;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {
    private final UserDAO userDAO;
    private final CatDAO catDAO;
    private final StoreItemDAO storeItemDAO;
    private final CartItemDAO cartItemDAO;
    private final SocialPostDAO socialPostDAO;
    private final SharedPreferences sharedPreferences;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        userDAO = db.userDAO();
        catDAO = db.catDAO();
        storeItemDAO = db.storeItemDAO();
        cartItemDAO = db.cartItemDAO();
        socialPostDAO = db.socialPostDAO();
        sharedPreferences = application.getSharedPreferences("UserPrefs", Application.MODE_PRIVATE);

        populateInitialData(application.getApplicationContext());
        preloadStoreItems();
    }

    private void populateInitialData(Context context) {
        databaseWriteExecutor.execute(() -> {
            if (userDAO.getAllUsers().isEmpty()) {
                Handler handler = new Handler(Looper.getMainLooper());

                User adminUser = new User(0, "Admin", "Fluffy", "fluffy@example.com", "1234567890", "!234Abcd", "ADMIN");
                User premiumUser = new User(0, "Premium", "Shadow", "shadow@example.com", "1234567890", "!234Abcd", "PREMIUM");
                User regularUser = new User(0, "Regular", "Donut", "donut@example.com", "1234567890", "!234Abcd", "REGULAR");

                userDAO.insert(adminUser);
                userDAO.insert(premiumUser);
                userDAO.insert(regularUser);

                Cat adminCat = new Cat(0, "Fox", 3, "", "Playful cat", adminUser.getUserID());
                Cat premiumCat = new Cat(0, "Socks", 3, "", "Friendly cat", premiumUser.getUserID());
                Cat regularCat = new Cat(0, "Clover", 1, "", "Adventurous cat", regularUser.getUserID());

                catDAO.insert(adminCat);
                catDAO.insert(premiumCat);
                catDAO.insert(regularCat);
            }
        });
    }

    private void preloadStoreItems() {
        databaseWriteExecutor.execute(() -> {
            if (storeItemDAO.getAllStoreItems().isEmpty()) {
                storeItemDAO.insert(new StoreItem(0, "Cat Toy", "Fun toy for cats", 9.99, true));
                storeItemDAO.insert(new StoreItem(0, "Cat Bed", "Comfortable bed for cats", 29.99, false));
                storeItemDAO.insert(new StoreItem(0, "Cat Food", "Nutritious food for cats", 19.99, false));
                storeItemDAO.insert(new StoreItem(0, "Cat Scratcher", "Durable scratcher for cats", 14.99, false));
                storeItemDAO.insert(new StoreItem(0, "Cat Litter", "Odor-free cat litter", 10.99, false));
            }
        });
    }

    // User-related methods
    public User getUserByID(int userID) {
        Callable<User> callable = () -> userDAO.getUserByID(userID);
        Future<User> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getUsersForCat(int catID) {
        Callable<List<User>> callable = () -> userDAO.getUsersForCat(catID);
        Future<List<User>> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        Callable<List<User>> callable = () -> userDAO.getAllUsers();
        Future<List<User>> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertUser(User user) {
        databaseWriteExecutor.execute(() -> userDAO.insert(user));
    }

    public void updateUser(User user) {
        databaseWriteExecutor.execute(() -> userDAO.update(user));
    }

    public void deleteUser(int userID) {
        databaseWriteExecutor.execute(() -> {
            User user = userDAO.getUserByID(userID);
            if (user != null) {
                userDAO.delete(user);
            }
        });
    }

    public User getUserByEmail(String email) {
        Callable<User> callable = () -> userDAO.getUserByEmail(email);
        Future<User> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getCurrentUser() {
        int userID = sharedPreferences.getInt("LoggedInUserID", -1);
        return getUserByID(userID);
    }

    // StoreItem-related methods
    public List<StoreItem> getAllStoreItems() {
        Callable<List<StoreItem>> callable = () -> storeItemDAO.getAllStoreItems();
        Future<List<StoreItem>> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertStoreItem(StoreItem storeItem) {
        databaseWriteExecutor.execute(() -> storeItemDAO.insert(storeItem));
    }

    public void updateStoreItem(StoreItem storeItem) {
        databaseWriteExecutor.execute(() -> storeItemDAO.update(storeItem));
    }

    public void deleteStoreItem(int storeItemID) {
        databaseWriteExecutor.execute(() -> storeItemDAO.delete(storeItemID));
    }

    public StoreItem getFeaturedItem() {
        Callable<StoreItem> callable = () -> storeItemDAO.getFeaturedItem();
        Future<StoreItem> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // SocialPost-related methods
    public List<SocialPost> getAllSocialPosts() {
        Callable<List<SocialPost>> callable = () -> socialPostDAO.getAllSocialPosts();
        Future<List<SocialPost>> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertSocialPost(SocialPost socialPost) {
        databaseWriteExecutor.execute(() -> socialPostDAO.insert(socialPost));
    }

    public void updateSocialPost(SocialPost socialPost) {
        databaseWriteExecutor.execute(() -> socialPostDAO.update(socialPost));
    }

    public void deleteSocialPost(int socialPostID) {
        databaseWriteExecutor.execute(() -> socialPostDAO.delete(socialPostID));
    }

    public SocialPost getMostLikedPost() {
        Callable<SocialPost> callable = () -> socialPostDAO.getMostLikedPost();
        Future<SocialPost> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Cat-related methods
    public Cat getCatByID(int catID) {
        Callable<Cat> callable = () -> catDAO.getCatByID(catID);
        Future<Cat> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cat> getCatsForUser(int userID) {
        Callable<List<Cat>> callable = () -> catDAO.getCatsForUser(userID);
        Future<List<Cat>> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insertCat(Cat cat) {
        Callable<Long> callable = () -> catDAO.insert(cat);
        Future<Long> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void updateCat(Cat cat) {
        databaseWriteExecutor.execute(() -> catDAO.update(cat));
    }

    public void deleteCat(int catID) {
        databaseWriteExecutor.execute(() -> {
            Cat cat = catDAO.getCatByID(catID);
            if (cat != null) {
                catDAO.delete(cat);
            }
        });
    }

    // CartItem-related methods
    public List<CartItem> getAllCartItems() {
        Callable<List<CartItem>> callable = () -> cartItemDAO.getAllCartItems();
        Future<List<CartItem>> future = databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertCartItem(CartItem cartItem) {
        databaseWriteExecutor.execute(() -> cartItemDAO.insert(cartItem));
    }

    public void updateCartItem(CartItem cartItem) {
        databaseWriteExecutor.execute(() -> cartItemDAO.update(cartItem));
    }

    public void deleteCartItem(int cartItemID) {
        databaseWriteExecutor.execute(() -> cartItemDAO.delete(cartItemID));
    }
}