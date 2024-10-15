package com.example.Contracts;

import android.content.Context;

import com.example.Models.AuthorData;
import com.example.Models.Book;
import com.example.Models.BorrowedBook;
import com.example.Models.GenreData;

import java.util.List;

public interface BookContract {
    interface View {
        interface HomeView {
            void displayBook(List<Book> list);

            void showSuccess(String mess);

            void showError(String mess);

            void displayAuthor(List<String> authorBooks);

            void displaySuggessBook(List<Book> list);

            void showBarChart(List<AuthorData> authorData);

            void showPieChart(List<GenreData> genreData);
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
        interface BorrowBookView {
             void SetData(List<BorrowedBook> list);
        }
    }

    interface Presenter {
        interface HomePresenter {
            void loadBook();

            void loadPopularBooks();

            List<String> loadAuthorBooks();

            void loadSuggessBook();

            void loadFavoriteBooks();

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
