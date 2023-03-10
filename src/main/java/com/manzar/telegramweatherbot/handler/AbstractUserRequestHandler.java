package com.manzar.telegramweatherbot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Abstract handler class, that contains some general methods for user request handlers.
 */
public abstract class AbstractUserRequestHandler implements UserRequestHandler {

  public boolean isCommand(Update update, String command) {
    return update.hasMessage() && update.getMessage().isCommand()
        && update.getMessage().getText().equals(command);
  }

  public boolean isText(Update update) {
    return update.hasMessage() && update.getMessage().hasText();
  }

  public boolean isTextAndEquals(Update update, String text) {
    return isText(update) && update.getMessage().getText().equals(text);
  }
}
