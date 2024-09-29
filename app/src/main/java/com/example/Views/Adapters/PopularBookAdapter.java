package com.example.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Book;
import com.example.btl_libary.R;

import java.util.List;

public class PopularBookAdapter extends RecyclerView.Adapter<PopularBookAdapter.BookHolder> {
    private List<Book> list;

    public PopularBookAdapter(List<Book> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PopularBookAdapter.BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularbook_item, parent, false);
        return new PopularBookAdapter.BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularBookAdapter.BookHolder holder, int position) {
        Book book = list.get(position);
        holder.imgAvt.setImageResource(book.getAvt());
        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        holder.txtCategory.setText(book.getDesc());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else
            return 0;
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtAuthor, txtCategory;
        private ImageView imgAvt;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.imgAvata);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
}
