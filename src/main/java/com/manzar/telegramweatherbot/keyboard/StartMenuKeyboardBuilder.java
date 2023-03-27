package com.manzar.telegramweatherbot.keyboard;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds start menu.
 */
@Component
public class StartMenuKeyboardBuilder implements KeyboardBuilder {

  /**
   * Builds start menu.
   */
  public ReplyKeyboardMarkup build() {

    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton("show.forecast"), new KeyboardButton(
           "set.notifications")));
    KeyboardRow secondRow = new KeyboardRow(List.of(new KeyboardButton("settings.button")));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow))
        .selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(false).build();
  }
}
