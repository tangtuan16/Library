package com.example.Untils;

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

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Library";
        private static final int DATABASE_VERSION = 24;

        private static final String TABLE_CREATE_BOOK = "CREATE TABLE books (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    avatar INTEGER,\n" +
                "    title TEXT NOT NULL,\n" +
                "    author TEXT NOT NULL,\n" +
                "    category TEXT NOT NULL,\n" +
                "    content TEXT\n" +
                ");\n";

        private static final String TABLE_CREATE_USERS = "CREATE TABLE users (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    username TEXT NOT NULL,\n" +
                "    password TEXT NOT NULL,\n" +
                "    fullName TEXT NOT NULL,\n" +
                "    email TEXT NOT NULL,\n" +
                "    phone TEXT NOT NULL,\n" +
                "    avatar BLOB\n" +
                ");\n";

        private static final String TABLE_CREATE_BOOKBORROW = "CREATE TABLE bookborrow (\n" +
                "    borrowing_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_ID INTEGER,\n" +
                "    book_ID INTEGER,\n" +
                "    category TEXT,\n" +
                "    book_Total INTEGER,\n" +
                "    date_Borrow DATE CHECK(Date_Borrow <= CURRENT_DATE),\n" +
                "    date_Payment DATE CHECK(Date_Payment >= Date_Borrow),\n" +
                "    FOREIGN KEY (user_ID) REFERENCES users(id),\n" +
                "    FOREIGN KEY (book_ID) REFERENCES books(id),\n" +
                "    FOREIGN KEY (category) REFERENCES books(category)\n" +
                ");\n";

        private static final String TABLE_CREATE_FAVOURITEBOOKS = "CREATE TABLE favorites_books(\n" +
                "    Favorite_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    user_ID INTEGER,\n" +
                "    book_ID INTEGER,\n" +
                "    favorite INTEGER CHECK (Favorite IN (0, 1)),\n" +
                "    FOREIGN KEY (user_ID) REFERENCES users(id),\n" +
                "    FOREIGN KEY (book_ID) REFERENCES books(id)\n" +
                ");\n";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_BOOK);
            db.execSQL(TABLE_CREATE_USERS);
            db.execSQL(TABLE_CREATE_BOOKBORROW);
            db.execSQL(TABLE_CREATE_FAVOURITEBOOKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion) {
                db.execSQL("CREATE TABLE books_backup AS SELECT * FROM books;");
                db.execSQL("CREATE TABLE users_backup AS SELECT * FROM users;");
                db.execSQL("CREATE TABLE bookborrow_backup AS SELECT * FROM bookborrow;");
                db.execSQL("CREATE TABLE favorites_books_backup AS SELECT * FROM favorites_books;");

                db.execSQL("DROP TABLE IF EXISTS books");
                db.execSQL("DROP TABLE IF EXISTS users");
                db.execSQL("DROP TABLE IF EXISTS bookborrow");
                db.execSQL("DROP TABLE IF EXISTS favorites_books");

                onCreate(db);
                db.execSQL("INSERT INTO books (id, avatar, title, author, category) " +
                        "SELECT id, avatar, title, author, category FROM books_backup;");
                db.execSQL("INSERT INTO users (id, username , password , fullName , email, phone, avatar) " +
                        "SELECT id, username , password , fullName , email, phone, avatar FROM users_backup;");
                db.execSQL("INSERT INTO bookborrow (borrowing_ID, user_ID, book_ID, category, book_Total, date_Borrow, date_Payment) " +
                        "SELECT borrowing_ID, user_ID, book_ID, category, book_Total, date_Borrow, date_Payment FROM  bookborrow_backup;");
                db.execSQL("INSERT INTO favorites_books (Favorite_id, user_ID, book_ID, favorite) " +
                        "SELECT Favorite_id, user_ID, book_ID, favorite FROM favorites_books_backup;");

                db.execSQL("DROP TABLE IF EXISTS books_backup");
                db.execSQL("DROP TABLE IF EXISTS users_backup");
                db.execSQL("DROP TABLE IF EXISTS bookborrow_backup");
                db.execSQL("DROP TABLE IF EXISTS favorites_books_backup");
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


}
