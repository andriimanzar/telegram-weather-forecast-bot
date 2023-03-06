package com.manzar.telegramweatherbot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

/**
 * Hides the keyboard when it is no longer needed.
 */
@Component
public class RemoveKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboard build() {
    return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
  }
}
