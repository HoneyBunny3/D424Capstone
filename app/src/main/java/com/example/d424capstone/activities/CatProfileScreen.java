package com.example.d424capstone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.CatAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Cat;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CatProfileScreen extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Repository repository;
    private TextInputEditText editName, editAge, editBio;
    private ImageView catImageView;
    private Uri catImageUri;
    private Button saveButton, cancelButton;
    private RecyclerView catRecyclerView;
    private CatAdapter catAdapter;
    private int catID = -1; // Default value for new cat profiles
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_profile_screen);

        initViews();
        repository = MyApplication.getInstance().getRepository();

        catID = getIntent().getIntExtra("catID", -1);
        userID = getIntent().getIntExtra("userID", -1);

        if (userID == -1) {
            showToast("Invalid user ID");
            finish();
            return;
        }

        if (catID != -1) {
            loadCatDetails();
        }

        initializeButtons();
        initializeRecyclerView();
        loadCatsForUser();

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        editName = findViewById(R.id.cat_name);
        editAge = findViewById(R.id.cat_age);
        editBio = findViewById(R.id.cat_bio);
        catImageView = findViewById(R.id.cat_image);
        catImageView.setImageResource(R.drawable.baseline_image_search_24);
        saveButton = findViewById(R.id.save_cat);
        cancelButton = findViewById(R.id.cancel_cat);
        catRecyclerView = findViewById(R.id.cat_recycler_view);
    }

    private void loadCatDetails() {
        new Thread(() -> {
            Cat cat = repository.getCatByID(catID);
            runOnUiThread(() -> {
                if (cat != null) {
                    editName.setText(cat.getCatName());
                    editAge.setText(String.valueOf(cat.getCatAge()));
                    editBio.setText(cat.getCatBio());
                    if (cat.getCatImage() != null && !cat.getCatImage().isEmpty()) {
                        catImageUri = Uri.parse(cat.getCatImage());
                        catImageView.setImageURI(catImageUri);
                    }
                } else {
                    showToast("Error loading cat details");
                }
            });
        }).start();
    }

    private void initializeButtons() {
        saveButton.setOnClickListener(view -> saveCatProfile());
        cancelButton.setOnClickListener(view -> finish());
        catImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    private void initializeRecyclerView() {
        catRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        catAdapter = new CatAdapter(this, null);
        catRecyclerView.setAdapter(catAdapter);
    }

    private void loadCatsForUser() {
        new Thread(() -> {
            List<Cat> cats = repository.getCatsForUser(userID);
            runOnUiThread(() -> {
                if (cats != null && !cats.isEmpty()) {
                    Log.d("CatProfileScreen", "Loaded cats: " + cats.size());
                    catAdapter.setCats(cats);
                } else {
                    Log.d("CatProfileScreen", "No cats found for user: " + userID);
                    showToast("No cats found for this user.");
                }
            });
        }).start();
    }

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

        if (catID == -1) {
            catID = 0;
        }

        Cat cat = new Cat(catID, catName, catAge, imageUriString, catBio, userID);

        new Thread(() -> {
            if (catID == 0) {
                long newCatID = repository.insertCat(cat);
                if (newCatID != -1) {
                    catID = (int) newCatID;
                    runOnUiThread(this::finish);
                } else {
                    runOnUiThread(() -> showToast("Error saving cat profile"));
                }
            } else {
                repository.updateCat(cat);
                runOnUiThread(this::finish);
            }
        }).start();
    }

    private void deleteCatProfile() {
        new Thread(() -> {
            repository.deleteCat(catID);
            runOnUiThread(() -> {
                showToast("Cat profile deleted");
                finish();
            });
        }).start();
    }

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

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}