package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.UnitSystemKeyboardBuilder;
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
 * Handles user request to change unit system.
 */
@Component
public class UnitSystemHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final UnitSystemKeyboardBuilder unitSystemKeyboardBuilder;

  public UnitSystemHandler(
      MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      UnitSystemKeyboardBuilder unitSystemKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.unitSystemKeyboardBuilder = unitSystemKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "edit.unit.system");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    userSession.setConversationState(ConversationState.WAITING_FOR_UNIT_SYSTEM);
    getUserSessionService().editUserSession(userSession);

    getMessageSendingService().sendMessage(requestToDispatch.getUserSession(), "choose.unit.system",
        unitSystemKeyboardBuilder.build());
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}


