package com.manzar.telegramweatherbot.service.factory;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.Language;
import com.manzar.telegramweatherbot.model.UserSession;

public class UserSessionFactory {

  public static UserSession createTestUserSession() {
    return createUserSession(ConversationState.CONVERSATION_STARTED);
  }

  public static UserSession createUserSessionWithConversationState(
      ConversationState conversationState) {
    return createUserSession(conversationState);
  }

  private static UserSession createUserSession(ConversationState conversationState) {
    return UserSession.builder().telegramId(1L).city("London")
        .conversationState(conversationState).language(Language.EN).unitSystem(
            UnitSystem.METRIC).build();
  }

}
