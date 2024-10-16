package com.example.Views.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Book;
import com.example.Views.Activitys.BookDetailActivity;
import com.example.btl_libary.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {
    private List<Book> list;

    public BookAdapter(List<Book> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = list.get(position);
        holder.imgAvt.setImageResource(book.getAvt());
        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        holder.txtCategory.setText(book.getDesc());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtAuthor, txtCategory;
        private ImageView imgAvt;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.imgAvt);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Book clickedBook = list.get(position);
                        int bookId = clickedBook.getId(); // Get the book ID
                        // Create an Intent to start the target activity
                        Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                        intent.putExtra("id_book", bookId); // Pass the book ID as an extra
                        intent.putExtra("title", txtTitle.getText().toString());
                        intent.putExtra("author", txtAuthor.getText().toString());
                        v.getContext().startActivity(intent); // Start the activity
                    }


                }
            });
        }

    }
}
