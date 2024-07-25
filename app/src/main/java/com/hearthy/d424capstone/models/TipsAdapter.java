package com.hearthy.d424capstone.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hearthy.d424capstone.R;
import com.hearthy.d424capstone.entities.Tip;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {
    private List<Tip> tips;
    private OnTipInteractionListener listener;
    private boolean showButtons; // Flag to control the visibility of buttons

    public TipsAdapter(List<Tip> tips, OnTipInteractionListener listener, boolean showButtons) {
        this.tips = tips;
        this.listener = listener;
        this.showButtons = showButtons; // Initialize the flag
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new TipsViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        Tip tip = tips.get(position);
        holder.tipTitle.setText(tip.getTitle());
        holder.tipContent.setText(tip.getContent());
        holder.tipSource.setText(tip.getSource());

        if (showButtons) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(v -> listener.onEditTip(position));
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteTip(position));
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder {
        TextView tipTitle;
        TextView tipContent;
        TextView tipSource;
        Button editButton;
        Button deleteButton;

        public TipsViewHolder(@NonNull View itemView, OnTipInteractionListener listener) {
            super(itemView);
            tipTitle = itemView.findViewById(R.id.tip_title);
            tipContent = itemView.findViewById(R.id.tip_content);
            tipSource = itemView.findViewById(R.id.tip_source);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnTipInteractionListener {
        void onEditTip(int position);
        void onDeleteTip(int position);
    }
}