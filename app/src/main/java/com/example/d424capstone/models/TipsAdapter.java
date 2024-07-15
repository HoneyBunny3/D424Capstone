package com.example.d424capstone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.entities.Tip;

import java.util.List;
import java.util.function.Consumer;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {

    private List<Tip> tips;
    private Consumer<Tip> editTipConsumer;
    private Consumer<Tip> deleteTipConsumer;

    public TipsAdapter(List<Tip> tips, Consumer<Tip> editTipConsumer, Consumer<Tip> deleteTipConsumer) {
        this.tips = tips;
        this.editTipConsumer = editTipConsumer;
        this.deleteTipConsumer = deleteTipConsumer;
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new TipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        Tip tip = tips.get(position);
        holder.tipTitle.setText(tip.getTitle());
        holder.tipContent.setText(tip.getContent());
        holder.tipSource.setText(tip.getSource());

        holder.editButton.setOnClickListener(v -> editTipConsumer.accept(tip));
        holder.deleteButton.setOnClickListener(v -> deleteTipConsumer.accept(tip));
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

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            tipTitle = itemView.findViewById(R.id.tip_title);
            tipContent = itemView.findViewById(R.id.tip_content);
            tipSource = itemView.findViewById(R.id.tip_source);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}