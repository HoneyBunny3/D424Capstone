package com.example.d424capstone.database;

import android.app.Application;

import com.example.d424capstone.dao.AssociatedCatsDAO;
import com.example.d424capstone.dao.SocialPostDAO;
import com.example.d424capstone.dao.StoreItemDAO;
import com.example.d424capstone.dao.UserCatCrossRefDAO;
import com.example.d424capstone.dao.UserDAO;
import com.example.d424capstone.entities.CatWithUsers;
import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.entities.UserCatCrossRef;
import com.example.d424capstone.entities.UserWithCats;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class to manage data operations and provide a clean API to the rest of the app.
 */
public class Repository {

    // Data Access Objects
    private UserDAO userDAO;
    private StoreItemDAO storeItemDAO;
    private SocialPostDAO socialPostDAO;
    private AssociatedCatsDAO associatedCatsDAO;
    private UserCatCrossRefDAO userCatCrossRefDAO;

    // Lists to hold data
    private List<User> allUsers;
    private List<StoreItem> allStoreItems;
    private List<SocialPost> allSocialPosts;

    // Define the number of threads for the ExecutorService
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Constructor to initialize the DAOs using the database builder.
     *
     * @param application The application context.
     */
    public Repository(Application application) {
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        userDAO = db.userDAO();
        storeItemDAO = db.storeItemDAO();
        socialPostDAO = db.socialPostDAO();
        associatedCatsDAO = db.associatedCatsDAO();
        userCatCrossRefDAO = db.userCatCrossRefDAO();
    }

    // Methods to retrieve lists of data
    public List<User> getAllUsers() {
        databaseExecutor.execute(() -> allUsers = userDAO.getAllUsers());
        return allUsers;
    }

    public List<StoreItem> getAllStoreItems() {
        databaseExecutor.execute(() -> allStoreItems = storeItemDAO.getAllStoreItems());
        return allStoreItems;
    }

    public List<SocialPost> getAllSocialPosts() {
        databaseExecutor.execute(() -> allSocialPosts = socialPostDAO.getAllSocialPosts());
        return allSocialPosts;
    }

    public List<UserWithCats> getCatsForUser(int userID) {
        return userCatCrossRefDAO.getCatsForUser(userID);
    }

    public List<CatWithUsers> getUsersForCat(int catID) {
        return userCatCrossRefDAO.getUsersForCat(catID);
    }

    // Methods to insert data
    public void insertUser(User user) {
        databaseExecutor.execute(() -> userDAO.insert(user));
    }

    public void insertStoreItem(StoreItem storeItem) {
        databaseExecutor.execute(() -> storeItemDAO.insert(storeItem));
    }

    public void insertSocialPost(SocialPost socialPost) {
        databaseExecutor.execute(() -> socialPostDAO.insert(socialPost));
    }

    public void insertUserCatCrossRef(UserCatCrossRef userCatCrossRef) {
        databaseExecutor.execute(() -> userCatCrossRefDAO.insert(userCatCrossRef));
    }

    // Methods to update data
    public void updateUser(User user) {
        databaseExecutor.execute(() -> userDAO.update(user));
    }

    public void updateStoreItem(StoreItem storeItem) {
        databaseExecutor.execute(() -> storeItemDAO.update(storeItem));
    }

    public void updateSocialPost(SocialPost socialPost) {
        databaseExecutor.execute(() -> socialPostDAO.update(socialPost));
    }

    // Methods to delete data
    public void deleteUser(int userID) {
        databaseExecutor.execute(() -> userDAO.delete(userID));
    }

    public void deleteStoreItem(int storeItemID) {
        databaseExecutor.execute(() -> storeItemDAO.delete(storeItemID));
    }

    public void deleteSocialPost(int socialPostID) {
        databaseExecutor.execute(() -> socialPostDAO.delete(socialPostID));
    }

    // Get featured item and most liked post for home screen
    public StoreItem getFeaturedItem() {
        return storeItemDAO.getFeaturedItem();
    }

    public SocialPost getMostLikedPost() {
        return socialPostDAO.getMostLikedPost();
    }

    // Method to execute tasks asynchronously
    public void executeAsync(Runnable task) {
        databaseExecutor.execute(task);
    }

    // Callback interface for user retrieval
    public interface UserCallback {
        void onUserRetrieved(User user);
    }

    // Method to get user by username asynchronously
    public void getUserByUsernameAsync(String userName, UserCallback callback) {
        databaseExecutor.execute(() -> {
            User user = userDAO.getUserByUsername(userName);
            callback.onUserRetrieved(user);
        });
    }

    // Method to get user by email asynchronously
    public void getUserByEmailAsync(String email, UserCallback callback) {
        databaseExecutor.execute(() -> {
            User user = userDAO.getUserByEmail(email);
            callback.onUserRetrieved(user);
        });
    }
}