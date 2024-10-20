package com.example.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.OnClickContracts;
import com.example.btl_libary.R;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private final List<String> options;
    private final Context context;
    private OnClickContracts.OnItemClickListener listener;

    public MoreAdapter(Context context, List<String> options) {
        this.context = context;
        this.options = options;
    }

    public void setOnItemClickListener(OnClickContracts.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String option = options.get(position);
        holder.optionTextView.setText(option);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
              listener.onItemClick((View) v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options != null ? options.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView optionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            optionTextView = itemView.findViewById(R.id.optionTextView);
        }
    }
}
