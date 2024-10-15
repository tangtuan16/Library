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
        // Lấy thông tin từ intent
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String notificationTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        int requestCode = intent.getIntExtra("requestCode", 0);

        // Tạo Intent để mở ứng dụng khi người dùng nhấn vào thông báo

        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                new Random().nextInt(), // Đảm bảo requestCode duy nhất
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        showNotification(context,title,content);


        // Lưu thông báo vào cơ sở dữ liệu
        saveNotificationToDatabase(context, title, content, notificationTime);
    }



    private void showNotification(Context context, String title, String content) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.download); // Thay đổi với tên file của biểu tượng lớn
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.download) // Đổi thành icon bạn muốn
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Tự động hủy thông báo khi nhấn vào

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Sử dụng ID ngẫu nhiên để có thể hiển thị nhiều thông báo
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


