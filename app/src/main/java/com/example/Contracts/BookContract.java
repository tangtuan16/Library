package com.example.Contracts;

import com.example.Models.Book;

import java.util.List;

public interface BookContract {
    interface View {
        interface HomeView {
            void displayBook(List<Book> list);

            void showSuccess(String mess);

            void showError(String mess);

            void displayAuthor(List<String> authorBooks);

        }

        interface LibraryView {
            void displayBook(List<Book> list);

            void showSuccess(String mess);

            void showError(String mess);
        }
    }

    interface Presenter {
        void loadBook();
    }

    interface Model {
        void insertBook();

        void deleteBook();

        void updateBook();

        void findBook();
    }
}
