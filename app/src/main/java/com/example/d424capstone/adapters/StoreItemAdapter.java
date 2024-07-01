package com.example.d424capstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.d424capstone.R;
import com.example.d424capstone.entities.StoreItem;

import java.util.List;

public class StoreItemAdapter extends BaseAdapter {
    private Context context;
    private List<StoreItem> storeItems;

    public StoreItemAdapter(Context context, List<StoreItem> storeItems) {
        this.context = context;
        this.storeItems = storeItems;
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
        TextView priceTextView = convertView.findViewById(R.id.storeItemPriceTextView);

        nameTextView.setText(storeItem.getName());
        priceTextView.setText(String.format("$%.2f", storeItem.getItemPrice()));

        return convertView;
    }
}