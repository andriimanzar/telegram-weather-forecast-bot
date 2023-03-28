package com.manzar.telegramweatherbot.keyboard;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds keyboard with supported unit systems.
 */
@Component
public class UnitSystemKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboardMarkup build() {
    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton("metric.unit.system"),
            new KeyboardButton("imperial.unit.system")));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton("cancel.button")));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}
