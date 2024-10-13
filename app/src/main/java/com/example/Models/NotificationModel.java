package com.example.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.Untils.DBManager;
import com.example.Untils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationModel {

    private DBManager dbManager;
    private SQLiteDatabase database;
    private Context context;

    public NotificationModel(Context context) {
        this.context = context;
        this.dbManager = new DBManager(context);
    }

    public long addDBNotification(String title, String content, Date notificationTime) {
        dbManager.Open();
        database = dbManager.getDatabase();
        int user_ID = SharedPreferencesUtil.getUserId(context);
        List<Notification> notificationList = new ArrayList<>();
        ContentValues values = new ContentValues();
        values.put("user_id", user_ID);
        values.put("title", title);
        values.put("content", content);
        values.put("notification_time", notificationTime.getTime());
        Log.d("NotificationModel", "addDBNotification: " + title);
        long resuilt = database.insert("notifications", null, values);
        dbManager.Close();
        return resuilt;
    }

    public List<Notification> getNotification() {
        dbManager.Open();
        database = dbManager.getDatabase();
        List<Notification> notificationList = new ArrayList<>();
        String sql = "Select * from notifications";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                long dateInMillis = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
                Date date = new Date(dateInMillis);
                notificationList.add(new Notification(id, title, content, date));
            }
            cursor.close();
            dbManager.Close();
        }
        return notificationList;
    }

    public void deleteNotification(int id) {
        dbManager.Open();
        database = dbManager.getDatabase();
        String sql = "Delete from notifications where id = ?";
        database.execSQL(sql, new Object[]{id});
        dbManager.Close();
    }

}

