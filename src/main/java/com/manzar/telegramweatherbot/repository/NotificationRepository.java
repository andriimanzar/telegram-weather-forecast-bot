package com.manzar.telegramweatherbot.repository;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import com.manzar.telegramweatherbot.model.UserSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Notification repository.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findAllByUserSessionAndNotificationType(UserSession userSession,
      NotificationType notificationType);

  void deleteAllByUserSessionTelegramId(Long telegramId);
}
