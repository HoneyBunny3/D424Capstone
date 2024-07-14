package com.example.d424capstone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.UserAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.User;

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

        repository = MyApplication.getInstance().getRepository(); // Use repository from MyApplication

        // Initialize the DrawerLayout and ActionBarDrawerToggle
        initializeDrawer();

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

    private void loadUsers() {
        List<User> users = repository.getAllUsers();
        userAdapter = new UserAdapter(this, users, this::onEditUser, this::onDeleteUser);
        userRecyclerView.setAdapter(userAdapter);
    }

    private void onEditUser(User user) {
        Intent intent = new Intent(this, AddEditUserScreen.class);
        intent.putExtra("user_id", user.getUserID());
        startActivityForResult(intent, 1);
    }

    private void onDeleteUser(User user) {
        repository.deleteUser(user.getUserID());
        loadUsers();
        Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
    }

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