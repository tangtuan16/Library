package com.example.Views.Popup;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.btl_libary.R;

import java.util.List;

public class BookDetailPopup implements BookContract.View.BookDetailView {
    private Dialog dialog;
    private TextView tvBookTitle, tvBookContent, tvBookAuthor;
    private ImageView ivBookAvt;

    public BookDetailPopup(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_book_details);
        dialog.getWindow().setLayout((int) (context.getResources().getDisplayMetrics().widthPixels * 0.95),
                (int) (context.getResources().getDisplayMetrics().heightPixels * 0.8));

        tvBookTitle = dialog.findViewById(R.id.bookTitle);
        tvBookContent = dialog.findViewById(R.id.bookDescription);
        tvBookAuthor = dialog.findViewById(R.id.bookAuthor);
        ivBookAvt = dialog.findViewById(R.id.bookImage);
    }

    @Override
    public void showBookDetail(List<Book> bookList, int bookId) {
        tvBookTitle.setText(bookList.get(bookId - 1).getTitle());
        tvBookAuthor.setText(bookList.get(bookId - 1).getAuthor());
        tvBookContent.setText(bookList.get(bookId - 1).getContent());
        dialog.show();
    }
}
