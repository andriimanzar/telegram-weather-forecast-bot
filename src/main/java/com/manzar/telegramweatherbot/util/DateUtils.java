package com.manzar.telegramweatherbot.util;

import com.manzar.telegramweatherbot.model.NotificationType;
import java.time.LocalDate;

/**
 * Provides utility methods for working with dates.
 */
public class DateUtils {

  public static boolean isValid(String date) {
    return date.matches("^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])$");
  }

  /**
   * Parses a date string using the specified format string.
   */
  public static LocalDate parse(String date) {
    int day = Integer.parseInt(date.split("/")[0]);
    int month = Integer.parseInt(date.split("/")[1]);
    return LocalDate.of(LocalDate.now().getYear(), month, day);
  }

  /**
   * Calculates the forecast date based on the given notification type.
   */
  public static LocalDate calculateForecastDate(NotificationType notificationType) {
    if (notificationType.equals(NotificationType.TOMORROW)) {
      return LocalDate.now().plusDays(1);
    } else {
      return LocalDate.now();
    }
  }
}
