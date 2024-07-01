package com.example.d424capstone.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.activities.CatProfileScreen;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.Cat;

import java.util.List;

/**
 * Adapter class for displaying and managing cat items in a RecyclerView.
 */
public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    private List<Cat> cats; // List of cats to display
    private final Context context; // Context of the parent activity
    private final LayoutInflater mInflater; // LayoutInflater to inflate the cat list items
    private final Repository repository; // Repository for data operations

    /**
     * Constructor for the CatAdapter.
     *
     * @param context      The context of the parent activity.
     * @param application  The application context.
     * @param cats         The list of cats to display.
     */

    public CatAdapter(Context context, Application application, List<Cat> cats) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.cats = cats;
        this.repository = new Repository(application);
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual cat list items.
        View itemView = mInflater.inflate(R.layout.cat_item, parent, false);
        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        if (cats != null) {
            // Get the current Cat and set its name in the TextView.
            Cat current = cats.get(position);
            holder.catItemView.setText(current.getCatName());
        } else {
            // If there are no cats, display a placeholder text.
            holder.catItemView.setText("No cat profile found");
        }
    }

    @Override
    public int getItemCount() {
        return (cats != null) ? cats.size() : 0;
    }

    /**
     * Updates the list of cats and notifies the adapter of the data change.
     *
     * @param cats The updated list of cats.
     */
    public void setCats(List<Cat> cats) {
        this.cats = cats;
        notifyDataSetChanged();
    }

    /**
     * Retrieves the list of cats.
     *
     * @return The list of cats.
     */
    public List<Cat> getCats() {
        return cats;
    }

    /**
     * ViewHolder class for managing individual cat items in the RecyclerView.
     */
    class CatViewHolder extends RecyclerView.ViewHolder {
        private final TextView catItemView; // TextView to display the cat name.
        private final Button deleteButton; // Button to delete the cat.

        /**
         * Constructor for the CatViewHolder.
         *
         * @param itemView The view for the individual cat item.
         */
        private CatViewHolder(View itemView) {
            super(itemView);
            catItemView = itemView.findViewById(R.id.cat_name);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            // Set an OnClickListener to handle item clicks.
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Cat current = cats.get(position);
                // Create an Intent to start the CatProfileScreen activity.
                Intent intent = new Intent(context, CatProfileScreen.class);
                intent.putExtra("catID", current.getCatID());
                intent.putExtra("userID", current.getUserID());  // Pass userID to the CatProfileScreen
                context.startActivity(intent);
            });

            // Set an OnClickListener to handle delete button clicks.
            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Cat current = cats.get(position);
                new Thread(() -> {
                    repository.deleteCat(current.getCatID());
                    ((AppCompatActivity) context).runOnUiThread(() -> {
                        cats.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Cat profile deleted", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            });
        }
    }
}