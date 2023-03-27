package com.manzar.telegramweatherbot.keyboard;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds menu to change city.
 */
@Component
public class ChangeCityKeyboardBuilder implements KeyboardBuilder {

  /**
   * Builds menu to change city.
   */

  @Override
  public ReplyKeyboardMarkup build() {

    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton("change.city")));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton("cancel.button")));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}
