package com.example.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Untils.DBManager;

import java.util.ArrayList;
import java.util.List;

public class BookModel {
    private DBManager dbManager;
    private SQLiteDatabase database;

    public BookModel(Context context) {
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
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                bookList.add(new Book(id, avt, title, author, desc));
            }
            cursor.close();
        }
        dbManager.Close();
        return bookList;
    }

    public List<Book> getPopularBooks() {
        List<Book> bookList = new ArrayList<>();
        dbManager.Open();
        database = dbManager.getDatabase();
        String sql = "SELECT * FROM books limit 4 offset 7";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int avt = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                bookList.add(new Book(id, avt, title, author, desc));
            }
            cursor.close();
        }
        dbManager.Close();
        return bookList;
    }
    public List<Book> getDetailBook(int id) {
        List<Book> bookList = new ArrayList<>();
        dbManager.Open();
        database = dbManager.getDatabase();
        String sql = "SELECT * FROM books Where id ="+id;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int avt = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                bookList.add(new Book(id, avt, title, author, desc));
            }
            cursor.close();
        }
        dbManager.Close();
        return bookList;
    }

    public List<Book> getSearchBooks(String edtTitleStr, String edtAuthorStr, String edtDescStr) {
        List<Book> bookList = new ArrayList<>();
        dbManager.Open();
        database = dbManager.getDatabase();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM books WHERE 1=1");
        List<String> selectionArgsList = new ArrayList<>();

        if (!edtTitleStr.isEmpty()) {
            sqlBuilder.append(" AND title LIKE ?");
            selectionArgsList.add("%" + edtTitleStr + "%");
        }
        if (!edtAuthorStr.isEmpty()) {
            sqlBuilder.append(" AND author LIKE ?");
            selectionArgsList.add("%" + edtAuthorStr + "%");
        }
        if (!edtDescStr.isEmpty()) {
            sqlBuilder.append(" AND category LIKE ?");
            selectionArgsList.add("%" + edtDescStr + "%");
        }

        String sql = sqlBuilder.toString();
        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Cursor cursor = database.rawQuery(sql, selectionArgs);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    int avt = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                    String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String bookAuthor = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String bookDesc = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                    bookList.add(new Book(id, avt, bookTitle, bookAuthor, bookDesc));
                }
            } finally {
                cursor.close();
            }
        }

        dbManager.Close();
        return bookList;
    }

    public List<String> getAuthorBooks() {
        List<String> authorBooks = new ArrayList<>();
        String[] authors = {"Nam Cao", "Nguyễn Nhật Ánh", "Xuân Diệu"};
        for (String author : authors) {
            authorBooks.add(author);
        }
//        dbManager.Open();
//        database = dbManager.getDatabase();
//        for (String author : authors) {
//            String sql = "SELECT author FROM books WHERE author = ?";
//            String[] selectionArgs = {author};
//            Cursor cursor = database.rawQuery(sql, selectionArgs);
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("author"));
//                    authorBooks.add(author);
//                }
//                cursor.close();
//            }
//        }
//        dbManager.Close();
        return authorBooks;
    }


    public long insertBook(int avatar, String title, String author, String category) {
        dbManager.Open();
        database = dbManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar", avatar);
        values.put("title", title);
        values.put("author", author);
        values.put("category", category);
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

    public Cursor NotifySearch(String edtTitleStr, String edtAuthorStr, String edtDescStr) {
        dbManager.Open();
        database = dbManager.getDatabase();
        String selection = "";
        List<String> selectionArgs = new ArrayList<>();

        if (!edtTitleStr.isEmpty()) {
            selection += "title LIKE ?";
            selectionArgs.add("%" + edtTitleStr + "%");
        }
        if (!edtAuthorStr.isEmpty()) {
            if (!selection.isEmpty()) {
                selection += " AND ";
            }
            selection += "author LIKE ?";
            selectionArgs.add("%" + edtAuthorStr + "%");
        }
        if (!edtDescStr.isEmpty()) {
            if (!selection.isEmpty()) {
                selection += " AND ";
            }
            selection += "category LIKE ?";
            selectionArgs.add("%" + edtDescStr + "%");
        }

        Cursor cursor = database.query(
                "books",
                null,
                selection,
                selectionArgs.toArray(new String[0]),
                null,
                null,
                null
        );
        return cursor;
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> bookList = new ArrayList<>();
        dbManager.Open();
        SQLiteDatabase database = dbManager.getDatabase();

        String sql = "SELECT * FROM books WHERE author LIKE ?";
        String[] selectionArgs = new String[]{"%" + author + "%"};

        Cursor cursor = database.rawQuery(sql, selectionArgs);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    int avt = cursor.getInt(cursor.getColumnIndexOrThrow("avatar"));
                    String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String bookAuthor = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String bookDesc = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                    bookList.add(new Book(id, avt, bookTitle, bookAuthor, bookDesc));
                }
            } finally {
                cursor.close();
            }
        }

        dbManager.Close();
        return bookList;
    }
}
