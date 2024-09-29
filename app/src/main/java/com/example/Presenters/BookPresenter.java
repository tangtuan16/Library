package com.example.Presenters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookRepository;

import java.util.List;

public class BookPresenter implements BookContract.Presenter {
    private BookContract.View view;
    private BookRepository repository;
    private Cursor cursor;

    public BookPresenter(Context context, BookContract.View view) {
        this.repository = new BookRepository(context);
        this.view = view;
    }


    @Override
    public void loadBook() {
        List<Book> books = repository.getAllBooks();
        view.displayBook(books);
    }

    public void addBook(int avatar, String title, String author, String desc) {
        long id = repository.insertBook(avatar, title, author, desc);
        if (id > 0) {
            view.showSuccess("Thêm thành công !");
        } else {
            view.showError("Thêm thất bại !");
        }
    }

    public void updateBook(ContentValues values, String selection, String[] selectionArgs) {
        int rows = repository.updateBook(values, selection, selectionArgs);
        if (rows > 0) {
            view.showSuccess("Cập nhật thành công !");
        } else {
            view.showError("Cập nhật thất bại !");
        }
    }

    public void deleteBook(String selection, String[] selectionArgs) {
        int rows = repository.deleteBook(selection, selectionArgs);
        if (rows > 0) {
            view.showSuccess("Xóa thành công !");
        } else {
            view.showError("Xóa thất bại !");
        }
    }


    public void getAllBooks() {
        cursor = (Cursor) repository.getAllBooks();
        if (cursor != null) {
           view.showSuccess("Thành công !");
        } else {
            view.showError("Không có dữ liệu !");
        }
    }
}
