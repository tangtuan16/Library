package com.example.Models;

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
        }
        return userList;
    }
}
