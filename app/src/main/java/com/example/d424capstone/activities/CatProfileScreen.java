package com.example.d424capstone.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.AssociatedCats;
import com.example.d424capstone.entities.User;
import com.example.d424capstone.entities.UserCatCrossRef;
import com.example.d424capstone.entities.UserWithCats;

import java.util.ArrayList;
import java.util.List;

public class CatProfileScreen extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Repository repository;
    private EditText catNameEditText, catAgeEditText, catBioEditText;
    private ImageView catImageView;
    private Uri catImageUri;
    private Button saveButton, cancelButton, addUserButton;
    private TextView associatedUsersTextView, associatedCatsTextView;
    private List<User> associatedUsers;
    private List<User> allUsers;
    private List<AssociatedCats> associatedCats;
    private int catID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_profile_screen);

        repository = new Repository(getApplication());
        associatedUsers = new ArrayList<>();
        allUsers = new ArrayList<>();
        associatedCats = new ArrayList<>();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        // Initialize views
        initializeViews();

        // Initialize buttons and set click listeners
        initializeButtons();

        // Load all users from the database
        loadAllUsers();

        // Set window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if editing an existing cat profile
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("catID")) {
            catID = intent.getIntExtra("catID", -1);
            if (catID != -1) {
                loadCatProfile(catID);
            }
        }
    }

    private void initializeViews() {
        catNameEditText = findViewById(R.id.cat_name);
        catAgeEditText = findViewById(R.id.cat_age);
        catBioEditText = findViewById(R.id.cat_bio);
        catImageView = findViewById(R.id.cat_image);
        catImageView.setImageResource(R.drawable.baseline_image_search_24); // Set default image
        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);
        addUserButton = findViewById(R.id.add_user);
        associatedUsersTextView = findViewById(R.id.associated_users);
        associatedCatsTextView = findViewById(R.id.associated_cats);
    }

    private void initializeButtons() {
        // Initialize the save button
        saveButton.setOnClickListener(view -> saveCatProfile());

        // Initialize the cancel button
        cancelButton.setOnClickListener(view -> finish());

        // Initialize the add user button
        addUserButton.setOnClickListener(view -> addUserToCatProfile());

        // Set an OnClickListener to the catImageView to allow picking an image
        catImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    private void saveCatProfile() {
        String catName = catNameEditText.getText().toString();
        String catAgeStr = catAgeEditText.getText().toString();
        String catBio = catBioEditText.getText().toString();

        if (catName.isEmpty() || catAgeStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int catAge = Integer.parseInt(catAgeStr);
        String imageUriString = (catImageUri != null) ? catImageUri.toString() : "android.resource://" + getPackageName() + "/" + R.drawable.baseline_image_search_24;

        if (catID == -1) {
            // New cat profile
            AssociatedCats cat = new AssociatedCats(0, catName, catAge, imageUriString, catBio);

            repository.insertCat(cat, id -> {
                if (id != -1) {
                    int loggedInUserId = getLoggedInUserId(); // Retrieve the logged-in user's ID
                    UserCatCrossRef crossRef = new UserCatCrossRef(loggedInUserId, (int) id);
                    repository.insertUserCatCrossRef(crossRef);

                    runOnUiThread(() -> {
                        Toast.makeText(CatProfileScreen.this, "Cat profile saved and associated with user", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        } else {
            // Update existing cat profile
            AssociatedCats cat = new AssociatedCats(catID, catName, catAge, imageUriString, catBio);
            repository.updateCat(cat);
            runOnUiThread(() -> {
                Toast.makeText(CatProfileScreen.this, "Cat profile updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    private void addUserToCatProfile() {
        if (allUsers == null || allUsers.isEmpty()) {
            Toast.makeText(this, "No users available to add", Toast.LENGTH_SHORT).show();
            return;
        }

        CharSequence[] userNames = new CharSequence[allUsers.size()];
        boolean[] checkedItems = new boolean[allUsers.size()];

        for (int i = 0; i < allUsers.size(); i++) {
            userNames[i] = allUsers.get(i).getUserName();
            checkedItems[i] = associatedUsers.contains(allUsers.get(i));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Users")
                .setMultiChoiceItems(userNames, checkedItems, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        associatedUsers.add(allUsers.get(which));
                    } else {
                        associatedUsers.remove(allUsers.get(which));
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    updateAssociatedUsersTextView();
                    loadAssociatedCats();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateAssociatedUsersTextView() {
        StringBuilder sb = new StringBuilder("Associated Users:\n");
        for (User user : associatedUsers) {
            sb.append(user.getUserName()).append("\n");
        }
        associatedUsersTextView.setText(sb.toString());
    }

    private void updateAssociatedCatsTextView() {
        StringBuilder sb = new StringBuilder("Associated Cats:\n");
        for (AssociatedCats cat : associatedCats) {
            sb.append(cat.getCatName()).append("\n");
        }
        associatedCatsTextView.setText(sb.toString());
    }

    private void loadAllUsers() {
        repository.executeAsync(() -> {
            allUsers = repository.getAllUsers();
            runOnUiThread(this::updateAssociatedUsersTextView);
        });
    }

    private void loadAssociatedCats() {
        associatedCats.clear();
        for (User user : associatedUsers) {
            List<UserWithCats> userWithCatsList = repository.getCatsForUser(user.getUserID());
            for (UserWithCats userWithCats : userWithCatsList) {
                associatedCats.addAll(userWithCats.getCats());
            }
        }
        runOnUiThread(this::updateAssociatedCatsTextView);
    }

    private void loadCatProfile(int catID) {
        repository.executeAsync(() -> {
            AssociatedCats cat = repository.getCatByID(catID);
            if (cat != null) {
                runOnUiThread(() -> {
                    catNameEditText.setText(cat.getCatName());
                    catAgeEditText.setText(String.valueOf(cat.getCatAge()));
                    catBioEditText.setText(cat.getCatBio());
                    catImageUri = Uri.parse(cat.getImageUri());
                    catImageView.setImageURI(catImageUri);
                });
            }
        });
    }

//    private int getLoggedInUserId() {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        return sharedPreferences.getInt("LoggedInUserID", -1);
//    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("LoggedInUserID", -1);
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