package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;

import java.util.List;

public class BookPresenter implements BookContract.Presenter {
    private BookContract.View.HomeView homeView;
    private BookContract.View.LibraryView libraryView;
    private BookModel repository;

    public BookPresenter(Context context, BookContract.View.HomeView view) {
        this.repository = new BookModel(context);
        this.homeView = view;
    }

    public BookPresenter(Context context, BookContract.View.LibraryView libraryView) {
        this.repository = new BookModel(context);
        this.libraryView = libraryView;
    }

    @Override
    public void loadBook() {
        List<Book> books = repository.getAllBooks();//lay data
        libraryView.displayBook(books);// tra data ve man hien thi
    }


    public void loadPopularBooks() {
        List<Book> popularBooks = repository.getPopularBooks();
        homeView.displayBook(popularBooks);
    }

    public List<String> loadAuthorBooks() {
        List<String> authorBooks = repository.getAuthorBooks();
        homeView.displayAuthor(authorBooks);
        return authorBooks;
    }

//    public void addBook(int avatar, String title, String author, String category) {
//        long id = repository.insertBook(avatar, title, author, category);
//        if (id > 0) {
//            view.showSuccess("Thêm thành công !");
//        } else {
//            view.showError("Thêm thất bại !");
//        }
//    }
//
//    public void updateBook(ContentValues values, String selection, String[] selectionArgs) {
//        int rows = repository.updateBook(values, selection, selectionArgs);
//        if (rows > 0) {
//            view.showSuccess("Cập nhật thành công !");
//        } else {
//            view.showError("Cập nhật thất bại !");
//        }
//    }
//
//    public void deleteBook(String selection, String[] selectionArgs) {
//        int rows = repository.deleteBook(selection, selectionArgs);
//        if (rows > 0) {
//            view.showSuccess("Xóa thành công !");
//        } else {
//            view.showError("Xóa thất bại !");
//        }
//    }
//

//    public void getAllBooks() {
//        cursor = (Cursor) repository.getAllBooks();
//        if (cursor != null) {
//           view.showSuccess("Thành công !");
//        } else {
//            view.showError("Không có dữ liệu !");
//        }
//    }
}
