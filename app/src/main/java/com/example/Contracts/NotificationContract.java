package com.example.Contracts;

import com.example.Models.Notification;
import com.example.Models.NotificationModel;

import java.util.List;

public interface NotificationContract {
    interface View {
        void showNotification(List<Notification> notificationList);

        void showError(String message);
    }

    interface Presenter {

    }
}
