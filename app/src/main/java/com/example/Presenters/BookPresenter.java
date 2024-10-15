package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;
import com.example.Models.GenreData;

import java.util.List;

public class BookPresenter implements BookContract.Presenter.HomePresenter {
    private BookContract.View.HomeView homeView;
    private BookContract.View.LibraryView libraryView;
    private BookContract.View.DetailBookView DetailBookView;
    private BookModel repository;

    public BookPresenter(Context context, BookContract.View.HomeView view) {
        this.homeView = view;
        this.repository = new BookModel(context);
    }

    public BookPresenter(Context context, BookContract.View.LibraryView libraryView) {
        this.repository = new BookModel(context);
        this.libraryView = libraryView;
    }
    public BookPresenter(Context context, BookContract.View.DetailBookView DetailBookView) {
        this.repository = new BookModel(context);
        this.DetailBookView = DetailBookView;
    }


    @Override
    public void loadBook() {
        List<Book> books = repository.getAllBooks();//lay data
        libraryView.displayBook(books);// tra data ve man hien thi
    }
    public void loadGridBook() {
        List<Book> books = repository.getAllBooks();//lay data
        libraryView.displayGridBook(books);// tra data ve man hien thi
    }
    public void loadFavoriteBooks() {
        List<Book> newBooks = repository.getFavoriteBooks();
        libraryView.displayFavoriteBook(newBooks);
    }
    public void updateFavoritesStatus(int userId, int bookId, int favorite) {
        repository.updateFavorites(userId, bookId, favorite);
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


    @Override
    public void loadSuggessBook() {
        List<Book> suggessBook = repository.getSuggestBooks();
        homeView.displaySuggessBook(suggessBook);
    }


    public void addBook(int avatar, String title, String author, String category) {
        long id = repository.insertBook(avatar, title, author, category);
        if (id > 0) {
            libraryView.showSuccess("Thêm thành công !");
        } else {
            libraryView.showError("Thêm thất bại !");
        }
    }
    //detail book cua khoi
        public void loadDetailBook(int id) {
            List<Book> DetailBook = repository.getDetailBook(id);


            DetailBookView.displayBook(DetailBook);
        }
        public int checkFavoriteStatus(int id) {
            int fav= repository.checkIfFavorite(id);
           return fav;
        }


}
