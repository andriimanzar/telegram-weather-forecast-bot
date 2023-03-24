package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.constant.ButtonLabel;
import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import org.springframework.stereotype.Component;

/**
 * Handles user request to change settings.
 */
@Component
public class SettingsHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final SettingsKeyboardBuilder settingsKeyboardBuilder;

  public SettingsHandler(
      MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      SettingsKeyboardBuilder settingsKeyboardBuilder) {
    super(messageSendingService, userSessionService);
    this.settingsKeyboardBuilder = settingsKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isTextAndEquals(request.getUpdate(),
        String.valueOf(ButtonLabel.SETTINGS.getValue()));
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    getMessageSendingService().sendMessage(requestToDispatch.getChatId(),
        "🔧 Which setting would you like to edit?", settingsKeyboardBuilder.build());
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
