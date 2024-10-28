package com.example.Views.Adapters;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_libary.R;

import java.util.List;

public class BookContentAdapter extends RecyclerView.Adapter<BookContentAdapter.BookViewHolder> {
    private List<String> text;
    private float textSize;


    public BookContentAdapter(List<String> text, float textSize) {
        this.text = text;
        this.textSize = textSize;
    }

    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_content, parent, false);
        return new BookViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        String modifiedText = text.get(position).replace(" ", "  ");
        holder.textViewContent.setText(modifiedText);
        holder.textViewContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
            
        public BookViewHolder(View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);

        }
    }
}
