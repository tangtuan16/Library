package com.example.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Untils.DBManager;
import com.example.Untils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class BorrowModel {
    private DBManager dbManager;
    private SQLiteDatabase database;
    private Context context;

    public BorrowModel(Context context) {
        this.dbManager = new DBManager(context);
        this.context = context;

    }
    public int getNumberOfBorrowedBooks(int bookId) {
        dbManager.Open();
        int userId= SharedPreferencesUtil.getUserId(context);
        database = dbManager.getDatabase();
        int count = 0;
        Cursor cursor = null;
        try {
            String query = "SELECT SUM(book_Total) FROM bookborrow WHERE user_ID = ? AND book_ID = ? ";
            cursor = database.rawQuery(query, new String[]{String.valueOf(userId),String.valueOf(bookId)});
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.Close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;

    }


    //code hùng
    public List<BorrowedBook> GetAllBorrowedBooks() {
        dbManager.Open();
        database = dbManager.getDatabase();

        List<BorrowedBook> borrowedBooksList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT bb.book_ID, b.avatar, b.title, b.author, b.category,bb.book_Total, " +
                    "bb.date_Borrow, bb.date_Return, bb.isInLibrary " +
                    "FROM bookborrow bb " +
                    "JOIN books b ON bb.book_ID = b.id " +
                    "WHERE bb.user_ID = "+ SharedPreferencesUtil.getUserId(context);

            cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("book_ID"));
                    int avatar = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                    int bookTotal = cursor.getInt(cursor.getColumnIndexOrThrow("book_Total"));
                    String borrowedDate = cursor.getString(cursor.getColumnIndexOrThrow("date_Borrow"));
                    String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("date_Return"));
                    int isInLibrary = cursor.getInt(cursor.getColumnIndexOrThrow("isInLibrary"));
                    String position = isInLibrary == 1 ? "Thư viện" : "Chỗ bạn";

                    // Tạo đối tượng BorrowedBook và thêm vào danh sách
                    BorrowedBook borrowedBook = new BorrowedBook(
                            id,
                            avatar,
                            title,
                            author,
                            category,
                            bookTotal,
                            borrowedDate,
                            returnDate,
                            position
                    );
                    borrowedBooksList.add(borrowedBook);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close(); // Đảm bảo Cursor được đóng sau khi sử dụng
            }
        }
        dbManager.Close();
        return borrowedBooksList;

    }
    public long Insert(String table, ContentValues values) {
        dbManager.Open();
        database = dbManager.getDatabase();
        long result = database.insert(table, null, values);
        dbManager.Close();
        return result;

    }
}
