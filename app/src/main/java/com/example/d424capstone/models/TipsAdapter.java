package com.example.d424capstone.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.models.Tip;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {

    private List<Tip> tips;

    public TipsAdapter(List<Tip> tips) {
        this.tips = tips;
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
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder {
        TextView tipTitle;
        TextView tipContent;
        TextView tipSource;

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            tipTitle = itemView.findViewById(R.id.tip_title);
            tipContent = itemView.findViewById(R.id.tip_content);
            tipSource = itemView.findViewById(R.id.tip_source);
        }
    }
}