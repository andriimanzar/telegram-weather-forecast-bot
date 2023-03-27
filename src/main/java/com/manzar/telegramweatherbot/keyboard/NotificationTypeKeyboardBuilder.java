package com.manzar.telegramweatherbot.keyboard;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds menu to choose notification type.
 */
@Component
public class NotificationTypeKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboardMarkup build() {

    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton("tomorrow.type"), new KeyboardButton(
            "morning.and.afternoon.type")));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton("unfollow.notifications")));
    KeyboardRow thirdRow = new KeyboardRow(
        List.of(new KeyboardButton("cancel.button")));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow, thirdRow))
        .selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}

