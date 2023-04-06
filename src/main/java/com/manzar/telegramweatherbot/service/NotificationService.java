package com.manzar.telegramweatherbot.service;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.repository.NotificationRepository;
import com.manzar.telegramweatherbot.util.DateUtils;
import com.manzar.telegramweatherbot.util.TimeUtils;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * A service for managing notifications for a user.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final MessageSendingService messageSendingService;
  private final WeatherService weatherService;

  /**
   * Creates a morning and afternoon weather forecast notification for the given user session and
   * chat ID.
   *
   * @param userSession the user session for which the notifications are being created
   */
  public boolean createMorningAndAfternoonNotification(UserSession userSession) {
    List<Notification> morningAndAfternoonUserNotifications =
        notificationRepository.findAllByUserSessionAndNotificationType(userSession,
            NotificationType.MORNING_AND_AFTERNOON);

    if (morningAndAfternoonUserNotifications.isEmpty()) {
      createNotification(userSession, NotificationType.MORNING_AND_AFTERNOON,
          Optional.of(LocalTime.of(7, 0)));
      createNotification(userSession, NotificationType.MORNING_AND_AFTERNOON,
          Optional.of(LocalTime.of(15, 0)));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Creates a weather forecast notification for tomorrow for the given user session, chat ID and
   * time.
   *
   * @param userSession      the user session for which the notifications are being created
   * @param notificationTime the time, at which the notification will trigger
   */
  public boolean createTomorrowNotification(UserSession userSession,
      Optional<LocalTime> notificationTime) {
    List<Notification> allUserNotificationsForTomorrow =
        notificationRepository.findAllByUserSessionAndNotificationType(
            userSession, NotificationType.TOMORROW);

    if (allUserNotificationsForTomorrow.isEmpty() || notificationTime.isEmpty()) {
      createNotification(userSession, NotificationType.TOMORROW,
          notificationTime);
      return true;
    } else {
      return createTomorrowNotificationIfOtherNotificationsExistsAndTimeIsPresent(userSession,
          allUserNotificationsForTomorrow, notificationTime);
    }
  }

  @Transactional
  public void deleteNotifications(Long telegramId) {
    notificationRepository.deleteAllByUserSessionTelegramId(telegramId);
  }

  /**
   * Sends notifications to the users on an hourly schedule.
   */
  @Scheduled(cron = "@hourly")
  public void sendAllNotifications() {
    notificationRepository.findAll().stream()
        .filter(notification -> notification.getNotificationTime() != null)
        .filter(TimeUtils::notificationTimeEqualsCurrent)
        .forEach(this::sendNotification);
    log.info("Sending notifications");
  }


  private void sendNotification(Notification notification) {
    UserSession userSession = notification.getUserSession();
    String formattedForecast = weatherService.getWeatherForecastByUserSessionAndDate(
        userSession,
        DateUtils.calculateForecastDate(notification.getNotificationType()));

    messageSendingService.sendMessage(userSession, formattedForecast);
  }

  private void createNotification(UserSession userSession,
      NotificationType notificationType, Optional<LocalTime> notificationTime) {
    Notification notification;

    if (notificationTime.isPresent()) {
      notification = Notification.builder().userSession(userSession)
          .chatId(userSession.getTelegramId())
          .notificationType(notificationType).notificationTime(notificationTime.get())
          .build();
    } else {
      notification = Notification.builder().userSession(userSession)
          .notificationType(notificationType).build();
    }
    notificationRepository.save(notification);
  }

  private boolean createTomorrowNotificationIfOtherNotificationsExistsAndTimeIsPresent(
      UserSession userSession, List<Notification> allUserNotificationsForTomorrow,
      Optional<LocalTime> notificationTime) {
    Predicate<Notification> notificationTimeEqualsGiven =
        notification -> notification.getNotificationTime().equals(notificationTime.get());
    boolean notificationWithSameTimeExists = allUserNotificationsForTomorrow.stream()
        .filter(notification -> notification.getNotificationTime() != null)
        .anyMatch(notificationTimeEqualsGiven);

    if (notificationWithSameTimeExists) {
      return false;
    } else {
      createNotification(userSession, NotificationType.TOMORROW, notificationTime);
      return true;
    }
  }
}
