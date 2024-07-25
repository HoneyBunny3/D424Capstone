package com.hearthy.d424capstone.adapters;

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

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.activities.CatDetails;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.Cat;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    private List<Cat> cats;
    private final Context context;
    private final LayoutInflater mInflater;
    private final Repository repository;

    public CatAdapter(Context context, List<Cat> cats) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.cats = cats;
        this.repository = MyApplication.getInstance().getRepository();
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cat_item, parent, false);
        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        if (cats != null) {
            Cat current = cats.get(position);
            holder.catItemView.setText(current.getName());
        } else {
            holder.catItemView.setText("No cat profile found");
        }
    }

    @Override
    public int getItemCount() {
        return (cats != null) ? cats.size() : 0;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
        notifyDataSetChanged();
    }

    class CatViewHolder extends RecyclerView.ViewHolder {
        private final TextView catItemView;
        private final Button deleteButton;

        private CatViewHolder(View itemView) {
            super(itemView);
            catItemView = itemView.findViewById(R.id.cat_name);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Cat current = cats.get(position);
                Intent intent = new Intent(context, CatDetails.class); // Navigate to CatDetails
                intent.putExtra("catID", current.getCatID());
                intent.putExtra("userID", current.getUserID());
                context.startActivity(intent);
            });

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