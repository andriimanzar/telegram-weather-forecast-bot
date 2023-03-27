package com.manzar.telegramweatherbot.handler;

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
 * Handles user request to change city.
 */
@Component
public class ChangeCityHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  public ChangeCityHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, LocalizationService localizationService) {
    super(messageSendingService, userSessionService, localizationService);
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "change.city");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession sessionToUpdate = requestToDispatch.getUserSession();
    sessionToUpdate.setConversationState(ConversationState.WAITING_FOR_CITY);
    getUserSessionService().editUserSession(sessionToUpdate);

    getMessageSendingService().sendMessage(sessionToUpdate,
        "enter.city");
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
