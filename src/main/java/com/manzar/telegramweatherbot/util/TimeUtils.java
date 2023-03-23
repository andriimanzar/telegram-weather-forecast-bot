package com.manzar.telegramweatherbot.util;

import com.manzar.telegramweatherbot.model.Notification;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Provides utility methods for working with time.
 */
public class TimeUtils {

  public static boolean notificationTimeEqualsCurrent(Notification notification) {
    return notification.getNotificationTime().truncatedTo(ChronoUnit.HOURS)
        .equals(LocalTime.now().truncatedTo(ChronoUnit.HOURS));
  }

  /**
   * Validates whether a given string can be parsed into a {@link java.time.LocalTime} object with 0
   * minutes.
   *
   * @param localTimeToParse the string to be validated
   * @return true if the string can be parsed into a {@link java.time.LocalTime} object with 0
   *     minutes, false otherwise
   */
  public static boolean isValid(String localTimeToParse) {
    try {
      LocalTime localTime = LocalTime.parse(localTimeToParse);
      return localTime.getMinute() == 0;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
