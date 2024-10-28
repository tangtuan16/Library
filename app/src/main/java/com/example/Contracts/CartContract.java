package com.example.Contracts;

import com.example.Models.CartItem;

import java.util.List;

public interface CartContract {
    interface View {
        void showCartItems(List<CartItem> cartItems);
        void showEmptyCart();
        void showError(String error);
        void showSuccess(String message);
    }

    interface Presenter {
        void loadCartItems(int userId);
        void removeCartItem(int userId, int bookId);
        void addBookToCart(int userId, int bookId);
        boolean checkBookInCart(int userId, int bookId);
    }

    interface Model {
        List<CartItem> getCartItems(int userId);
        void deleteCartItem(int userId, int bookId);
        void addBookToCart(int userId, int bookId);
        boolean checkBookInCart(int userId, int bookId);
    }

}
