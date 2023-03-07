package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UpdateParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Handles start command.
 */
@RequiredArgsConstructor
@Component
public class StartCommandHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private static final String COMMAND = "/start";

  private final MessageSendingService messageSendingService;
  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;
  private final UserSessionService userSessionService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isCommand(request.getUpdate(), COMMAND);
  }

  @Override
  public void handle(UserRequest dispatchRequest) {
    userSessionService.createUserSessionIfNotExists(
        UpdateParser.getTelegramId(dispatchRequest.getUpdate()));

    ReplyKeyboard replyKeyboard = startMenuKeyboardBuilder.build();
    messageSendingService.sendMessage(dispatchRequest.getChatId(),
        "👋 Hello! 🌦️I can help you with the weather forecast!🌡️",
        replyKeyboard);
    messageSendingService.sendMessage(dispatchRequest.getChatId(),
        "🏠 Main Menu 🌐" + System.lineSeparator()
            + "What would you like to do next? Choose an option below: ⬇️");
  }

  @Override
  public boolean isGlobal() {
    return true;
  }
}
