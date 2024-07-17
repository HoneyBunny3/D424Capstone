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

import java.util.List;
import java.util.function.Consumer;

public class CartItemAdapter extends BaseAdapter {

    private Context context;
    private List<CartItem> cartItems;
    private Consumer<List<CartItem>> onCartUpdated;
    private Repository repository;

    public CartItemAdapter(Context context, List<CartItem> cartItems, Consumer<List<CartItem>> onCartUpdated) {
        this.context = context;
        this.cartItems = cartItems;
        this.onCartUpdated = onCartUpdated;
        this.repository = ((MyApplication) context.getApplicationContext()).getRepository();
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cartItems.get(position).getCartItemID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item_list_item, parent, false);
        }

        CartItem cartItem = cartItems.get(position);

        TextView nameTextView = convertView.findViewById(R.id.cartItemNameTextView);
        TextView priceTextView = convertView.findViewById(R.id.cartItemPriceTextView);
        EditText quantityEditText = convertView.findViewById(R.id.cartItemQuantityEditText);
        Button updateButton = convertView.findViewById(R.id.cartItemUpdateButton);
        Button removeButton = convertView.findViewById(R.id.cartItemRemoveButton);

        nameTextView.setText(cartItem.getName());
        priceTextView.setText(String.valueOf(cartItem.getPrice()));
        quantityEditText.setText(String.valueOf(cartItem.getQuantity()));

        updateButton.setOnClickListener(v -> {
            String quantityText = quantityEditText.getText().toString();
            if (!quantityText.isEmpty()) {
                int quantity = Integer.parseInt(quantityText);
                cartItem.setQuantity(quantity);
                repository.updateCartItem(cartItem);
                Toast.makeText(context, "Updated quantity of " + cartItem.getName(), Toast.LENGTH_SHORT).show();
                onCartUpdated.accept(cartItems);
            } else {
                Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            }
        });

        removeButton.setOnClickListener(v -> {
            repository.deleteCartItem(cartItem.getCartItemID());
            cartItems.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Removed " + cartItem.getName() + " from cart", Toast.LENGTH_SHORT).show();
            onCartUpdated.accept(cartItems);
        });

        return convertView;
    }
}