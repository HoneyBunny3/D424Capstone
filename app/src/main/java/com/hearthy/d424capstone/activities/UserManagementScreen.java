package com.hearthy.d424capstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.adapters.UserAdapter;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.User;

import java.util.List;

public class UserManagementScreen extends BaseActivity {
    private Repository repository;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_management_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle
        initViews(); // Initialize UI components

        loadUsers(); // Load the list of users

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRecyclerView = findViewById(R.id.user_list_recyclerview);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.add_user_button).setOnClickListener(view -> {
            Intent intent = new Intent(UserManagementScreen.this, AddEditUserScreen.class);
            startActivityForResult(intent, 1);
        });

        loadUsers();
    }

    // Initialize UI components
    private void initViews() {
        userRecyclerView = findViewById(R.id.user_list_recyclerview);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.add_user_button).setOnClickListener(view -> {
            Intent intent = new Intent(UserManagementScreen.this, AddEditUserScreen.class);
            startActivityForResult(intent, 1);
        });
    }

    // Load the list of users from the repository
    private void loadUsers() {
        List<User> users = repository.getAllUsers();
        userAdapter = new UserAdapter(this, users, this::onEditUser, this::onDeleteUser);
        userRecyclerView.setAdapter(userAdapter);
    }

    // Handle editing a user
    private void onEditUser(User user) {
        Intent intent = new Intent(this, AddEditUserScreen.class);
        intent.putExtra("user_id", user.getUserID());
        startActivityForResult(intent, 1);
    }

    // Handle deleting a user
    private void onDeleteUser(User user) {
        repository.deleteUser(user.getUserID());
        loadUsers();
        Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
    }

    // Handle result from the AddEditUserScreen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadUsers();
        }
    }

    @Override
    protected boolean shouldShowSearch() {
        return false; // Disable the search feature on this activity
    }
}