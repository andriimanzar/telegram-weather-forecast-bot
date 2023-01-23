package com.manzar.telegramweatherbot.keyboard;

import com.manzar.telegramweatherbot.constant.Constants;
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
    List<KeyboardButton> buttons = List.of(new KeyboardButton("☀️Show forecast"),
        new KeyboardButton("⚙️Settings"),
        new KeyboardButton("🚨Set notifications"));

    KeyboardRow firstRow = new KeyboardRow(buttons);
    KeyboardRow secondRow = new KeyboardRow(List.of(new KeyboardButton(Constants.CANCEL_BUTTON)));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow)).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(false).build();
  }
}
