package com.manzar.telegramweatherbot.service.factory;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class UserRequestFactory {

  public static UserRequest createUserRequest(Update update, UserSession userSession) {
    return new UserRequest(update, 1L, userSession);
  }

  public static UserRequest createRequestWithStateAndText(ConversationState conversationState,
      String text) {
    UserSession userSession = UserSessionFactory.createUserSessionWithConversationState(
        conversationState);

    User user = new User();
    user.setId(1L);

    Message message = new Message();
    message.setText(text);
    message.setFrom(user);

    Update update = new Update();
    update.setMessage(message);
    return UserRequestFactory.createUserRequest(update, userSession);
  }

  public static UserRequest createRequestWithStateAndTestMessage(
      ConversationState conversationState) {
    return createRequestWithStateAndText(conversationState, "test");
  }
}
