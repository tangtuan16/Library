package com.example.Untils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.Models.User;

public class DBManager {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public long registerUser(String username, String password, String fullName, String email, String phone) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("fullName", fullName);
        values.put("email", email);
        values.put("phone", phone);

        return database.insert("users", null, values);
    }

    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        Cursor cursor = database.rawQuery(query, new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public int updateUser(int userId, String password, String fullName, String email, String phone, byte[] avatar) {
        ContentValues values = new ContentValues();
        if (password != null) {
            values.put("password", password);
        }
        if (fullName != null) {
            values.put("fullName", fullName);
        }
        if (email != null) {
            values.put("email", email);
        }
        if (phone != null) {
            values.put("phone", phone);
        }
        if (avatar != null) {
            values.put("avatar", avatar);
        }
        return database.update("users", values, "id=?", new String[]{String.valueOf(userId)});
    }


    public User getUserById(int userId) {
        Cursor cursor = database.query("users", null, "id=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") User user = new User(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("fullName")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("phone")),
                        cursor.getBlob(cursor.getColumnIndex("avatar"))
                );
                cursor.close();
                return user;
            }
            cursor.close();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        Cursor cursor = database.query("users", null, "username=?", new String[]{username}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") User user = new User(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("fullName")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("phone")),
                        cursor.getBlob(cursor.getColumnIndex("avatar"))
                );
                cursor.close();
                return user;
            }
            cursor.close();
        }
        return null;
    }





    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Library";
        private static final int DATABASE_VERSION = 13;

        private static final String TABLE_CREATE_USER =
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE, " +
                        "password TEXT, " +
                        "fullName TEXT, " +
                        "email TEXT, " +
                        "phone TEXT, " +
                        "avatar BLOB);";

        private static final String TABLE_CREATE_BOOK =
                "CREATE TABLE books (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "avatar INTEGER, " +
                        "title TEXT, " +
                        "author TEXT, " +
                        "description TEXT);";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_USER);
            db.execSQL(TABLE_CREATE_BOOK);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS books");
            onCreate(db);
        }
    }
}
