package com.example.Views.Activitys;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.NotificationContract;
import com.example.Models.BookBorrow;
import com.example.Models.Notification;
import com.example.Presenters.NotificationPresenter;
import com.example.Untils.ReturnWorkerNotification;
import com.example.Views.Adapters.NotificationAdapter;
import com.example.btl_libary.R;

import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationContract.View {
    private NotificationPresenter notificationPresenter;
    private RecyclerView rvBooks;
    private Date date = new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rvBooks = findViewById(R.id.rvNotification);
        notificationPresenter = new NotificationPresenter(this, (NotificationContract.View) NotificationActivity.this);
        BookBorrow book = new BookBorrow(1, 1, "Sample Book", 1, "2023-01-01", "2023-01-03");
        notificationPresenter.loadNotification();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ReturnWorkerNotification.REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                BookBorrow book = new BookBorrow(1, 1, "Sample Book", 1, "2023-01-01", "2023-01-03");
                ReturnWorkerNotification.createNotification(this, book);
            } else {
                Toast.makeText(this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void showNotification(List<Notification> notificationList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvBooks.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvBooks.addItemDecoration(itemDecoration);
        rvBooks.setAdapter(new NotificationAdapter(this, notificationList));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }
}
