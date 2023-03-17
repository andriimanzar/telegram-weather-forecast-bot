package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.RemoveKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
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
  private final RemoveKeyboardBuilder removeKeyboardBuilder;

  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  public DateEnteredHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, WeatherService weatherService,
      RemoveKeyboardBuilder removeKeyboardBuilder) {
    super(messageSendingService, userSessionService);
    this.weatherService = weatherService;
    this.removeKeyboardBuilder = removeKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_DATE);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String date = requestToDispatch.getUpdate().getMessage().getText();
    if (!DateUtils.isValid(date)) {
      getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
          "📅 Sorry, the date you entered is invalid. "
              + "Please use the format day/month. Example: 15/03");
    } else {

      UserSession userSession = requestToDispatch.getUserSession();
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getUserSessionService().editUserSession(userSession);

      String city = requestToDispatch.getUserSession().getCity();
      getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
          weatherService.getWeatherForecastByCityNameAndDate(city, DateUtils.parse(date)),
          removeKeyboardBuilder.build());
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
