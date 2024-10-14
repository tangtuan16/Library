package com.example.Contracts;

import android.content.Context;

import com.example.Models.Book;
import com.example.Models.GenreData;

import java.util.List;

public interface BookContract {
    interface View {
        interface HomeView {
            void displayBook(List<Book> list);

            void showSuccess(String mess);

            void showError(String mess);

            void displayAuthor(List<String> authorBooks);

            void displayGenreData(List<GenreData> genreDataList);
        }

        interface BookDetailView {
            void showBookDetail(List<Book> bookList, int bookId);
        }

        interface LibraryView {
            void displayBook(List<Book> list);

            void showSuccess(String mess);

            void showError(String mess);

            void displayGridBook(List<Book> list);

            void displayFavoriteBook(List<Book> newBooks);


        }

        //detail book cua khoi
        interface DetailBookView {
            void displayBook(List<Book> list);

        }
    }

    interface Presenter {
        interface HomePresenter {
            void loadBook();

            void loadPopularBooks();

            List<String> loadAuthorBooks();

        }

        interface BookDetailPresenter {
            void loadBookDetail(int bookId, Context context);
        }
    }

    interface Model {
        void insertBook();

        void deleteBook();

        void updateBook();

        void findBook();
    }
}
