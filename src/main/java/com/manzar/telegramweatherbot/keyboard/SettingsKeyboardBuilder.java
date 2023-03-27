package com.manzar.telegramweatherbot.keyboard;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds keyboard for user to change settings.
 */
@Component
public class SettingsKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboardMarkup build() {
    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton("edit.language"),
            new KeyboardButton("edit.unit.system")));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton("cancel.button")));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}
