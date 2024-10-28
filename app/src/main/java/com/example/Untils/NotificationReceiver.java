package com.example.Untils;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.Views.Activitys.BorrowBookActivity;
import com.example.Views.Activitys.NotificationActivity;
import com.example.btl_libary.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    private DBManager dbManager;
    private SQLiteDatabase database;
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String notificationTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        int requestCode = intent.getIntExtra("requestCode", 0);



        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                new Random().nextInt(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        showNotification(context,title,content,pendingIntent);



        saveNotificationToDatabase(context, title, content, notificationTime);
    }



    private void showNotification(Context context, String title, String content,PendingIntent pendingIntent) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.download);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANEL1")
                .setSmallIcon(R.drawable.download)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }

    private void saveNotificationToDatabase(Context context, String title, String content,String notificationTime) {
        DBManager dbManager=new DBManager(context);
        dbManager.Open();
        SQLiteDatabase database= dbManager.getDatabase();
        database = dbManager.getDatabase();
        int user_ID = SharedPreferencesUtil.getUserId(context);
        ContentValues values = new ContentValues();
        values.put("user_id", user_ID);
        values.put("title", title);
        values.put("content", content);
        values.put("notification_time", notificationTime);

        long resuilt = database.insert("notifications", null, values);
        dbManager.Close();

    }


}


