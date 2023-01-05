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
}
