package com.example.Presenters;

import android.content.ContentValues;
import android.content.Context;

import com.example.Contracts.BookContract;
import com.example.Models.BorrowModel;
import com.example.Models.BorrowedBook;

import java.util.List;

public class BorrowPresenter implements  BookContract.Presenter {
    private BookContract.View.BorrowBookView borrowView;
    private BorrowModel model;

    public BorrowPresenter(Context context, BookContract.View.BorrowBookView view) {
        this.borrowView = view;
        this.model = new BorrowModel(context);
    }

    public int getNumberOfBorrowedBooks(int bookId) {
        return model.getNumberOfBorrowedBooks(bookId);
    }

    public long InsertBorrowedBook(ContentValues values) {
       return model.Insert("bookborrow", values);
    }
    public void deleteBorrowedBook(int borrowing_ID) {
        model.deleteBorrowedBook(borrowing_ID);
    }
    public void LoadBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = model.GetAllBorrowedBooks();
        borrowView.SetData(borrowedBooks);
    }
}
