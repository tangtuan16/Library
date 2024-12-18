package com.example.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Book;
import com.example.Presenters.BookPreviewPresenter;
import com.example.Views.Activitys.BookDetailActivity;
import com.example.Views.Popup.BookPreviewPopup;
import com.example.btl_libary.R;

import java.util.List;

public class PopularBookAdapter extends RecyclerView.Adapter<PopularBookAdapter.BookHolder> {
    private List<Book> list;
    private Context context;

    public PopularBookAdapter(List<Book> list, Context context) {
        this.list = list;
        this.context = context;
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
        holder.txtTitle.setText(book.getTitle().toString());
        holder.txtAuthor.setText(book.getAuthor().toString());
        holder.txtCategory.setText(book.getDesc().toString());
        holder.btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookPreviewPopup popup = new BookPreviewPopup(v.getContext());
                BookPreviewPresenter presenter = new BookPreviewPresenter(popup, v.getContext(), null);
                presenter.loadBookDetail(book.getId(), context);
                Log.d("CheckInfor", "id= " + book.getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = holder.getAdapterPosition();
                Book clickedBook = list.get(a);
                int bookId = clickedBook.getId();
                Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                intent.putExtra("id_book", bookId);
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
      return list != null ? list.size() : 0;
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtAuthor, txtCategory;
        private ImageView imgAvt;
        private Button btnPreview;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.imgAvata);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            btnPreview = itemView.findViewById(R.id.btnPreview);
        }
    }
}
