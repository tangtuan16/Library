package com.example.Presenters;

import com.example.Contracts.CartContract;
import com.example.Models.CartItem;
import com.example.Models.CartModel;
import com.example.Views.Activitys.BookDetailActivity;
import com.example.Views.Activitys.CartActivity;

import java.util.List;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View view;
    private CartContract.Model model;




    public CartPresenter(CartActivity cartActivity, CartModel model) {
        this.view = cartActivity; // Gán cartActivity cho view
        this.model = model;
    }


    public CartPresenter(BookDetailActivity bookDetailActivity, CartModel model) {

        this.model = model;
    }


    public boolean addBookToCart(int userId, int bookId) {

        model.addBookToCart(userId,bookId);


        return false;
    }
    @Override
    public void loadCartItems(int userId) {
        List<CartItem> cartItems = model.getCartItems(userId);
        if (cartItems.isEmpty()) {
            view.showEmptyCart();
        } else {
            view.showCartItems(cartItems);
        }
    }

    @Override
    public void removeCartItem(int userId, int bookId) {
        if (model.deleteCartItem(userId, bookId)) {
            loadCartItems(userId); // Tải lại giỏ hàng sau khi xóa
        } else {
            view.showError("Lỗi khi hiển thị giỏ sách");
        }
    }
}
