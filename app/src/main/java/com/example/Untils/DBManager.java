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

//        private static final String TABLE_CREATE_USERS =
//                "CREATE TABLE users (" +
//                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        "username TEXT UNIQUE, " +
//                        "password TEXT, " +
//                        "fullName TEXT, " +
//                        "email TEXT, " +
//                        "phone TEXT, " +
//                        "avatar BLOB);";
//        private static final String TABLE_CREATE_BOOK = "CREATE TABLE books (id INTEGER PRIMARY KEY AUTOINCREMENT, avatar INTEGER, title TEXT, author TEXT,category TEXT);";


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
//            if (oldVersion < newVersion) {
//                db.execSQL("CREATE TABLE books_backup AS SELECT * FROM books;");
//                db.execSQL("CREATE TABLE users_backup AS SELECT * FROM users;");
//                db.execSQL("DROP TABLE IF EXISTS books");
//                db.execSQL("DROP TABLE IF EXISTS users");
//                onCreate(db);
//                db.execSQL("INSERT INTO books (id, avatar, title, author, category) " +
//                        "SELECT id, avatar, title, author, category FROM books_backup;");
//                db.execSQL("INSERT INTO users (id, username , password , fullName , email, phone, avatar) " +
//                        "SELECT id, username , password , fullName , email, phone, avatar FROM users_backup;");
//                db.execSQL("DROP TABLE IF EXISTS books_backup");
//                db.execSQL("DROP TABLE IF EXISTS users_backup");
//            }
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
