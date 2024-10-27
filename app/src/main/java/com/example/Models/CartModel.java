package com.example.Models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.Contracts.CartContract;
import com.example.Untils.DBManager;
import java.util.ArrayList;
import java.util.List;

public class CartModel implements CartContract.Model {
    private DBManager dbManager;
    public CartModel(Context context) {
        dbManager = new DBManager(context);
    }
    @Override
    public List<CartItem> getCartItems(int userId) {
        dbManager.Open();
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = dbManager.getDatabase();
        String query = "SELECT c.book_id,b.title, b.author, b.category AS genre, b.avatar " +
                "FROM cart c " +
                "JOIN books b ON c.book_id = b.id " +
                "WHERE c.user_id=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
                int avt = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                CartItem cartItem = new CartItem(userId, bookId, title, author,genre, avt);
                cartItems.add(cartItem);
            }
            cursor.close();
        }
        dbManager.Close();
        return cartItems;
    }
    @Override
    public boolean deleteCartItem(int userId, int bookId) {
        dbManager.Open();
        SQLiteDatabase db = dbManager.getDatabase();
        int result = db.delete("cart", "user_id=? AND book_id=?", new String[]{String.valueOf(userId), String.valueOf(bookId)});
        dbManager.Close();
        return result > 0;
    }
    public void addBookToCart(int userId, int bookId) {
        dbManager.Open();
        SQLiteDatabase db = dbManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("book_id", bookId);
        long result = db.insert("cart", null, values);
        dbManager.Close();
    }
    public boolean checkBookInCart(int userId, int bookId) {
        dbManager.Open();
        SQLiteDatabase db = dbManager.getDatabase();
        String query = "SELECT * FROM cart WHERE user_id=? AND book_id=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(bookId)});
        boolean exists = cursor != null && cursor.getCount() > 0;
        cursor.close();
        dbManager.Close();
        return exists; // Trả về true nếu sách đã có trong giỏ
    }
}
