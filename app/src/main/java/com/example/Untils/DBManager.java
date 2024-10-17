package com.example.Untils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
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

    private static final String TABLE_CREATE_CART = "CREATE TABLE cart (\n" +
            "    cart_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    user_id INTEGER,\n" +
            "    book_id INTEGER,\n" +
            "    FOREIGN KEY (user_id) REFERENCES users(id),\n" +
            "    FOREIGN KEY (book_id) REFERENCES books(id)\n" +
            ");\n";


    public DBManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Library";
        private static final int DATABASE_VERSION = 41;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setPageSize(1024 * 1024);
            db.setMaximumSize(50 * 1024 * 1024);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_BOOK);
            db.execSQL(TABLE_CREATE_USERS);
            db.execSQL(TABLE_CREATE_BOOKBORROW);
            db.execSQL(TABLE_CREATE_FAVOURITEBOOKS);
            db.execSQL(TABLE_CREATE_NOTIFICATIONS);
            db.execSQL(TABLE_CREATE_CART);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion) {
                db.execSQL("CREATE TABLE books_backup AS SELECT * FROM books;");
                db.execSQL("CREATE TABLE users_backup AS SELECT * FROM users;");
                db.execSQL("CREATE TABLE bookborrow_backup AS SELECT * FROM bookborrow;");
                db.execSQL("CREATE TABLE favorites_books_backup AS SELECT * FROM favorites_books;");
                  db.execSQL("CREATE TABLE notifications_backup AS SELECT * FROM notifications;");
                db.execSQL("CREATE TABLE cart_backup AS SELECT * FROM cart;");
                db.execSQL("DROP TABLE IF EXISTS books");
                db.execSQL("DROP TABLE IF EXISTS users");
                db.execSQL("DROP TABLE IF EXISTS bookborrow");
                db.execSQL("DROP TABLE IF EXISTS favorites_books");
                db.execSQL("DROP TABLE IF EXISTS notifications");
                db.execSQL("DROP TABLE IF EXISTS cart");
                onCreate(db);
//                db.execSQL("INSERT INTO books (id, avatar, title, author, category) " +
//                        "SELECT id, avatar, title, author, category FROM books_backup;");
                db.execSQL("INSERT INTO users (id, username , password , fullName , email, phone, avatar) " +
                        "SELECT id, username , password , fullName , email, phone, avatar FROM users_backup;");

                db.execSQL("INSERT INTO bookborrow (borrowing_ID, user_ID, book_ID, book_Total, isInLibrary, date_Borrow, date_Return)" +
                        "SELECT borrowing_ID, user_ID, book_ID, book_Total, isInLibrary, date_Borrow, date_Return FROM bookborrow_backup;");
                db.execSQL("INSERT INTO favorites_books (Favorite_id, user_ID, book_ID, favorite) " +
                        "SELECT Favorite_id, user_ID, book_ID, favorite FROM favorites_books_backup;");

                db.execSQL("INSERT INTO notifications (id, user_id, title, content, notification_time, status) " +
                        "SELECT id, user_id, title, content, notification_time, status FROM notifications_backup;");
                db.execSQL("INSERT INTO cart (cart_id, user_id, book_id) " +
                        "SELECT cart_id, user_id, book_id FROM cart_backup;");

                db.execSQL("DROP TABLE IF EXISTS books_backup");
                db.execSQL("DROP TABLE IF EXISTS users_backup");
                db.execSQL("DROP TABLE IF EXISTS bookborrow_backup");
                db.execSQL("DROP TABLE IF EXISTS favorites_books_backup");
                db.execSQL("DROP TABLE IF EXISTS notifications_backup");
                db.execSQL("DROP TABLE IF EXISTS cart_backup");
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

}