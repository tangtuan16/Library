package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;
import com.example.Models.GenreData;
import com.example.Untils.JsonUtils;

import java.util.List;

public class BookPresenter implements BookContract.Presenter.HomePresenter {
    private BookContract.View.HomeView homeView;
    private BookContract.View.LibraryView libraryView;
    private BookModel repository;

    public BookPresenter(Context context, BookContract.View.HomeView view) {
        this.homeView = view;
        this.repository = new BookModel(context);
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

    public void loadGenreData() {
        List<GenreData> genreDataList = repository.getGenreData();
        homeView.displayGenreData(genreDataList);
    }


    public void addBook(int avatar, String title, String author, String category) {
        long id = repository.insertBook(avatar, title, author, category);
        if (id > 0) {
            libraryView.showSuccess("Thêm thành công !");
        } else {
            libraryView.showError("Thêm thất bại !");
        }
    }
}
