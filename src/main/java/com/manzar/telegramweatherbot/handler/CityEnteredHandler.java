package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.CityNameValidator;
import org.springframework.stereotype.Component;

/**
 * Handles user message, that contains city name.
 */
@Component
public class CityEnteredHandler extends AbstractUserRequestHandler {

  private final CityNameValidator cityNameValidator;

  public CityEnteredHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      CityNameValidator cityNameValidator) {
    super(messageSendingService, userSessionService, localizationService);
    this.cityNameValidator = cityNameValidator;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_CITY);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String city = requestToDispatch.getUpdate().getMessage().getText();
    UserSession userSession = requestToDispatch.getUserSession();

    if (!cityNameValidator.enteredCityExists(city)) {
      getMessageSendingService().sendMessage(userSession,
          "city.not.found");
    } else {
      userSession.setCity(city);
      userSession.setConversationState(ConversationState.WAITING_FOR_DATE);
      getUserSessionService().editUserSession(userSession);

      getMessageSendingService().sendMessage(userSession,
          "enter.date");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}