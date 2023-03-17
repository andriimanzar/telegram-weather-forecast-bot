package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.ButtonLabel;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import org.springframework.stereotype.Component;

/**
 * Handles set notification user request.
 */
@Component
public class SetNotificationsHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  public SetNotificationsHandler(
      MessageSendingService messageSendingService,
      UserSessionService userSessionService) {
    super(messageSendingService, userSessionService);
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextAndEquals(request.getUpdate(),
        String.valueOf(ButtonLabel.SET_NOTIFICATIONS.getValue()));
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    if (userSession.getCity() == null) {
      getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
          "🌍 Please specify the city first to start receiving weather forecast notifications");
    } else {
      userSession.setConversationState(ConversationState.WAITING_FOR_NOTIFICATION_TYPE);
      getUserSessionService().editUserSession(userSession);
      getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
          "🔔 Please choose the type of weather notifications you want to receive from this bot");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
