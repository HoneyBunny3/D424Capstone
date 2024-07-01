package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.adapters.CatAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Cat;
import com.example.d424capstone.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserProfileScreen extends BaseActivity {

    private EditText editFirstName, editLastName, editEmailAddress, editPhoneNumber;
    private Repository repository;
    private int userID = -1;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private ListView catListView;
    private ArrayAdapter<String> catAdapter;
    private List<Cat> catList = new ArrayList<>();
    private List<String> catNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_screen);

        // Initialize UI components
        initViews();
        setupRecyclerView();

        // Initialize repository and lists
        repository = new Repository(getApplication());
        catList = new ArrayList<>();
        catNames = new ArrayList<>();

        // Initialize UI components
        firstNameTextView = findViewById(R.id.edit_first_name);
        lastNameTextView = findViewById(R.id.edit_last_name);
        emailTextView = findViewById(R.id.edit_email_address);
        phoneTextView = findViewById(R.id.edit_phone_number);
        catListView = findViewById(R.id.catRecyclerView);

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

        // Load associated cat profiles
        loadAssociatedCats();

        // Set item click listener for associated cats list view
        catListView.setOnItemClickListener((parent, view, position, id) -> {
            Cat selectedCat = catList.get(position);
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            intent.putExtra("catID", selectedCat.getCatID());
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        // Set up adapter for associated cats list view
        catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, catNames);
        catListView.setAdapter(catAdapter);
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        loadAssociatedCats(); // Reload cat profiles when activity is resumed
    }

    protected void initViews() {
        editFirstName = findViewById(R.id.edit_first_name);
        editLastName = findViewById(R.id.edit_last_name);
        editEmailAddress = findViewById(R.id.edit_email_address);
        editPhoneNumber = findViewById(R.id.edit_phone_number);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.catRecyclerView);
        CatAdapter catRecyclerViewAdapter = new CatAdapter(this, getApplication(), catList);
        recyclerView.setAdapter(catRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadUserDetails() {
        userID = getIntent().getIntExtra("userID", -1);
        editFirstName.setText(getIntent().getStringExtra("firstName"));
        editLastName.setText(getIntent().getStringExtra("lastName"));
        editEmailAddress.setText(getIntent().getStringExtra("emailAddress"));
        editPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
    }

    private void loadAssociatedCats() {
        new Thread(() -> {
            List<Cat> associatedCats = repository.getCatsForUser(userID);
            runOnUiThread(() -> {
                catList.clear();
                catList.addAll(associatedCats);
                catAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void addNewCat() {
        Intent intent = new Intent(this, CatProfileScreen.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void loadUserProfileAndCats() {
        int userID = getUserID();
        Toast.makeText(this, "Loading user profile for userID: " + userID, Toast.LENGTH_SHORT).show();

        if (userID == -1) {
            Toast.makeText(this, "No user ID found in SharedPreferences", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User user = repository.getUserByID(userID);
            runOnUiThread(() -> {
                if (user != null) {
                    Toast.makeText(this, "User retrieved: " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();
                    firstNameTextView.setText(user.getFirstName());
                    lastNameTextView.setText(user.getLastName());
                    emailTextView.setText(user.getEmail());
                    phoneTextView.setText(user.getPhoneNumber());
                    loadCat(userID);
                } else {
                    Toast.makeText(this, "User not found with ID: " + userID, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void loadCat(int userID) {
        Toast.makeText(this, "Loading associated cats for userID: " + userID, Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            List<Cat> cats = repository.getCatsForUser(userID);
            runOnUiThread(() -> {
                if (cats != null) {
                    catNames.clear();
                    catList.clear();
                    for (Cat cat : cats) {
                        Toast.makeText(this, "Loaded cat: " + cat.getCatName() + " for userID: " + userID, Toast.LENGTH_SHORT).show();
                        catNames.add(cat.getCatName());
                        catList.add(cat);
                    }
                    catAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "No associated cats found for userID: " + userID, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private int getUserID() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("LoggedInUserID", -1);
        Toast.makeText(this, "Retrieved LoggedInUserID: " + userId, Toast.LENGTH_SHORT).show();
        return userId;
    }
}