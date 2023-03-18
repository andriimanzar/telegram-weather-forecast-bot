package com.manzar.telegramweatherbot.keyboard;

import static com.manzar.telegramweatherbot.constant.ButtonLabel.CANCEL_BUTTON;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.FOR_MORNING_AND_AFTERNOON;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.FOR_TOMORROW;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.UNFOLLOW_NOTIFICATIONS;

import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds menu to choose notification type.
 */
@Component
public class NotificationTypeKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboard build() {

    KeyboardRow firstRow = new KeyboardRow(
        List.of(new KeyboardButton(FOR_TOMORROW.getValue()), new KeyboardButton(
            FOR_MORNING_AND_AFTERNOON.getValue())));
    KeyboardRow secondRow = new KeyboardRow(
        List.of(new KeyboardButton(UNFOLLOW_NOTIFICATIONS.getValue())));
    KeyboardRow thirdRow = new KeyboardRow(
        List.of(new KeyboardButton(CANCEL_BUTTON.getValue())));

    return ReplyKeyboardMarkup.builder().keyboard(List.of(firstRow, secondRow, thirdRow))
        .selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }
}

