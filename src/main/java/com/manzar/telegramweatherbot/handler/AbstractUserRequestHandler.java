package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Abstract handler class, that contains some general methods for user request handlers.
 */
@RequiredArgsConstructor
@Getter
public abstract class AbstractUserRequestHandler implements UserRequestHandler {

  private final MessageSendingService messageSendingService;
  private final UserSessionService userSessionService;
  private final LocalizationService localizationService;

  public boolean isCommand(Update update, String command) {
    return update.hasMessage() && update.getMessage().isCommand()
        && update.getMessage().getText().equals(command);
  }

  public boolean isText(Update update) {
    return update.hasMessage() && update.getMessage().hasText();
  }
}

