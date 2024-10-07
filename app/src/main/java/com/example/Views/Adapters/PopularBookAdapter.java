package com.example.Views.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Book;
import com.example.Views.Fragments.HomeFragment;
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
        holder.btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CheckIn4", "Click on button");
                Context context = holder.itemView.getContext();
                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).showBookDetailsPopup(v, book);
                    }
                }
            }
        });
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
