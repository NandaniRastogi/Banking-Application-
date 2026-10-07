package com.nandani.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nandani.banking.entitiy.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

}
