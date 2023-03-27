package com.manzar.telegramweatherbot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Declares method, that should be implemented by all keyboard builders.
 */
public interface KeyboardBuilder {

  ReplyKeyboardMarkup build();

}
