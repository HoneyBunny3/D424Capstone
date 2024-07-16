package com.example.d424capstone.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.SocialPostAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.SocialPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CatSocialScreen extends BaseActivity {
    private Repository repository;
    private SocialPostAdapter adapter;
    private RecyclerView recyclerView;
    private int currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_social_screen);

        repository = MyApplication.getInstance().getRepository(); // Initialize repository instance
        currentUserID = getCurrentUserID(); // Get current user ID

        initializeDrawer(); // Initialize the DrawerLayout and ActionBarDrawerToggle

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager for RecyclerView

        loadSocialPosts(); // Load social posts from the database

        FloatingActionButton fabAddPost = findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> openAddPostDialog()); // Open dialog to add a new post

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu); // Inflate the menu for search functionality

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Set query text listener for the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSocialPosts(query); // Search social posts based on query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    // Load social posts from the repository
    private void loadSocialPosts() {
        new Thread(() -> {
            List<SocialPost> socialPosts = repository.getAllSocialPosts();
            runOnUiThread(() -> {
                // Initialize adapter with the list of social posts
                adapter = new SocialPostAdapter(this, socialPosts, repository, new SocialPostAdapter.OnItemClickListener() {
                    @Override
                    public void onEditClick(SocialPost socialPost) {
                        // No action needed as edit is not allowed on this screen
                    }

                    @Override
                    public void onDeleteClick(SocialPost socialPost) {
                        // No action needed as delete is not allowed on this screen
                    }
                }, false, currentUserID, false); // Pass false for isManagementPage
                recyclerView.setAdapter(adapter); // Set the adapter to the RecyclerView
            });
        }).start();
    }

    // Search social posts based on the query
    private void searchSocialPosts(String query) {
        new Thread(() -> {
            List<SocialPost> searchResults = repository.searchSocialPosts(query);
            runOnUiThread(() -> adapter.updateData(searchResults));
        }).start();
    }

    private void openAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_post, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editPostContent = dialogView.findViewById(R.id.edit_post_content);
        Button buttonSavePost = dialogView.findViewById(R.id.button_save_post);

        // Set click listener for the save button
        buttonSavePost.setOnClickListener(v -> {
            String content = editPostContent.getText().toString();
            if (!content.isEmpty()) {
                SocialPost newPost = new SocialPost(0, currentUserID, content, 0); // Create new social post
                new Thread(() -> {
                    repository.insertSocialPost(newPost); // Insert new post into the repository
                    runOnUiThread(() -> {
                        adapter.addPost(newPost); // Add new post to the adapter
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1); // Scroll to the new post
                        dialog.dismiss(); // Dismiss the dialog
                    });
                }).start();
            }
        });

        dialog.show(); // Show the dialog
    }

    // Get the current user ID (placeholder implementation)
    private int getCurrentUserID() {
        // Your logic to get the current user ID
        return 1; // Placeholder
    }
}