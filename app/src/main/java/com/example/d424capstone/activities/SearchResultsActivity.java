package com.example.d424capstone.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.adapters.StoreItemAdapter;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    private Repository repository;
    private RecyclerView recyclerView;
    private StoreItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        repository = new Repository(getApplication()); // Initialize repository instance

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String query = getIntent().getStringExtra("QUERY");
        List<StoreItem> searchResults = repository.searchStoreItems(query);

        adapter = new StoreItemAdapter(this, searchResults);  // Pass the context and searchResults
        recyclerView.setAdapter(adapter);
    }
}