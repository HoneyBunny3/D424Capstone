package com.example.d424capstone.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.adapters.CatAdapter;
import com.example.d424capstone.adapters.ContactMessageAdapter;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.ContactMessage;

import java.util.List;

public class ContactMessagesScreen extends BaseActivity {

    private Repository repository;
    private ContactMessageAdapter contactMessageAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_messages_screen);

        initViews();
        repository = MyApplication.getInstance().getRepository();

        loadContactMessages();

        initializeDrawer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_contact_messages); // Ensure ID matches XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactMessageAdapter = new ContactMessageAdapter(this, null);
        recyclerView.setAdapter(contactMessageAdapter);
    }

    private void loadContactMessages() {
        new Thread(() -> {
            List<ContactMessage> contactMessages = repository.getAllContactMessages();
            runOnUiThread(() -> {
                if (contactMessages != null && !contactMessages.isEmpty()) {
                    contactMessageAdapter.setContactMessages(contactMessages);
                } else {
                    showToast("No contact messages found");
                }
            });
        }).start();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ContactMessagesScreen.this, message, Toast.LENGTH_LONG).show());
    }
}