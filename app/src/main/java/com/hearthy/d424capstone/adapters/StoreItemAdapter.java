package com.hearthy.d424capstone.adapters;

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

import com.hearthy.d424capstone.MyApplication;
import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.database.Repository;
import com.hearthy.d424capstone.entities.CartItem;
import com.hearthy.d424capstone.entities.StoreItem;

import java.util.List;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ViewHolder> {
    private final Repository repository;
    private final Context context;
    private final List<StoreItem> storeItems;

    public StoreItemAdapter(Context context, List<StoreItem> storeItems) {
        this.repository = ((MyApplication) context.getApplicationContext()).getRepository();
        this.context = context;
        this.storeItems = storeItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_item_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreItem storeItem = storeItems.get(position);
        holder.nameTextView.setText(storeItem.getName());
        holder.descriptionTextView.setText(storeItem.getDescription());
        holder.priceTextView.setText(String.format("$%.2f", storeItem.getPrice()));

        // Set visibility for the premium text view
        if (storeItem.isPremium()) {
            holder.premiumTextView.setVisibility(View.VISIBLE);
        } else {
            holder.premiumTextView.setVisibility(View.GONE);
        }

        holder.addToCartButton.setOnClickListener(v -> {
            String quantityText = holder.quantityEditText.getText().toString();
            if (!quantityText.isEmpty()) {
                int quantity = Integer.parseInt(quantityText);
                if (storeItem.getPrice() == 0) {
                    Toast.makeText(context, "Items with a price of $0 cannot be added to the cart", Toast.LENGTH_SHORT).show();
                } else {
                    CartItem cartItem = new CartItem(0, storeItem.getName(), storeItem.getPrice(), quantity);
                    repository.insertCartItem(cartItem);
                    Toast.makeText(context, "Added " + quantity + " " + storeItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            repository.deleteStoreItem(storeItem.getStoreItemID());
            storeItems.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Removed " + storeItem.getName() + " from store", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return storeItems != null ? storeItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        EditText quantityEditText;
        Button addToCartButton;
        Button removeButton;
        TextView premiumTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.storeItemNameTextView);
            descriptionTextView = itemView.findViewById(R.id.storeItemDescriptionTextView);
            priceTextView = itemView.findViewById(R.id.storeItemPriceTextView);
            quantityEditText = itemView.findViewById(R.id.storeItemQuantityEditText);
            addToCartButton = itemView.findViewById(R.id.storeItemAddToCartButton);
            removeButton = itemView.findViewById(R.id.storeItemRemoveButton);
            premiumTextView = itemView.findViewById(R.id.storeItemPremiumTextView);
        }
    }
}