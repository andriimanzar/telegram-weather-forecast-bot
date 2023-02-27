package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.Constants;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles show forecast message.
 */
@Component
@RequiredArgsConstructor
public class ShowForecastHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final MessageSendingService messageSendingService;
  private final UserSessionService userSessionService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextAndEquals(request.getUpdate(), Constants.SHOW_FORECAST);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession sessionToUpdate = requestToDispatch.getUserSession();
    sessionToUpdate.setConversationState(ConversationState.WAITING_FOR_CITY);
    userSessionService.editUserSession(sessionToUpdate);

    messageSendingService.sendMessage(requestToDispatch.getChatId(),
        "Please, enter the name of the city for which you want to see the weather forecast⛅");
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
