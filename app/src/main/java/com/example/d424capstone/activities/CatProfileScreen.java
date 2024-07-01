package com.example.d424capstone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.adapters.UserAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Cat;
import com.example.d424capstone.entities.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

/**
 * Activity class for displaying and managing the cat profile screen.
 */
public class CatProfileScreen extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Repository repository;
    private TextInputEditText editName, editAge, editBio;
    private ImageView catImageView;
    private Uri catImageUri;
    private Button saveButton, cancelButton;
    private RecyclerView associatedUsersRecyclerView;
    private UserAdapter userAdapter;
    private int catID = -1; // Default value for new cat profiles
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_profile_screen);

        // Initialize activity components
        initViews();

        // Initialize repository
        repository = new Repository(getApplication());

        // Retrieve intent extras
        catID = getIntent().getIntExtra("catID", -1);
        userID = getIntent().getIntExtra("userID", -1);

        // Check if userID is valid
        if (userID == -1) {
            finish();
            return;
        }

        // Load cat profile details if editing an existing cat profile
        if (catID != -1) {
            loadCatDetails();
        }

        // Initialize buttons and set click listeners
        initializeButtons();

        // Initialize RecyclerView
        initializeRecyclerView();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Initialize activity components
    private void initViews() {
        editName = findViewById(R.id.cat_name);
        editAge = findViewById(R.id.cat_age);
        editBio = findViewById(R.id.cat_bio);
        catImageView = findViewById(R.id.cat_image);
        catImageView.setImageResource(R.drawable.baseline_image_search_24); // Set default image
        saveButton = findViewById(R.id.save_cat);
        cancelButton = findViewById(R.id.cancel_cat);
//        associatedUsersRecyclerView = findViewById(R.id.associated_users);
    }

    // Load cat profiles details if editing an existing cat profile
    private void loadCatDetails() {
        if (catID != -1) {
            new Thread(() -> {
                Cat cat = repository.getCatByID(catID);
                if (cat != null) {
                    runOnUiThread(() -> {
                        editName.setText(cat.getCatName());
                        editAge.setText(String.valueOf(cat.getCatAge()));
                        editBio.setText(cat.getCatBio());
                        if (cat.getCatImage() != null && !cat.getCatImage().isEmpty()) {
                            catImageUri = Uri.parse(cat.getCatImage());
                            catImageView.setImageURI(catImageUri);
                        }
                    });
                } else {
                    runOnUiThread(() -> showToast("Error loading cat details"));
                }
            }).start();
        }
    }

    /**
     * Initialize buttons and set click listeners for saving, deleting, and canceling cat profile changes.
     */
    private void initializeButtons() {
        // Initialize the save button
        saveButton.setOnClickListener(view -> saveCatProfile());

        // Initialize the cancel button
        cancelButton.setOnClickListener(view -> finish());

        // Set an OnClickListener to the catImageView to allow picking an image
        catImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    /**
     * Initialize RecyclerView for displaying associated users.
     */
    private void initializeRecyclerView() {
        associatedUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, null);
        associatedUsersRecyclerView.setAdapter(userAdapter);
        loadAssociatedUsers();
    }

    /**
     * Load associated users for the current cat profile.
     */
    private void loadAssociatedUsers() {
        new Thread(() -> {
            List<User> associatedUsers = repository.getUsersForCat(catID);
            runOnUiThread(() -> {
                userAdapter.setUsers(associatedUsers);
            });
        }).start();
    }

    /**
     * Save the cat profile to the database.
     */
    private void saveCatProfile() {
        String catName = editName.getText().toString();
        String catAgeStr = editAge.getText().toString();
        String catBio = editBio.getText().toString();

        if (catName.isEmpty()) {
            showToast("Please enter your cat's name");
            return;
        }

        if (catAgeStr.isEmpty()) {
            showToast("Please enter your cat's age");
            return;
        }

        int catAge = Integer.parseInt(catAgeStr);
        String imageUriString = (catImageUri != null) ? catImageUri.toString() : "android.resource://" + getPackageName() + "/" + R.drawable.baseline_image_search_24;

        // Indicate new cat profile if ID is not set
        if (catID == -1) {
            catID = 0; // Indicate new cat profile
        }

        Cat cat = new Cat(catID, catName, catAge, imageUriString, catBio, userID);

        new Thread(() -> {
            if (catID == 0) {
                // Insert new cat
                long newCatID = repository.insertCat(cat);
                if (newCatID != -1) {
                    catID = (int) newCatID;
                    runOnUiThread(this::finish);
                } else {
                    runOnUiThread(() -> showToast("Error saving cat profile"));
                }
            } else {
                // Update existing cat
                repository.updateCat(cat);
                runOnUiThread(this::finish);
            }
        }).start();
    }

    /**
     * Delete the cat profile from the database.
     */
    private void deleteCatProfile() {
        if (catID != -1) {
            new Thread(() -> {
                repository.deleteCat(catID);
                runOnUiThread(() -> {
                    showToast("Cat profile deleted");
                    finish();
                });
            }).start();
        }
    }

    // Show a toast message
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(CatProfileScreen.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            catImageUri = data.getData();
            catImageView.setImageURI(catImageUri);
        }
    }
}