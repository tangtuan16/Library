package com.example.Untils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.Models.BookBorrow;
import com.example.btl_libary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReturnWorkerNotification extends Worker {
    private DBManager dbManager;
    private SQLiteDatabase database;
    private Context context;
    public static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

    public ReturnWorkerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams, DBManager dbManager) {
        super(context, workerParams);
        this.dbManager = dbManager;
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Calendar today = Calendar.getInstance();
        List<BookBorrow> dueSoonBooks = getDueSoonBooks(today);
        for (BookBorrow book : dueSoonBooks) {
            createNotification(context, book);
            saveNotificationToDatabase(book);
        }
        return Result.success();
    }

    private List<BookBorrow> getDueSoonBooks(Calendar today) {
        dbManager.Open();
        database = dbManager.getDatabase();
        int user_ID = SharedPreferencesUtil.getUserId(context);
        List<BookBorrow> bookBorrowList = new ArrayList<>();
        String sql = "SELECT bb.book_ID, b.title, bb.book_total, bb.date_Borrow, bb.date_Return " +
                "FROM bookborrow bb " +
                "JOIN books b ON bb.book_ID = b.id " +
                "WHERE strftime('%Y-%m-%d', bb.date_Return) BETWEEN date('now') AND date('now', '+3 days') " +
                "AND bb.user_ID = ?";

        String[] selectionArgs = {String.valueOf(user_ID)};
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_ID"));
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_ID"));
                int bookTotal = cursor.getInt(cursor.getColumnIndexOrThrow("book_total"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String borrowedDate = cursor.getString(cursor.getColumnIndexOrThrow("date_Borrow"));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("date_Return"));
                bookBorrowList.add(new BookBorrow(bookId, userId, title, bookTotal, borrowedDate, returnDate));
            }
            cursor.close();
            Log.d("ReturnWorkerNotification", "getDueSoonBooks: " + bookBorrowList.size());
        }
        return bookBorrowList;
    }

    public static void createNotification(Context context, BookBorrow book) {
        if (!isNotificationValid(book)) {
            Log.e("Notification", "Book data is invalid. Notification not created.");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo NotificationChannel cho Android 8.0 trở lên
            NotificationChannel channel = new NotificationChannel(
                    "RETURN_REMINDER_CHANNEL",
                    "Return Reminder Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel for return reminders");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RETURN_REMINDER_CHANNEL")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Sắp đến hạn trả sách!")
                .setContentText("Cuốn sách '" + book.getTitle() + "' sắp đến hạn trả vào ngày " + book.getReturnDate())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            return;
        }
        notificationManager.notify((int) book.getId(), builder.build());
    }

    private static boolean isNotificationValid(BookBorrow book) {
        return book != null && book.getId() > 0 && book.getTitle() != null && !book.getTitle().isEmpty();
    }


    private void saveNotificationToDatabase(BookBorrow book) {
        // Lưu thông tin thông báo vào bảng notifications
        // Ví dụ: INSERT INTO notifications (title, content, timestamp) VALUES (...)
    }
}

