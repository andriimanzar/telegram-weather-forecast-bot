package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UpdateParser;
import org.springframework.stereotype.Component;

/**
 * Handles start command.
 */
@Component
public class StartCommandHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private static final String COMMAND = "/start";

  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;

  public StartCommandHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      StartMenuKeyboardBuilder startMenuKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.startMenuKeyboardBuilder = startMenuKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isCommand(request.getUpdate(), COMMAND);
  }

  @Override
  public void handle(UserRequest dispatchRequest) {
    getUserSessionService().createUserSessionIfNotExists(
        UpdateParser.getTelegramId(dispatchRequest.getUpdate()));

    getMessageSendingService().sendMessage(dispatchRequest.getUserSession(),
        "greeting",
        startMenuKeyboardBuilder.build());
    getMessageSendingService().sendMessage(dispatchRequest.getUserSession(),
        "main.menu");
  }

  @Override
  public boolean isGlobal() {
    return true;
  }
}
