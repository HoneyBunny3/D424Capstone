package com.example.d424capstone.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        repository = MyApplication.getInstance().getRepository();
        currentUserID = getCurrentUserID(); // Assuming you have a way to get the current user ID

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSocialPosts();

        FloatingActionButton fabAddPost = findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> openAddPostDialog());

        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSocialPosts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void loadSocialPosts() {
        new Thread(() -> {
            List<SocialPost> socialPosts = repository.getAllSocialPosts();
            runOnUiThread(() -> {
                adapter = new SocialPostAdapter(this, socialPosts, repository, new SocialPostAdapter.OnItemClickListener() {
                    @Override
                    public void onEditClick(SocialPost socialPost) {
                    }

                    @Override
                    public void onDeleteClick(SocialPost socialPost) {
                    }
                }, false, currentUserID); // Pass currentUserID here
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

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

        buttonSavePost.setOnClickListener(v -> {
            String content = editPostContent.getText().toString();
            if (!content.isEmpty()) {
                SocialPost newPost = new SocialPost(0, currentUserID, content, 0); // Use currentUserID
                new Thread(() -> {
                    repository.insertSocialPost(newPost);
                    runOnUiThread(() -> {
                        adapter.addPost(newPost);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        dialog.dismiss();
                    });
                }).start();
            }
        });

        dialog.show();
    }

    private int getCurrentUserID() {
        // Your logic to get the current user ID
        return 1; // Placeholder
    }
}