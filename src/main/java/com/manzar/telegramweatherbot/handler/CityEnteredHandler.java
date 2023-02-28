package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.CityNameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles user message, that contains city name.
 */
@Component
@RequiredArgsConstructor
public class CityEnteredHandler extends AbstractUserRequestHandler {

  private final CityNameValidator cityNameValidator;
  private final MessageSendingService messageSendingService;
  private final UserSessionService userSessionService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState().equals(
        ConversationState.WAITING_FOR_CITY);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String city = requestToDispatch.getUpdate().getMessage().getText();

    if (!cityNameValidator.enteredCityExists(city)) {
      messageSendingService.sendMessage(requestToDispatch.getChatId(),
          "Entered city not found. Please, try again");
    } else {
      UserSession userSession = requestToDispatch.getUserSession();
      userSession.setCity(city);
      userSession.setConversationState(ConversationState.WAITING_FOR_DATE);
      userSessionService.editUserSession(userSession);

      messageSendingService.sendMessage(requestToDispatch.getChatId(),
          "Now, write the date for which you would like to see the forecast in day/month format");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}