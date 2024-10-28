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

    public long addDBNotification(String title, String content, String notificationTime) {
        dbManager.Open();
        database = dbManager.getDatabase();
        int user_ID = SharedPreferencesUtil.getUserId(context);
        ContentValues values = new ContentValues();
        values.put("user_id", user_ID);
        values.put("title", title);
        values.put("content", content);
        values.put("notification_time", notificationTime);
        Log.d("NotificationModel", "addDBNotification: " + title);
        long resuilt = database.insert("notifications", null, values);
        dbManager.Close();
        return resuilt;
    }

    public List<Notification> getNotification() {
        dbManager.Open();
        database = dbManager.getDatabase();
        List<Notification> notificationList = new ArrayList<>();
        String sql = "Select * from notifications where user_id = "+ SharedPreferencesUtil.getUserId(context);
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String notificationTime = cursor.getString(cursor.getColumnIndexOrThrow("notification_time"));
                int status = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                notificationList.add(new Notification(id, title, content, notificationTime, status));
            }
            cursor.close();
            dbManager.Close();
        }
        return notificationList;
    }

    public void deleteNotification(int id) {
        dbManager.Open();
        database = dbManager.getDatabase();
        String sql = "Delete from notifications where id = ? and user_id = ?";
        int userId = SharedPreferencesUtil.getUserId(context);
        database.execSQL(sql, new Object[]{id, userId});
        dbManager.Close();
    }

    public void setNotificationStatus2True()
    {
        dbManager.Open();
        database = dbManager.getDatabase();
        String sql = "Update notifications set status = 1 where status = 0";
        database.execSQL(sql);
        dbManager.Close();
    }

}

