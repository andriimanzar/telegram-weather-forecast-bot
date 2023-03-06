package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.RemoveKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.WeatherService;
import com.manzar.telegramweatherbot.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles user message, that contains date.
 */
@Component
@RequiredArgsConstructor
public class DateEnteredHandler extends AbstractUserRequestHandler implements UserRequestHandler {


  private final MessageSendingService messageSendingService;
  private final WeatherService weatherService;
  private final UserSessionService userSessionService;
  private final RemoveKeyboardBuilder removeKeyboardBuilder;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_DATE);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String date = requestToDispatch.getUpdate().getMessage().getText();
    if (!DateUtils.isValid(date)) {
      messageSendingService.sendMessage(requestToDispatch.getChatId(),
          "Please, enter the date in day/month format(e.g. 20/12)");
    } else {

      UserSession userSession = requestToDispatch.getUserSession();
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      userSessionService.editUserSession(userSession);

      String city = requestToDispatch.getUserSession().getCity();
      messageSendingService.sendMessage(requestToDispatch.getChatId(),
          weatherService.getWeatherForecastByCityNameAndDate(city, DateUtils.parse(date)),
          removeKeyboardBuilder.build());
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
