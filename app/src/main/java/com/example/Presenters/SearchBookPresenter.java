package com.example.Presenters;

import android.content.Context;
import android.database.Cursor;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;

import java.util.List;

public class SearchBookPresenter {
    private BookContract.View.LibraryView view;
    private BookModel repository;

    public SearchBookPresenter(Context context, BookContract.View.LibraryView view) {
        this.repository = new BookModel(context);
        this.view = (BookContract.View.LibraryView) view;
    }
    public void loadSearchBook(String edtTitleStr, String edtAuthorStr, String edtDescStr) {
        List<Book> books = repository.getSearchBooks(edtTitleStr, edtAuthorStr, edtDescStr);
        view.displayBook(books);
    }

    public void searchBooksByAuthor(String selectedAuthor) {
        List<Book> books = repository.getBooksByAuthor(selectedAuthor);
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
