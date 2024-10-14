package com.example.Untils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.Models.BorrowedBook;
import com.example.Models.User;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Library";
        private static final int DATABASE_VERSION = 32;

        private static final String TABLE_CREATE_BOOK = "CREATE TABLE books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "avatar INTEGER, " +
                "title TEXT NOT NULL, " +
                "author TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "content TEXT);";


        private static final String TABLE_CREATE_USERS = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "fullName TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "avatar BLOB);";


        private static final String TABLE_CREATE_BOOKBORROW = "CREATE TABLE bookborrow (" +
                "borrowing_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_ID INTEGER, " +
                "book_ID INTEGER, " +
                "book_Total INTEGER, " +
                "isInLibrary INTEGER DEFAULT 1 CHECK (isInLibrary IN (0, 1)), " +
                "date_Borrow TEXT, " +
                "date_Return TEXT, " +
                "FOREIGN KEY (user_ID) REFERENCES users(id), " +
                "FOREIGN KEY (book_ID) REFERENCES books(id));";

        private static final String TABLE_CREATE_FAVOURITEBOOKS = "CREATE TABLE favorites_books (" +
                "Favorite_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_ID INTEGER, " +
                "book_ID INTEGER, " +
                "favorite INTEGER CHECK (favorite IN (0, 1)), " +
                "FOREIGN KEY (user_ID) REFERENCES users(id), " +
                "FOREIGN KEY (book_ID) REFERENCES books(id));";

        private static final String TABLE_CREATE_NOTIFICATIONS = "CREATE TABLE notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "notification_time DATE NOT NULL, " +
                "status INTEGER DEFAULT 0 CHECK (status IN (0,1)) NOT NULL);";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_BOOK);
            db.execSQL(TABLE_CREATE_USERS);
            db.execSQL(TABLE_CREATE_BOOKBORROW);
            db.execSQL(TABLE_CREATE_FAVOURITEBOOKS);
            db.execSQL(TABLE_CREATE_NOTIFICATIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion) {
                db.execSQL("CREATE TABLE books_backup AS SELECT * FROM books;");
                db.execSQL("CREATE TABLE users_backup AS SELECT * FROM users;");
                db.execSQL("DROP TABLE IF EXISTS books");
                db.execSQL("DROP TABLE IF EXISTS users");
                db.execSQL("DROP TABLE IF EXISTS bookborrow");
                db.execSQL("DROP TABLE IF EXISTS favorites_books");
                db.execSQL("DROP TABLE IF EXISTS notifications");
                onCreate(db);
                db.execSQL("INSERT INTO books (id, avatar, title, author, category) " +
                        "SELECT id, avatar, title, author, category FROM books_backup;");
                db.execSQL("INSERT INTO users (id, username , password , fullName , email, phone, avatar) " +
                        "SELECT id, username , password , fullName , email, phone, avatar FROM users_backup;");
                db.execSQL("DROP TABLE IF EXISTS books_backup");
                db.execSQL("DROP TABLE IF EXISTS users_backup");
            }
        }
    }

    public void Open() {
        database = dbHelper.getWritableDatabase();
    }

    public void Close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    //Code e Sơn
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
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"))
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
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"))
                );
                cursor.close();
                return user;
            }
            cursor.close();
        }
        return null;
    }




    public long Insert(String table, ContentValues values) {
        return database.insert(table, null, values);
    }

    public int Update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return database.update(table, values, whereClause, whereArgs);
    }

    public int Delete(String table, String whereClause, String[] whereArgs) {
        return database.delete(table, whereClause, whereArgs);
    }


}