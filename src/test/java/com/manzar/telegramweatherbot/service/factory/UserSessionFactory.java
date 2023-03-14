package com.manzar.telegramweatherbot.service.factory;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserSession;

public class UserSessionFactory {

  public static UserSession createTestUserSession() {
    return UserSession.builder().telegramId(1L)
        .conversationState(ConversationState.CONVERSATION_STARTED).build();
  }

}
