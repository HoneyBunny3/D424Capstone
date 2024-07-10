package com.example.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d424capstone.MyApplication;
import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.CartItem;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class StoreItemAdapter extends BaseAdapter {
    private final Repository repository;
    private final Context context;
    private final List<StoreItem> storeItems;
    private final LayoutInflater mInflater;

    public StoreItemAdapter(Context context, List<StoreItem> storeItems) {
        this.repository = ((MyApplication) context.getApplicationContext()).getRepository();
        this.context = context;
        this.storeItems = storeItems;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return storeItems != null ? storeItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return storeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return storeItems.get(position).getStoreItemID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.store_item_list_item, parent, false); // Updated to use the correct layout file
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.storeItemNameTextView);
            holder.descriptionTextView = convertView.findViewById(R.id.storeItemDescriptionTextView);
            holder.priceTextView = convertView.findViewById(R.id.storeItemPriceTextView);
            holder.quantityEditText = convertView.findViewById(R.id.storeItemQuantityEditText);
            holder.addToCartButton = convertView.findViewById(R.id.storeItemAddToCartButton);
            holder.removeButton = convertView.findViewById(R.id.storeItemRemoveButton);
            holder.premiumTextView = convertView.findViewById(R.id.storeItemPremiumTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StoreItem storeItem = storeItems.get(position);
        holder.nameTextView.setText(storeItem.getName());
        holder.descriptionTextView.setText(storeItem.getDescription());
        holder.priceTextView.setText(String.format("$%.2f", storeItem.getItemPrice()));

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
                CartItem cartItem = new CartItem(0, storeItem.getName(), storeItem.getItemPrice(), quantity);
                repository.insertCartItem(cartItem);
                Toast.makeText(context, "Added " + quantity + " " + storeItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
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

        return convertView;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        EditText quantityEditText;
        Button addToCartButton;
        Button removeButton;
        TextView premiumTextView;
    }
}