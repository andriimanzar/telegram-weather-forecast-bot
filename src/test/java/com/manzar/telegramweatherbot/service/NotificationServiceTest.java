package com.manzar.telegramweatherbot.service;

import static com.manzar.telegramweatherbot.service.factory.NotificationFactory.TEST_CITY;
import static com.manzar.telegramweatherbot.service.factory.NotificationFactory.createNotification;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import com.manzar.telegramweatherbot.repository.NotificationRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  private static final LocalTime TEST_TIME = LocalTime.of(12, 0);
  private static MockedStatic<LocalTime> localTimeMock;
  @Mock
  private WeatherService weatherService;
  @Mock
  private NotificationRepository notificationRepository;
  @Mock
  private MessageSendingService messageSendingService;
  @InjectMocks
  private NotificationService notificationService;

  @BeforeAll
  static void setUp() {
    localTimeMock = mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS);

  }

  @AfterAll
  static void closeResources() {
    localTimeMock.close();
  }

  @Test
  void createOrUpdateNotificationCallsRepositorySaveMethod() {
    Notification notification = createNotification(NotificationType.TOMORROW, TEST_TIME);

    when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

    notificationService.createOrUpdateNotification(notification);

    verify(notificationRepository, times(1)).save(notification);
  }

  @Test
  void deleteNotificationReturnsTrueIfNotificationDeletedRepositoryDeleteMethod() {

    when(notificationRepository.deleteNotificationByUserSessionTelegramId(anyLong())).thenReturn(
        true);

    assertTrue(notificationService.deleteNotifications(1L));
    verify(notificationRepository, times(1)).deleteNotificationByUserSessionTelegramId(anyLong());
  }

  @Test
  void deleteNotificationReturnsFalseIfNotificationsNotExists() {

    assertFalse(notificationService.deleteNotifications(1L));
  }

  @Test
  void sendAllNotificationsSendsNotificationsForValidDateAndOnlyIfNotificationTimeEqualsCurrent() {
    List<Notification> allNotifications = createNotificationsList();
    when(notificationRepository.findAll()).thenReturn(allNotifications);

    setValidTime();
    notificationService.sendAllNotifications();

    verify(weatherService, times(1)).getWeatherForecastByCityNameAndDate(TEST_CITY,
        LocalDate.now());
    verify(weatherService, times(1)).getWeatherForecastByCityNameAndDate(TEST_CITY,
        LocalDate.now().plusDays(1));

    localTimeMock.clearInvocations();
  }

  private void setValidTime() {
    localTimeMock.when(LocalTime::now).thenReturn(TEST_TIME);
  }

  private List<Notification> createNotificationsList() {
    return List.of(createNotification(NotificationType.TOMORROW, TEST_TIME),
        createNotification(NotificationType.MORNING_AND_AFTERNOON, TEST_TIME),
        createNotification(NotificationType.MORNING_AND_AFTERNOON, TEST_TIME.minusHours(1)));
  }
}