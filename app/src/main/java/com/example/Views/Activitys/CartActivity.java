package com.example.Views.Activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.CartContract;
import com.example.Models.CartItem;
import com.example.Models.CartModel;
import com.example.Presenters.CartPresenter;
import com.example.Untils.SharedPreferencesUtil;
import com.example.Views.Adapters.CartAdapter;
import com.example.btl_libary.R;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartContract.View, CartAdapter.OnRemoveClickListener {
    private CartPresenter presenter;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
         int userId = SharedPreferencesUtil.getUserId(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new CartPresenter(this, new CartModel(this));

        presenter.loadCartItems(userId);

    }

    @Override
    public void showCartItems(List<CartItem> cartItems) {
        cartAdapter = new CartAdapter(cartItems, this); // Thêm listener cho adapter
        recyclerView.setAdapter(cartAdapter);
    }

    @Override
    public void showEmptyCart() {
        Toast.makeText(this, "Giỏ sách đang trống", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRemoveClick(int bookId) {
      int userId = SharedPreferencesUtil.getUserId(this);
        // Xử lý xóa sản phẩm
        presenter.removeCartItem(userId, bookId);
        // Tải lại giỏ hàng sau khi xóa
        presenter.loadCartItems(userId);
    }
}
