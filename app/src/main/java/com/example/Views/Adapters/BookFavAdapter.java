package com.example.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.Models.Book;
import com.example.Views.Activitys.BookDetailActivity;
import com.example.btl_libary.R;
import java.util.List;

public class BookFavAdapter extends RecyclerView.Adapter<BookFavAdapter.BookViewHolder> {
    private List<Book> list;
    private ViewPager2 viewPager2;

    public BookFavAdapter(List<Book> list, ViewPager2 viewPager2) {
        this.list = list;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_fav_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = list.get(position % list.size());
        holder.imgAvt.setImageResource(book.getAvt());
//        holder.txtTitle.setText(book.getTitle());
//        holder.txtAuthor.setText(book.getAuthor());
//        holder.txtCategory.setText(book.getDesc());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;  // Để có thể lặp lại vòng tròn
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtAuthor, txtCategory;
        private ImageView imgAvt;

        public BookViewHolder(@NonNull View itemView) {

            super(itemView);
            imgAvt = itemView.findViewById(R.id.imgAvt);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition() % list.size();
                    if (position != RecyclerView.NO_POSITION) {
                        Book clickedBook = list.get(position);
                        int bookId = clickedBook.getId(); // Lấy ID sách
                        Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                        intent.putExtra("id_book", bookId); // Truyền ID sách làm extra
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (viewPager2 != null) {
            viewPager2.setCurrentItem(getMiddlePosition(), false);
        }
    }

    private int getMiddlePosition() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % list.size();
    }
}
