//package com.example.Untils;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Build;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.example.Models.BookBorrow;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//public class ReturnReminderWorker extends Worker {
//    private DBManager dbManager;
//    private SQLiteDatabase database;
//
//    public ReturnReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
//        super(context, params);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        Calendar today = Calendar.getInstance();
//        List<BookBorrow> dueSoonBooks = getDueSoonBooks(today);
//        for (BookBorrow book : dueSoonBooks) {
//            createNotification(book);
//            saveNotificationToDatabase(book);
//        }
//        return Result.success();
//    }
//
//    private List<BookBorrow> getDueSoonBooks(Calendar today) {
//        dbManager.Open();
//        database = dbManager.getDatabase();
//        List<BookBorrow> bookBorrowList = new ArrayList<>();
//        String sql = "Select * from bookborrow where date_Payment BETWEEN today AND (today + 3)";
//        Cursor cursor = database.rawQuery(sql, null);
//        if(cursor != null){
//            while (cursor.moveToNext()){
//                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_ID"));
//                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_ID"));
//                int bookTotal = cursor.getInt(cursor.getColumnIndexOrThrow("book_Total"));
//
//            }
//        }
//        return null;
//    }
//
//    private void createNotification(BookBorrow book) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "RETURN_REMINDER_CHANNEL",
//                    "Return Reminder Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setDescription("Channel for return reminders");
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "RETURN_REMINDER_CHANNEL")
//    //            .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle("Sắp đến hạn trả sách!")
//    //            .setContentText("Cuốn sách '" + book.getBookTitle() + "' sắp đến hạn trả vào ngày " + book.getReturnDate())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//   //     notificationManager.notify((int) book.getId(), builder.build());
//    }
//
//    private void saveNotificationToDatabase(BookBorrow book) {
//        // Lưu thông tin thông báo vào bảng notifications
//        // Ví dụ: INSERT INTO notifications (title, content, timestamp) VALUES (...)
//    }
//}
