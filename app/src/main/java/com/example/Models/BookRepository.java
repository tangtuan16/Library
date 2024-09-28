package com.example.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Untils.DBManager;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private DBManager dbManager;
    private SQLiteDatabase database;

    public BookRepository(Context context) {
        this.dbManager = new DBManager(context);
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        dbManager.Open();
        database = dbManager.getDatabase();
        String sql = "SELECT * FROM books";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int avt = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                bookList.add(new Book(id, avt, title, author, desc));
            }
            cursor.close();
        }
        dbManager.Close();
        return bookList;
    }

    public long insertBook(int avatar, String title, String author, String desc) {
        dbManager.Open();
        database = dbManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", avatar);
        values.put("title", title);
        values.put("author", author);
        values.put("description", desc);
        long result = database.insert("books", null, values);
        dbManager.Close();
        return result;
    }

    public int updateBook(ContentValues values, String selection, String[] selectionArgs) {
        dbManager.Open();
        database = dbManager.getDatabase();
        int rows = database.update("books", values, selection, selectionArgs);
        dbManager.Close();
        return rows;
    }

    public int deleteBook(String selection, String[] selectionArgs) {
        dbManager.Open();
        database = dbManager.getDatabase();
        int rows = database.delete("books", selection, selectionArgs);
        dbManager.Close();
        return rows;
    }

    public Cursor searchBooks(String selection, String[] selectionArgs) {
        dbManager.Open();
        database = dbManager.getDatabase();
        Cursor cursor = database.query("books", null, selection, selectionArgs, null, null, null);
        dbManager.Close();
        return cursor;
    }
}
