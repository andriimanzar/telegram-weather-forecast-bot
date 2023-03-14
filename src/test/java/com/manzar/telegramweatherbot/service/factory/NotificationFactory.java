package com.manzar.telegramweatherbot.service.factory;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.Notification;
import com.manzar.telegramweatherbot.model.NotificationType;
import com.manzar.telegramweatherbot.model.UserSession;
import java.time.LocalTime;

public class NotificationFactory {

  public static final String TEST_CITY = "London";

  public static Notification createNotification(NotificationType notificationType,
      LocalTime notificationTime) {
    UserSession userSession = UserSession.builder().telegramId(1L).conversationState(
        ConversationState.CONVERSATION_STARTED).city(TEST_CITY).build();
    return Notification.builder().userSession(userSession)
        .notificationType(notificationType).notificationTime(notificationTime).build();
  }

}
