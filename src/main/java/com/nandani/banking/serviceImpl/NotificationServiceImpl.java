package com.nandani.banking.serviceImpl;

import com.nandani.banking.entitiy.Notification.NotificationChannel;
import com.nandani.banking.entitiy.Notification.NotificationStatus;
import com.nandani.banking.entitiy.Notification.NotificationType;
import com.nandani.banking.repository.NotificationRepo;

import org.springframework.stereotype.Service;

import com.nandani.banking.entitiy.Notification;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.service.EmailService;
import com.nandani.banking.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final EmailService emailService;

    @Override
    public void createNotification(User user, String message, NotificationType type, NotificationChannel channel) {

        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setNotificationChannel(channel);
        notification.setNotificationStatus(NotificationStatus.PENDING);
        notification.setNotificationType(type);
        notification.setUser(user);

        if (channel == NotificationChannel.EMAIL) {
            System.out.print("Notification sent");
            emailService.sendEmail(
                    user.getEmail(),
                    type.name(),
                    message);
        }

        notification.setNotificationStatus(NotificationStatus.SENT);

        notificationRepo.save(notification);

    }

}
