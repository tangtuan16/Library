package com.example.Untils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        private static final int DATABASE_VERSION = 15; // Tăng version để trigger onUpgrade

        // Bảng books hiện tại
        private static final String TABLE_BOOKS = "books";
        private static final String TABLE_CREATE_BOOK =
                "CREATE TABLE " + TABLE_BOOKS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "avatar INTEGER, " +
                        "title TEXT, " +
                        "author TEXT, " +
                        "description TEXT, " +
                        "firebase_uid TEXT);"; // Thêm trường firebase_uid

        // Bảng user_books để theo dõi sách của từng người dùng
        private static final String TABLE_USER_BOOKS = "user_books";
        private static final String TABLE_CREATE_USER_BOOKS =
                "CREATE TABLE " + TABLE_USER_BOOKS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "firebase_uid TEXT NOT NULL, " +
                        "book_id INTEGER, " +
                        "reading_status TEXT, " + // Ví dụ: 'reading', 'completed', 'want_to_read'
                        "last_read_date DATETIME, " +
                        "FOREIGN KEY(book_id) REFERENCES books(id), " +
                        "UNIQUE(firebase_uid, book_id));";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_BOOK);
            db.execSQL(TABLE_CREATE_USER_BOOKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 9) {
                db.execSQL("CREATE TABLE books_backup AS SELECT * FROM books;");
                db.execSQL("DROP TABLE IF EXISTS books");
                db.execSQL("DROP TABLE IF EXISTS user_books");
                onCreate(db);
                db.execSQL("INSERT INTO books (id, avatar, title, author, description) " +
                        "SELECT id, avatar, title, author, description FROM books_backup;");
                db.execSQL("DROP TABLE IF EXISTS books_backup");
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

    public long InsertBook(ContentValues values, String firebaseUid) {
        values.put("firebase_uid", firebaseUid);
        return database.insert("books", null, values);
    }

//    public int UpdateBook(ContentValues values, String whereClause, String[] whereArgs, String firebaseUid) {
//        Cursor cursor = database.query("books",
//                new String[]{"firebase_uid"},
//                whereClause,
//                whereArgs,
//                null, null, null);
//
//        if (cursor.moveToFirst()) {
//            String bookOwnerUid = cursor.getString(cursor.getColumnIndex("firebase_uid"));
//            if (firebaseUid.equals(bookOwnerUid)) {
//                return database.update("books", values, whereClause, whereArgs);
//            }
//        }
//        cursor.close();
//        return 0; // Trả về 0 nếu không có quyền update
//    }

//    // Phương thức xóa sách với kiểm tra quyền
//    public int DeleteBook(String whereClause, String[] whereArgs, String firebaseUid) {
//        // Kiểm tra quyền trước khi delete
//        Cursor cursor = database.query("books",
//                new String[]{"firebase_uid"},
//                whereClause,
//                whereArgs,
//                null, null, null);
//
//        if (cursor.moveToFirst()) {
//            String bookOwnerUid = cursor.getString(cursor.getColumnIndex("firebase_uid"));
//            if (firebaseUid.equals(bookOwnerUid)) {
//                return database.delete("books", whereClause, whereArgs);
//            }
//        }
//        cursor.close();
//        return 0; // Trả về 0 nếu không có quyền delete
//    }

    // Thêm sách vào thư viện người dùng
    public long AddBookToUserLibrary(String firebaseUid, long bookId, String readingStatus) {
        ContentValues values = new ContentValues();
        values.put("firebase_uid", firebaseUid);
        values.put("book_id", bookId);
        values.put("reading_status", readingStatus);
        values.put("last_read_date", System.currentTimeMillis());
        return database.insert("user_books", null, values);
    }

    // Lấy danh sách sách của người dùng
    public Cursor GetUserBooks(String firebaseUid) {
        return database.rawQuery(
                "SELECT books.*, user_books.reading_status, user_books.last_read_date " +
                        "FROM books " +
                        "INNER JOIN user_books ON books.id = user_books.book_id " +
                        "WHERE user_books.firebase_uid = ?",
                new String[]{firebaseUid}
        );
    }
}
