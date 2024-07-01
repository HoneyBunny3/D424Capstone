package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
    private RecyclerView catRecyclerView;
    private CatAdapter catAdapter;
    private List<Cat> catList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_screen);

        initViews();
        setupRecyclerView();

        repository = new Repository(getApplication());

        initializeDrawer();
        initializeButtons();

        loadUserProfileAndCats();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        catAdapter.setOnItemClickListener(cat -> {
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            intent.putExtra("catID", cat.getCatID());
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
    }

    private void initializeButtons() {
        Button buttonShopping = findViewById(R.id.toshoppingscreen);
        buttonShopping.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileScreen.this, ShoppingScreen.class);
            startActivity(intent);
        });

        Button buttonSocial = findViewById(R.id.tocatsocialscreen);
        buttonSocial.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileScreen.this, CatSocialScreen.class);
            startActivity(intent);
        });

        Button buttonCatProfile = findViewById(R.id.tocatprofilescreen);
        buttonCatProfile.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileScreen.this, CatProfileScreen.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssociatedCats();
    }

    protected void initViews() {
        editFirstName = findViewById(R.id.edit_first_name);
        editLastName = findViewById(R.id.edit_last_name);
        editEmailAddress = findViewById(R.id.edit_email_address);
        editPhoneNumber = findViewById(R.id.edit_phone_number);
        catRecyclerView = findViewById(R.id.catRecyclerView);
    }

    private void setupRecyclerView() {
        catAdapter = new CatAdapter(this, getApplication(), catList);
        catRecyclerView.setAdapter(catAdapter);
        catRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadUserProfileAndCats() {
        userID = getUserID();

        if (userID == -1) {
            return;
        }

        new Thread(() -> {
            User user = repository.getUserByID(userID);
            if (user != null) {
                runOnUiThread(() -> {
                    editFirstName.setText(user.getFirstName());
                    editLastName.setText(user.getLastName());
                    editEmailAddress.setText(user.getEmail());
                    editPhoneNumber.setText(user.getPhone());
                    loadAssociatedCats();
                });
            }
        }).start();
    }

    private void loadAssociatedCats() {
        new Thread(() -> {
            List<Cat> associatedCats = repository.getCatsForUser(userID);
            if (associatedCats != null) {
                runOnUiThread(() -> {
                    catList.clear();
                    catList.addAll(associatedCats);
                    catAdapter.notifyDataSetChanged();
                });
            }
        }).start();
    }

    private int getUserID() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("LoggedInUserID", -1);
    }
}