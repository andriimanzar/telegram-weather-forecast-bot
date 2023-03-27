package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
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
 * Handles cancel request from user.
 */
@Component
public class CancelHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;

  public CancelHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, LocalizationService localizationService,
      StartMenuKeyboardBuilder startMenuKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.startMenuKeyboardBuilder = startMenuKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "cancel.button");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
    getUserSessionService().editUserSession(userSession);

    getMessageSendingService().sendMessage(userSession,
        "cancel",
        startMenuKeyboardBuilder.build());
  }

  @Override
  public boolean isGlobal() {
    return true;
  }
}
