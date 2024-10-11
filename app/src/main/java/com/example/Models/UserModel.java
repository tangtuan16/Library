package com.example.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.Untils.DBManager;
import com.example.Untils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private Context context;
    private DBManager dbManager;
    private SQLiteDatabase database;
    private int userId;

    public UserModel(Context context) {
        this.dbManager = new DBManager(context);
        this.context = context;
    }

    public List<User> getAllUsers() {
        userId = SharedPreferencesUtil.getUserId(context);
        Log.d("CheckID", "userIdModel: " + userId);
        List<User> userList = new ArrayList<>();

        dbManager.Open();
        database = dbManager.getDatabase();

        String sql = "SELECT * FROM users WHERE id = ?";
        String[] arg = new String[]{String.valueOf(userId)};
        Cursor cursor = database.rawQuery(sql, arg);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                byte[] avt = cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
                userList.add(new User(id, null, null, name, null, null, avt));
            }
            cursor.close();
        }

        dbManager.Close(); // Close the database connection

        return userList;
    }


    public long registerUser(String username, String password, String fullName, String email, String phone) {
        dbManager.Open();
        database = dbManager.getDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("fullName", fullName);
        values.put("email", email);
        values.put("phone", phone);

        long result = database.insert("users", null, values);

        dbManager.Close(); // Close the database connection

        return result;
    }

    public boolean loginUser(String username, String password) {
        dbManager.Open();
        database = dbManager.getDatabase();

        String query = "SELECT * FROM users WHERE username=? AND password=?";
        Cursor cursor = database.rawQuery(query, new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();

        dbManager.Close(); // Close the database connection

        return isValid;
    }

    public int updateUser(int userId, String password, String fullName, String email, String phone, byte[] avatar) {
        dbManager.Open();
        database = dbManager.getDatabase();

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

        int rowsAffected = database.update("users", values, "id=?", new String[]{String.valueOf(userId)});

        dbManager.Close(); // Close the database connection

        return rowsAffected;
    }

    public User getUserById(int userId) {
        dbManager.Open();
        database = dbManager.getDatabase();

        Cursor cursor = database.query("users", null, "id=?", new String[]{String.valueOf(userId)}, null, null, null);
        User user = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"))
                );
            }
            cursor.close();
        }

        dbManager.Close(); // Close the database connection

        return user;
    }

    public User getUserByUsername(String username) {
        dbManager.Open();
        database = dbManager.getDatabase();

        Cursor cursor = database.query("users", null, "username=?", new String[]{username}, null, null, null);
        User user = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"))
                );
            }
            cursor.close();
        }

        dbManager.Close(); // Close the database connection

        return user;
    }
}
