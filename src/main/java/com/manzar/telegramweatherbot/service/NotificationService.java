package com.manzar.telegramweatherbot.service;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.repository.NotificationRepository;
import com.manzar.telegramweatherbot.util.DateUtils;
import com.manzar.telegramweatherbot.util.TimeUtils;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.Optional;
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

  /**
   * Creates a morning and afternoon weather forecast notification for the given user session and
   * chat ID.
   *
   * @param userSession the user session for which the notifications are being created
   * @param chatId      the chat ID associated with the user session
   */
  public void createMorningAndAfternoonNotification(UserSession userSession, Long chatId) {
    createNotification(userSession, chatId, NotificationType.MORNING_AND_AFTERNOON,
        Optional.of(LocalTime.of(7, 0)));
    createNotification(userSession, chatId, NotificationType.MORNING_AND_AFTERNOON,
        Optional.of(LocalTime.of(14, 0)));
  }

  /**
   * Creates a weather forecast notification for tomorrow for the given user session, chat ID and
   * time.
   *
   * @param userSession      the user session for which the notifications are being created
   * @param chatId           the chat ID associated with the user session
   * @param notificationTime the time, at which the notification will trigger
   */
  public void createTomorrowNotification(UserSession userSession, Long chatId,
      Optional<LocalTime> notificationTime) {
    createNotification(userSession, chatId, NotificationType.TOMORROW,
        notificationTime);
  }

  @Transactional
  public void deleteNotifications(Long telegramId) {
    notificationRepository.deleteAllByUserSessionTelegramId(telegramId);
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
        DateUtils.calculateForecastDate(notification.getNotificationType()));

    messageSendingService.sendMessage(notification.getChatId(), formattedForecast);
  }

  private void createNotification(UserSession userSession, Long chatId,
      NotificationType notificationType, Optional<LocalTime> notificationTime) {
    Notification notification;
    if (notificationTime.isPresent()) {
      notification = Notification.builder().userSession(userSession).chatId(chatId)
          .notificationType(notificationType).notificationTime(notificationTime.get())
          .build();
    } else {
      notification = Notification.builder().userSession(userSession)
          .notificationType(notificationType).build();
    }
    notificationRepository.save(notification);
  }
}

