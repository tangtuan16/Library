package com.example.Contracts;

import android.annotation.SuppressLint;

import com.example.Models.Book;
import com.google.firebase.firestore.auth.User;

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
        //detail book cua khoi
        interface DetailBookView {
            void displayBook(List<Book> list);

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
