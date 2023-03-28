package com.manzar.telegramweatherbot.keyboard;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds keyboard with supported languages.
 */
@Component
public class LanguageKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboardMarkup build() {
    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton("ukrainian.language"), new KeyboardButton("english.language")));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton("cancel.button")));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}
