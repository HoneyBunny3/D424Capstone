package com.example.d424capstone.activities;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.ContactMessageAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.ContactMessage;

import java.util.List;

public class ContactMessagesScreen extends BaseActivity {

    private Repository repository;
    private ContactMessageAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_messages_screen);

        repository = MyApplication.getInstance().getRepository();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            List<ContactMessage> contactMessages = repository.getAllContactMessages();
            runOnUiThread(() -> {
                adapter = new ContactMessageAdapter(this, contactMessages);
                recyclerView.setAdapter(adapter);
            });
        }).start();

        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}