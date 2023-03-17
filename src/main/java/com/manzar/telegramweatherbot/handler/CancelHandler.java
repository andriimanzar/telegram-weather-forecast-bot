package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.ButtonLabel;
import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import org.springframework.stereotype.Component;

/**
 * Handles cancel request from user.
 */
@Component
public class CancelHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;

  public CancelHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, StartMenuKeyboardBuilder startMenuKeyboardBuilder) {
    super(messageSendingService, userSessionService);
    this.startMenuKeyboardBuilder = startMenuKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUpdate().getMessage().getText().equals(
        ButtonLabel.CANCEL_BUTTON.getValue());
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
        "🔙 You have been sent back to the main menu! 🏠",
        startMenuKeyboardBuilder.build());

    UserSession userSession = requestToDispatch.getUserSession();
    userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
    getUserSessionService().editUserSession(userSession);
  }

  @Override
  public boolean isGlobal() {
    return true;
  }
}
