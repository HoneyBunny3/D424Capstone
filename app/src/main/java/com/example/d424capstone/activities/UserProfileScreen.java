package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.AssociatedCats;
import com.example.d424capstone.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class for displaying and managing the user profile screen.
 */
public class UserProfileScreen extends BaseActivity {

    // Repository instance for data operations
    private Repository repository;
    // UI components for displaying user information and associated cats
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private ListView associatedCatsListView;

    // Lists to hold associated cats and their names
    private List<AssociatedCats> associatedCatsList;
    private ArrayAdapter<String> catsAdapter;
    private List<String> catNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_screen);

        // Initialize repository and lists
        repository = new Repository(getApplication());
        associatedCatsList = new ArrayList<>();
        catNames = new ArrayList<>();

        // Initialize UI components
        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        emailTextView = findViewById(R.id.email_address);
        associatedCatsListView = findViewById(R.id.associated_cats_list_view);

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Initialize buttons and set click listeners
        initializeButtons();

        // Load user profile and associated cats
        loadUserProfileAndCats();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set item click listener for associated cats list view
        associatedCatsListView.setOnItemClickListener((parent, view, position, id) -> {
            AssociatedCats selectedCat = associatedCatsList.get(position);
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            intent.putExtra("catID", selectedCat.getCatID());
            startActivity(intent);
        });

        // Set up adapter for associated cats list view
        catsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, catNames);
        associatedCatsListView.setAdapter(catsAdapter);
    }

    /**
     * Initialize buttons and set click listeners for navigation.
     */
    private void initializeButtons() {
        // Initialize the button that navigates to the Shopping screen
        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Social screen
        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileScreen.this, CatSocialScreen.class);
            startActivity(intent);
        });

        // Initialize the button that navigates to the Cat Profile screen
        Button buttonCatProfile = findViewById(R.id.tocatprofilescreen);
        buttonCatProfile.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            startActivity(intent);
        });
    }

    /**
     * Load user profile and associated cats from the database.
     */
    private void loadUserProfileAndCats() {
        int userID = getUserID();
        Log.d("UserProfileScreen", "Loading user profile for userID: " + userID);

        if (userID == -1) {
            Log.e("UserProfileScreen", "No user ID found in SharedPreferences");
            return;
        }

        // Fetch user details asynchronously
        repository.getUserByIDAsync(userID, new Repository.UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                if (user != null) {
                    runOnUiThread(() -> {
                        Log.d("UserProfileScreen", "User retrieved: " + user.getFirstName() + " " + user.getLastName());
                        firstNameTextView.setText(user.getFirstName());
                        lastNameTextView.setText(user.getLastName());
                        emailTextView.setText(user.getEmail());
                    });
                    // Load associated cats after user data is loaded
                    loadAssociatedCats(userID);
                } else {
                    Log.e("UserProfileScreen", "User not found with ID: " + userID);
                }
            }
        });
    }

    /**
     * Load associated cats for the specified user from the database.
     *
     * @param userID The ID of the user whose associated cats are to be loaded.
     */
    private void loadAssociatedCats(int userID) {
        Log.d("UserProfileScreen", "Loading associated cats for userID: " + userID);

        repository.executeAsync(() -> {
            List<AssociatedCats> cats = repository.getCatsForUser(userID);
            runOnUiThread(() -> {
                if (cats != null) {
                    catNames.clear();
                    associatedCatsList.clear();
                    for (AssociatedCats cat : cats) {
                        catNames.add(cat.getCatName());
                        associatedCatsList.add(cat);
                    }
                    catsAdapter.notifyDataSetChanged();
                } else {
                    Log.e("UserProfileScreen", "No associated cats found for userID: " + userID);
                }
            });
        });
    }

    /**
     * Get the user ID of the currently logged-in user from SharedPreferences.
     *
     * @return The ID of the logged-in user.
     */
    private int getUserID() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("LoggedInUserID", -1);
    }
}