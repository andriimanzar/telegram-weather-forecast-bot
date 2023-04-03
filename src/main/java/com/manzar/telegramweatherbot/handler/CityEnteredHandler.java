package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.ChangeCityKeyboardBuilder;
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
public class CityEnteredHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final CityNameValidator cityNameValidator;
  private final ChangeCityKeyboardBuilder changeCityKeyboardBuilder;

  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  public CityEnteredHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, LocalizationService localizationService,
      CityNameValidator cityNameValidator, ChangeCityKeyboardBuilder changeCityKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.cityNameValidator = cityNameValidator;
    this.changeCityKeyboardBuilder = changeCityKeyboardBuilder;
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
          "enter.date", changeCityKeyboardBuilder.build());
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}