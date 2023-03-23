package com.manzar.telegramweatherbot.keyboard;

import static com.manzar.telegramweatherbot.constant.ButtonLabel.CANCEL_BUTTON;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builds keyboard with hours to choose notification time.
 */
@Component
public class NotificationTimeKeyboardBuilder implements KeyboardBuilder {

  @Override
  public ReplyKeyboard build() {
    return ReplyKeyboardMarkup.builder().keyboard(createAllRows()).selective(true)
        .resizeKeyboard(true).oneTimeKeyboard(true).build();
  }

  private List<KeyboardRow> createAllRows() {
    List<KeyboardRow> allRows = Stream.iterate(0, n -> n + 4).limit(6)
        .map(n -> createRowWithGivenHoursInterval(n, n + 4))
        .collect(Collectors.toList());

    KeyboardRow rowWithCancel = new KeyboardRow(
        List.of(new KeyboardButton(CANCEL_BUTTON.getValue())));
    allRows.add(rowWithCancel);

    return allRows;
  }

  private KeyboardRow createRowWithGivenHoursInterval(int lowerBound, int higherBound) {
    List<KeyboardButton> rowButtons = Stream.iterate(lowerBound, n -> n < higherBound, n -> n + 1)
        .map(n -> new KeyboardButton(String.format("%02d:00", n))).collect(Collectors.toList());

    return new KeyboardRow(rowButtons);
  }
}
