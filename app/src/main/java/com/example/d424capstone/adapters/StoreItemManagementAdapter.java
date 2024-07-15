package com.example.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class StoreItemManagementAdapter extends RecyclerView.Adapter<StoreItemManagementAdapter.ViewHolder> {
    private List<StoreItem> storeItems;
    private LayoutInflater inflater;
    private Repository repository;
    private Context context;

    public StoreItemManagementAdapter(Context context, List<StoreItem> storeItems) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.storeItems = storeItems;
        this.repository = ((MyApplication) context.getApplicationContext()).getRepository();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.admin_store_item_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreItem storeItem = storeItems.get(position);
        holder.nameEditText.setText(storeItem.getName());
        holder.descriptionEditText.setText(storeItem.getDescription());
        holder.priceEditText.setText(String.format("$%.2f", storeItem.getItemPrice()));

        // Set visibility for the premium text view
        if (storeItem.isPremium()) {
            holder.premiumTextView.setVisibility(View.VISIBLE);
        } else {
            holder.premiumTextView.setVisibility(View.GONE);
        }

        holder.updateButton.setOnClickListener(v -> {
            String updatedName = holder.nameEditText.getText().toString();
            String updatedDescription = holder.descriptionEditText.getText().toString();
            String priceText = holder.priceEditText.getText().toString().replace("$", "");

            if (priceText.isEmpty()) {
                Toast.makeText(context, "Please enter a price", Toast.LENGTH_SHORT).show();
                return;
            }

            double updatedPrice = Double.parseDouble(priceText);
            if (updatedPrice == 0) {
                Toast.makeText(context, "Price cannot be $0", Toast.LENGTH_SHORT).show();
                return;
            }

            storeItem.setName(updatedName);
            storeItem.setDescription(updatedDescription);
            storeItem.setItemPrice(updatedPrice);
            repository.updateStoreItem(storeItem);
            Toast.makeText(context, "Updated " + storeItem.getName(), Toast.LENGTH_SHORT).show();
        });

        holder.removeButton.setOnClickListener(v -> {
            repository.deleteStoreItem(storeItem.getStoreItemID());
            storeItems.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Removed " + storeItem.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    public void updateData(List<StoreItem> newStoreItems) {
        this.storeItems = newStoreItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText nameEditText;
        EditText descriptionEditText;
        EditText priceEditText;
        Button updateButton;
        Button removeButton;
        TextView premiumTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameEditText = itemView.findViewById(R.id.storeItemNameEditText);
            descriptionEditText = itemView.findViewById(R.id.storeItemDescriptionEditText);
            priceEditText = itemView.findViewById(R.id.storeItemPriceEditText);
            updateButton = itemView.findViewById(R.id.storeItemUpdateButton);
            removeButton = itemView.findViewById(R.id.storeItemRemoveButton);
            premiumTextView = itemView.findViewById(R.id.storeItemPremiumTextView);
        }
    }
}