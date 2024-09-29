package com.example.Presenters;

import android.content.Context;
import android.database.Cursor;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookRepository;
import com.example.Untils.DBManager;

import java.util.List;

public class SearchBookPresenter {
    private BookContract.View view;
    private BookRepository repository;

    public SearchBookPresenter(Context context, BookContract.View view) {
        this.repository = new BookRepository(context);
        this.view = view;
    }

    public void loadSearchBook(String edtTitleStr, String edtAuthorStr, String edtDescStr) {
        List<Book> books = repository.getSearchBooks(edtTitleStr, edtAuthorStr, edtDescStr);
        view.displayBook(books);
    }

    public void NotifySearch(String edtTitleStr, String edtAuthorStr, String edtDescStr) {
        Cursor cursor = repository.NotifySearch(edtTitleStr, edtAuthorStr, edtDescStr);
        if (cursor != null && cursor.getCount() > 0) {
            view.showSuccess("Thành công !");
        } else {
            view.showError("Sách không tồn tại !");
        }
    }

}
