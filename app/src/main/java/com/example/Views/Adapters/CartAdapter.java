package com.example.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.CartItem;
import com.example.btl_libary.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private OnRemoveClickListener listener;

    public interface OnRemoveClickListener {
        void onRemoveClick(int bookId);
    }

    public CartAdapter(List<CartItem> cartItems, OnRemoveClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView titleTextView;
        TextView genreTextView;
        Button removeButton;
        ImageView bookImageView;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
            authorTextView =itemView.findViewById((R.id.authorTextView));
            genreTextView = itemView.findViewById(R.id.genreTextView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
        }

        void bind(CartItem item, OnRemoveClickListener listener) {

            titleTextView.setText(item.getTitle());
            authorTextView.setText("Tác giả: " + item.getAuthor());
            genreTextView.setText("Thể loại: " + item.getGenre());
            bookImageView.setImageResource(item.getAvt());
            removeButton.setOnClickListener(v -> listener.onRemoveClick(item.getBookId()));
        }

    }
}
