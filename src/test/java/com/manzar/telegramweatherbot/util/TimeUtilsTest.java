package com.manzar.telegramweatherbot.util;

import static com.manzar.telegramweatherbot.service.factory.NotificationFactory.createNotification;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeUtilsTest {

  private static final LocalTime TEST_TIME = LocalTime.of(12, 0);
  private static MockedStatic<LocalTime> localTimeMock;

  @BeforeAll
  static void setUp() {
    localTimeMock = mockStatic(LocalTime.class,
        Mockito.CALLS_REAL_METHODS);

  }

  @AfterAll
  static void closeResources() {
    localTimeMock.close();
  }

  @Test
  void notificationTimeEqualsCurrentReturnsTrueIfTimeEqualsCurrent() {
    Notification notificationToTrigger = createNotification(
        NotificationType.TOMORROW, TEST_TIME);

    setValidTime();

    assertTrue(TimeUtils.notificationTimeEqualsCurrent(notificationToTrigger));
    localTimeMock.clearInvocations();
  }

  @Test
  void notificationTimeEqualsCurrentReturnsFalseIfTimeIsNotEqualsCurrent() {
    Notification notificationNotToTrigger = createNotification(
        NotificationType.TOMORROW, TEST_TIME.minusHours(1));

    setValidTime();

    assertFalse(TimeUtils.notificationTimeEqualsCurrent(notificationNotToTrigger));
    localTimeMock.clearInvocations();
  }

  private void setValidTime() {
    localTimeMock.when(LocalTime::now).thenReturn(TEST_TIME);
  }


}