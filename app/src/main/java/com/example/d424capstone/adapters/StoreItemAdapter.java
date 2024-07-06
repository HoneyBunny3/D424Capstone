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

    private Context context;
    private List<StoreItem> storeItems;
    private Repository repository;

    public StoreItemAdapter(Context context, List<StoreItem> storeItems) {
        this.context = context;
        this.storeItems = storeItems;
        this.repository = ((MyApplication) context.getApplicationContext()).getRepository();
    }

    @Override
    public int getCount() {
        return storeItems.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.store_item_list_item, parent, false);
        }

        StoreItem storeItem = storeItems.get(position);

        TextView nameTextView = convertView.findViewById(R.id.storeItemNameTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.storeItemDescriptionTextView);
        TextView priceTextView = convertView.findViewById(R.id.storeItemPriceTextView);
        EditText quantityEditText = convertView.findViewById(R.id.storeItemQuantityEditText);
        Button addToCartButton = convertView.findViewById(R.id.storeItemAddToCartButton);
        Button removeButton = convertView.findViewById(R.id.storeItemRemoveButton);

        nameTextView.setText(storeItem.getName());
        descriptionTextView.setText(storeItem.getDescription());
        priceTextView.setText(String.valueOf(storeItem.getItemPrice()));

        addToCartButton.setOnClickListener(v -> {
            String quantityText = quantityEditText.getText().toString();
            if (!quantityText.isEmpty()) {
                int quantity = Integer.parseInt(quantityText);
                CartItem cartItem = new CartItem(0, storeItem.getName(), storeItem.getItemPrice(), quantity);
                repository.insertCartItem(cartItem);
                Toast.makeText(context, "Added " + quantity + " " + storeItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            }
        });

        removeButton.setOnClickListener(v -> {
            repository.deleteStoreItem(storeItem.getStoreItemID());
            storeItems.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Removed " + storeItem.getName() + " from store", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }
}