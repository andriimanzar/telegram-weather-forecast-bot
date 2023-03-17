package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.ButtonLabel;
import com.manzar.telegramweatherbot.keyboard.ChangeCityKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import org.springframework.stereotype.Component;

/**
 * Handles show forecast message.
 */
@Component
public class ShowForecastHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final ChangeCityKeyboardBuilder changeCityKeyboardBuilder;

  public ShowForecastHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, ChangeCityKeyboardBuilder changeCityKeyboardBuilder) {
    super(messageSendingService, userSessionService);
    this.changeCityKeyboardBuilder = changeCityKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextAndEquals(request.getUpdate(),
        String.valueOf(ButtonLabel.SHOW_FORECAST.getValue()));
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession sessionToUpdate = requestToDispatch.getUserSession();

    if (requestToDispatch.getUserSession().getCity() == null) {
      getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
          "Please enter the name of the city 🌆🌃 "
              + "for which you would like to see the weather forecast 🌦️🌡️.");
      sessionToUpdate.setConversationState(ConversationState.WAITING_FOR_CITY);
    } else {
      getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
          "Please enter the date ⌨️📅 in day/month format (e.g. 05/03) "
              + "for which you would like to see the weather forecast 🌦️🌡️.",
          changeCityKeyboardBuilder.build());
      sessionToUpdate.setConversationState(ConversationState.WAITING_FOR_DATE);
    }
    getUserSessionService().editUserSession(sessionToUpdate);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
