package com.example.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_libary.R;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private List<String> options;
    private Context context;

    public MoreAdapter(Context context, List<String> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String option = options.get(position);
        holder.optionTextView.setText(option);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView optionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            optionTextView = itemView.findViewById(R.id.optionTextView);
        }
    }
}
