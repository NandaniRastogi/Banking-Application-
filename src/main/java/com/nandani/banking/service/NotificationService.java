package com.nandani.banking.service;

import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.Notification.NotificationChannel;
import com.nandani.banking.entitiy.Notification.NotificationType;

public interface NotificationService {

    void createNotification(
            User user,
            String message,
            NotificationType type,
            NotificationChannel channel);

}
