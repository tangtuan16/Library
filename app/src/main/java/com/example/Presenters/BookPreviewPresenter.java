package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;

import java.util.List;

public class BookPreviewPresenter implements BookContract.Presenter.BookDetailPresenter {
    private BookContract.View.BookDetailView bookDetailView;
    private Context context;
    private BookModel bookModel;

    public BookPreviewPresenter(BookContract.View.BookDetailView bookDetailView, Context context, BookModel bookModel) {
        this.bookDetailView = bookDetailView;
        this.context = context;
        this.bookModel = new BookModel(context);
    }

    @Override
    public void loadBookDetail(int bookId, Context context) {
        List<Book> bookList = bookModel.loadBookDetail(bookId, context);
        bookDetailView.showBookDetail(bookList, bookId);
    }
}
