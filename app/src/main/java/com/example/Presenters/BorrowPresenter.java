package com.example.Presenters;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.example.Contracts.BookContract;
import com.example.Models.BookModel;
import com.example.Models.BorrowModel;
import com.example.Models.BorrowedBook;
import com.example.Models.Notification;

import java.util.List;

public class BorrowPresenter implements  BookContract.Presenter {
    private BookContract.View.BorrowBookView borrowView;
    private BookContract.Model borrowModel;
    private BorrowModel repository ;

    public BorrowPresenter(Context context, BookContract.View.BorrowBookView view) {
        this.borrowView = view;
        this.repository = new BorrowModel(context);
    }

    public int getNumberOfBorrowedBooks(int bookId) {
        return repository.getNumberOfBorrowedBooks(bookId);
    }

    public long InsertBorrowedBook(ContentValues values) {
       return repository.Insert("bookborrow", values);
    }
    public void deleteBorrowedBook(int borrowing_ID) {
        repository.deleteBorrowedBook(borrowing_ID);
    }
    public void LoadBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = repository.GetAllBorrowedBooks();
        borrowView.SetData(borrowedBooks);
    }
}
