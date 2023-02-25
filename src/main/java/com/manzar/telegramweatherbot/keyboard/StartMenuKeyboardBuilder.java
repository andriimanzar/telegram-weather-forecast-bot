package com.manzar.telegramweatherbot.keyboard;

import static com.manzar.telegramweatherbot.constant.Constants.CANCEL_BUTTON;
import static com.manzar.telegramweatherbot.constant.Constants.SETTINGS;
import static com.manzar.telegramweatherbot.constant.Constants.SET_NOTIFICATIONS;
import static com.manzar.telegramweatherbot.constant.Constants.SHOW_FORECAST;

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
    List<KeyboardButton> buttons = List.of(new KeyboardButton(SHOW_FORECAST),
        new KeyboardButton(SETTINGS),
        new KeyboardButton(SET_NOTIFICATIONS));

    KeyboardRow firstRow = new KeyboardRow(buttons);
    KeyboardRow secondRow = new KeyboardRow(List.of(new KeyboardButton(CANCEL_BUTTON)));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(false).build();
  }
}
