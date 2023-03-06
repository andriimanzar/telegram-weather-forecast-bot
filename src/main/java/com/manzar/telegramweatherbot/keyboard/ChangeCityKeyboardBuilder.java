package com.manzar.telegramweatherbot.keyboard;

import static com.manzar.telegramweatherbot.constant.ButtonLabel.CANCEL_BUTTON;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.CHANGE_CITY;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
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
  public ReplyKeyboard build() {

    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton(CHANGE_CITY.getValue())));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton(CANCEL_BUTTON.getValue())));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}
