package com.example.Views.Activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.NotificationContract;
import com.example.Models.Notification;
import com.example.Presenters.NotificationPresenter;
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
        notificationPresenter.loadNotification();
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
