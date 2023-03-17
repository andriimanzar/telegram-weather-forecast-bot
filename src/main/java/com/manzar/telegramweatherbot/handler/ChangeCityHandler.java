package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.ButtonLabel;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import org.springframework.stereotype.Component;

/**
 * Handles user request to change city.
 */
@Component
public class ChangeCityHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  public ChangeCityHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService) {
    super(messageSendingService, userSessionService);
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUpdate().getMessage().getText()
        .equals(ButtonLabel.CHANGE_CITY.getValue());
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
        "Please enter the name of the city 🌆🌃 "
            + "for which you would like to see the weather forecast 🌦️🌡️.");
    UserSession sessionToUpdate = requestToDispatch.getUserSession();
    sessionToUpdate.setConversationState(ConversationState.WAITING_FOR_CITY);
    getUserSessionService().editUserSession(sessionToUpdate);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
