package com.manzar.telegramweatherbot.util;

import static com.manzar.telegramweatherbot.service.factory.NotificationFactory.createNotification;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

  @ParameterizedTest
  @MethodSource("provideTimes")
  void isValidReturnsTrueIfDateIsValid(String localTimeToParse, boolean expected) {
    assertEquals(expected, TimeUtils.isValid(localTimeToParse));
  }

  private void setValidTime() {
    localTimeMock.when(LocalTime::now).thenReturn(TEST_TIME);
  }

  private static Stream<Arguments> provideTimes() {
    return Stream.of(
        Arguments.of("5181", false),
        Arguments.of("asd", false),
        Arguments.of("15:00", true),
        Arguments.of("23:00", true),
        Arguments.of("25:00", false),
        Arguments.of("12:61", false),
        Arguments.of("17:04", false),
        Arguments.of("00:35", false));
  }
}