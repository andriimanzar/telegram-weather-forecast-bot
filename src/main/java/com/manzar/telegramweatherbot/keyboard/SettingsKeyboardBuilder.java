package com.manzar.telegramweatherbot.keyboard;

import static com.manzar.telegramweatherbot.constant.ButtonLabel.CANCEL_BUTTON;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.EDIT_LANGUAGE;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.EDIT_METRIC_SYSTEM;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds keyboard for user to change settings.
 */
@Component
public class SettingsKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboard build() {
    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton(EDIT_LANGUAGE.getValue()),
            new KeyboardButton(EDIT_METRIC_SYSTEM.getValue())));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton(CANCEL_BUTTON.getValue())));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}
