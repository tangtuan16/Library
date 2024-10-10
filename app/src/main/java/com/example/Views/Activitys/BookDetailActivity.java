package com.example.Views.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.Contracts.BookContract;
import com.example.Models.Book;

import com.example.Presenters.BookPresenter;
import com.example.btl_libary.R;

import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class BookDetailActivity extends AppCompatActivity implements BookContract.View.DetailBookView {
    private BookPresenter bookPresenter;
    private Button btnBorrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        int bookId = getIntent().getIntExtra("id_book", -1);
        bookPresenter = new BookPresenter(this, (BookContract.View.DetailBookView) this);
        if (bookId != -1) {
            bookPresenter.loadDetailBook(bookId);
            Toast.makeText(this, "id" + bookId, Toast.LENGTH_SHORT).show();

        } else {
            //xu ly loi
            Toast.makeText(this, "ko tim thay sach", Toast.LENGTH_SHORT).show();
        }
        btnBorrow = findViewById(R.id.btnBorrow);
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BorrowBookActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void displayBook(List<Book> list) {
        Book book = list.get(0);
        TextView txtTitle = findViewById(R.id.title); // Replace with your actual TextView IDs
        TextView txtAuthor = findViewById(R.id.author);
        TextView txtCategory = findViewById(R.id.category);
        ImageView imgAvt = findViewById(R.id.imgAvt);
        // Set the text of the TextViews
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtCategory.setText(book.getDesc()); // Assuming 'getDesc()' returns the category
        imgAvt.setImageResource(book.getAvt());

    }


}
