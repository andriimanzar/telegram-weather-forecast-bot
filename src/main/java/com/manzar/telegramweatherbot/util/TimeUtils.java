package com.manzar.telegramweatherbot.util;

import com.manzar.telegramweatherbot.model.Notification;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Provides utility methods for working with time.
 */
public class TimeUtils {

  public static boolean notificationTimeEqualsCurrent(Notification notification) {
    return notification.getNotificationTime().truncatedTo(ChronoUnit.HOURS)
        .equals(LocalTime.now().truncatedTo(ChronoUnit.HOURS));
  }
}
