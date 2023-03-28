package com.manzar.telegramweatherbot.service.factory;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.Language;
import com.manzar.telegramweatherbot.model.UserSession;

public class UserSessionFactory {

  public static UserSession createTestUserSession() {
    return UserSession.builder().telegramId(1L)
        .conversationState(ConversationState.CONVERSATION_STARTED).language(Language.EN).unitSystem(
            UnitSystem.METRIC).build();
  }

}
