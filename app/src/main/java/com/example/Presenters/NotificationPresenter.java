package com.example.Presenters;

import android.content.Context;

import com.example.Contracts.NotificationContract;
import com.example.Models.Notification;
import com.example.Models.NotificationModel;

import java.util.Date;
import java.util.List;

public class NotificationPresenter {
    private NotificationContract.View view;
    private Context context;
    private NotificationModel notificationModel;

    public NotificationPresenter(Context context, NotificationContract.View view) {
        this.context = context;
        this.notificationModel = new NotificationModel(context);
        this.view = view;
    }

    public void addNotification(String title, String content, Date date) {
        long id = notificationModel.addDBNotification(title, content, date);
        if (id < 0) {
            view.showError("Thêm thất bại !");
        }
    }

    public void loadNotification() {
        List<Notification> notificationList = notificationModel.getNotification();
        view.showNotification(notificationList);
    }

    public void deleteNotification(int id) {
        notificationModel.deleteNotification(id);
    }
}
