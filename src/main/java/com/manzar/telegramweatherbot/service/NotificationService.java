package com.manzar.telegramweatherbot.service;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import com.manzar.telegramweatherbot.repository.NotificationRepository;
import com.manzar.telegramweatherbot.util.TimeUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * A service for managing notifications for a user.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final WeatherService weatherService;
  private final NotificationRepository notificationRepository;
  private final MessageSendingService messageSendingService;

  public Notification createOrUpdateNotification(Notification notification) {
    return notificationRepository.save(notification);
  }

  public boolean deleteNotifications(Long telegramId) {
    return notificationRepository.deleteNotificationByUserSessionTelegramId(telegramId);
  }

  /**
   * A method to send notifications to the users on an hourly schedule.
   */
  @Scheduled(cron = "@hourly")
  public void sendAllNotifications() {
    notificationRepository.findAll().stream().filter(TimeUtils::notificationTimeEqualsCurrent)
        .forEach(this::sendNotification);
  }


  private void sendNotification(Notification notification) {
    String cityName = notification.getUserSession().getCity();
    String formattedForecast = weatherService.getWeatherForecastByCityNameAndDate(cityName,
        calculateForecastDate(notification.getNotificationType()));

    messageSendingService.sendMessage(notification.getChatId(), formattedForecast);
  }

  private LocalDate calculateForecastDate(NotificationType notificationType) {
    if (notificationType.equals(NotificationType.TOMORROW)) {
      return LocalDate.now().plusDays(1);
    } else {
      return LocalDate.now();
    }
  }
}

