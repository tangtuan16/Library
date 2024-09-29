package com.example.Untils;

import android.content.Context;
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
        private static final int DATABASE_VERSION = 14;
        private static final String TABLE_CREATE_BOOK = "CREATE TABLE books (id INTEGER PRIMARY KEY AUTOINCREMENT, avatar INTEGER, title TEXT, author TEXT,category TEXT);";
        private static final String TABLE_CREATE_NOTE = "CREATE TABLE notes (" +
                "id_note INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL)";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_BOOK);
            db.execSQL(TABLE_CREATE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS books");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public void Open() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void Close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        database = null;
    }

    public boolean isDatabaseOpen() {
        return database != null && database.isOpen();
    }

}
