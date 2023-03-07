package com.manzar.telegramweatherbot.keyboard;

import static com.manzar.telegramweatherbot.constant.ButtonLabel.SETTINGS;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.SET_NOTIFICATIONS;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.SHOW_FORECAST;

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
        List.of(new KeyboardButton(SHOW_FORECAST.getValue()), new KeyboardButton(
            SET_NOTIFICATIONS.getValue())));
    KeyboardRow secondRow = new KeyboardRow(List.of(new KeyboardButton(SETTINGS.getValue())));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow))
        .selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(false).build();
  }
}
