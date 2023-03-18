package com.manzar.telegramweatherbot.repository;

import com.manzar.telegramweatherbot.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Notification repository.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  void deleteAllByUserSessionTelegramId(Long telegramId);
}
