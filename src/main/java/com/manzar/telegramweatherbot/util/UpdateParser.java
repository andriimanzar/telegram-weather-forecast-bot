package com.manzar.telegramweatherbot.util;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Util class, that contains methods, which parses some values from user update.
 */
public class UpdateParser {

  public static Long getTelegramId(Update update) {
    return update.getMessage().getFrom().getId();
  }

  public static String getText(Update update) {
    return update.getMessage().getText();
  }
}
