package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.ButtonLabel;
import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles cancel request from user.
 */
@Component
@RequiredArgsConstructor
public class CancelHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final MessageSendingService messageSendingService;
  private final UserSessionService userSessionService;
  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUpdate().getMessage().getText().equals(
        ButtonLabel.CANCEL_BUTTON.getValue());
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    messageSendingService.sendMessage(requestToDispatch.getChatId(),
        "🔙 You have been sent back to the main menu! 🏠",
        startMenuKeyboardBuilder.build());

    UserSession userSession = requestToDispatch.getUserSession();
    userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
    userSessionService.editUserSession(userSession);
  }

  @Override
  public boolean isGlobal() {
    return true;
  }
}
