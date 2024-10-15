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
        private static final int DATABASE_VERSION = 34;

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
              //  db.execSQL("INSERT INTO books (id, avatar, title, author, category) " +
              //          "SELECT id, avatar, title, author, category FROM books_backup;");
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


    public int getNumberOfBorrowedBooks(int userId,int bookId) {
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
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }


    //code hùng
    public List<BorrowedBook> GetAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooksList = new ArrayList<>();
        Cursor cursor = null;
        try {

            String query = "SELECT bb.book_ID, b.avatar, b.title, b.author, b.category,bb.book_Total, " +
                    "bb.date_Borrow, bb.date_Return, bb.isInLibrary " +
                    "FROM bookborrow bb " +
                    "JOIN books b ON bb.book_ID = b.id " +
                    "WHERE bb.user_ID = 1";

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
        return borrowedBooksList;

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