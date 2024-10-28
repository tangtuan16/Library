package com.example.Presenters;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.example.Contracts.CartContract;
import com.example.Models.CartItem;
import com.example.Models.CartModel;
import com.example.Views.Activitys.BookDetailActivity;
import com.example.Views.Activitys.CartActivity;
import java.util.List;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View view;
    private CartContract.Model model;
    private Context context;
    public CartPresenter(CartActivity cartActivity, CartModel model) {
        this.view = cartActivity; // Gán cartActivity cho view
        this.model = model;
        this.context = cartActivity;
    }
    public CartPresenter(BookDetailActivity bookDetailActivity, CartModel model) {
        this.model = model;
        this.context = bookDetailActivity;
    }
    public void addBookToCart(int userId, int bookId) {

        model.addBookToCart(userId,bookId);
    }
    public boolean checkBookInCart(int userId, int bookId) {
        return model.checkBookInCart(userId, bookId);
    }
    @Override
    public void loadCartItems(int userId) {
        List<CartItem> cartItems = model.getCartItems(userId);
        if (cartItems.isEmpty()) {
            view.showCartItems(cartItems);
            view.showEmptyCart();
        } else {
            view.showCartItems(cartItems);
        }
    }
    @Override
    public void removeCartItem(int userId, int bookId) {
        try {
            model.deleteCartItem(userId, bookId);
            loadCartItems(userId);
        } catch (Exception e) {
            view.showError("Lỗi khi hiển thị giỏ sách");
        }
    }
    public void showConfirmDialog(int bookId, int userId) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn xóa sách này khỏi giỏ không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    removeCartItem(userId, bookId);
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
