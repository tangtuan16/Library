package com.example.Views.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.CartModel;
import com.example.Presenters.BookPresenter;
import com.example.Presenters.CartPresenter;
import com.example.Untils.SharedPreferencesUtil;
import com.example.btl_libary.R;

import java.util.ArrayList;
import java.util.List;


public class BookDetailActivity extends AppCompatActivity implements BookContract.View.DetailBookView {
    private BookPresenter bookPresenter;
    private Button btnBorrow;
    CheckBox checkBox;
    int bookId;
    private CartPresenter cartPresenter;
    private Button btnAddToCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        bookId = getIntent().getIntExtra("id_book", -1);
        bookPresenter = new BookPresenter(this, (BookContract.View.DetailBookView) this);
        if (bookId != -1) {
            bookPresenter.loadDetailBook(bookId);

        } else {
            //xu ly loi
            Toast.makeText(this, "ko tim thay sach", Toast.LENGTH_SHORT).show();
        }
        checkBox = findViewById(R.id.checkBox);
        int userId = SharedPreferencesUtil.getUserId(this);

        int isFavorite = bookPresenter.checkFavoriteStatus(bookId);
        if (isFavorite == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bookPresenter.updateFavoritesStatus(userId, bookId, 1);
                    Toast.makeText(BookDetailActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    bookPresenter.updateFavoritesStatus(userId, bookId, 0);
                    Toast.makeText(BookDetailActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBorrow = findViewById(R.id.btnBorrow);
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BorrowBookActivity.class);
                intent.putExtra("id_book", bookId);
                intent.putExtra("title", getIntent().getStringExtra("title"));
                startActivity(intent);
            }
        });
        Button readBookButton = findViewById(R.id.readbook);
        readBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, ReadBookActivity.class);
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("bookId", bookId);
                intent.putExtra("author", getIntent().getStringExtra("author"));
                startActivity(intent);
            }
        });

        //code của tùng(nút thêm vào giỏ)-start
        cartPresenter = new CartPresenter(this, new CartModel(this));
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartPresenter.checkBookInCart(userId, bookId)) {
                    Toast.makeText(getApplicationContext(), "Sách đã tồn tại trong giỏ.", Toast.LENGTH_SHORT).show();
                } else {
                    cartPresenter.addBookToCart(userId, bookId);
                    Toast.makeText(getApplicationContext(), "Thêm sách vào giỏ thành công.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //code của tùng(nút thêm vào giỏ)-end

    }

    @Override
    public void displayBook(@NonNull List<Book> list) {
        Book book = list.get(0);
        TextView txtTitle = findViewById(R.id.title); // Replace with your actual TextView IDs
        TextView txtAuthor = findViewById(R.id.author);
        TextView txtCategory = findViewById(R.id.category);
        ImageView imgAvt = findViewById(R.id.imgAvt);
        // Set the text of the TextViews
        txtTitle.setText("Tác phẩm: " + book.getTitle());
        txtAuthor.setText("Tác giả: " + book.getAuthor());
        txtCategory.setText("Thể loại: " + book.getDesc()); // Assuming 'getDesc()' returns the category
        imgAvt.setImageResource(book.getAvt());
    }


}
