package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.CancelKeyboardBuilder;
import com.manzar.telegramweatherbot.keyboard.ChangeCityKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UpdateParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handles show forecast message.
 */
@Component
public class ShowForecastHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final ChangeCityKeyboardBuilder changeCityKeyboardBuilder;
  private final CancelKeyboardBuilder cancelKeyboardBuilder;

  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  public ShowForecastHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, LocalizationService localizationService,
      ChangeCityKeyboardBuilder changeCityKeyboardBuilder,
      CancelKeyboardBuilder cancelKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.changeCityKeyboardBuilder = changeCityKeyboardBuilder;
    this.cancelKeyboardBuilder = cancelKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "show.forecast");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession sessionToUpdate = requestToDispatch.getUserSession();

    if (requestToDispatch.getUserSession().getCity() == null) {
      getMessageSendingService().sendMessage(sessionToUpdate,
          "enter.city", cancelKeyboardBuilder.build());
      sessionToUpdate.setConversationState(ConversationState.WAITING_FOR_CITY);
    } else {
      getMessageSendingService().sendMessage(sessionToUpdate,
          "enter.date",
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
