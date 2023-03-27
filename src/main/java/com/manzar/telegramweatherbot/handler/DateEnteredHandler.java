package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.WeatherService;
import com.manzar.telegramweatherbot.util.DateUtils;
import org.springframework.stereotype.Component;

/**
 * Handles user message, that contains date.
 */
@Component
public class DateEnteredHandler extends AbstractUserRequestHandler implements UserRequestHandler {


  private final WeatherService weatherService;
  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;


  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */

  public DateEnteredHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      WeatherService weatherService, StartMenuKeyboardBuilder startMenuKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.weatherService = weatherService;
    this.startMenuKeyboardBuilder = startMenuKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_DATE);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String date = requestToDispatch.getUpdate().getMessage().getText();
    UserSession userSession = requestToDispatch.getUserSession();
    if (!DateUtils.isValid(date)) {
      getMessageSendingService().sendMessage(userSession,
          "invalid.date");
    } else {

      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getUserSessionService().editUserSession(userSession);

      String formattedForecast = weatherService.getWeatherForecastByCityNameAndDate(userSession,
          DateUtils.parse(date));
      getMessageSendingService().sendMessage(userSession, formattedForecast,
          startMenuKeyboardBuilder.build());

    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
