package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.MessageSendingService;
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

  @Override
  public boolean isApplicable(UserRequest request) {
    return isCommand(request.getUpdate(), COMMAND);
  }

  @Override
  public void handle(UserRequest dispatchRequest) {
    ReplyKeyboard replyKeyboard = startMenuKeyboardBuilder.build();
    messageSendingService.sendMessage(dispatchRequest.getChatId(),
        "I can help you with weather forecast☁️", replyKeyboard);
    messageSendingService.sendMessage(dispatchRequest.getChatId(),
        "Choose from menu what are you interested in");
  }

  @Override
  public boolean isGlobal() {
    return true;
  }
}
