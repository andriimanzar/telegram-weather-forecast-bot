package com.manzar.telegramweatherbot.service;

import com.manzar.telegramweatherbot.model.UserSession;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Retrieves localized text from a resource bundle based on the user's language preference.
 */
@Component
@RequiredArgsConstructor
public class LocalizationService {

  private final MessageSource messageSource;

  /**
   * Localizes the given message with the given parameters for the user session's locale. If message
   * was not found in resource bundle - returns the same message.
   *
   * @param userSession the user session for which to localize the message
   * @param message     the message to be localized
   * @param params      the parameters to be used in the message
   * @return the localized message
   */
  public String localizeMessage(UserSession userSession, String message, String[] params) {
    try {
      return messageSource.getMessage(message, params, getLocaleFromSession(userSession));
    } catch (NoSuchMessageException exception) {
      return message;
    }
  }

  /**
   * Localizes the labels of the buttons in the given `KeyboardRow`, based on the user's language
   * preference.
   *
   * @param userSession     the `UserSession` object representing the user's session
   * @param keyboardButtons the `KeyboardRow` object containing the buttons to be localized
   */
  public void localizeButtonRowLabels(UserSession userSession, KeyboardRow keyboardButtons) {
    for (KeyboardButton keyboardButton : keyboardButtons) {
      String localizedButtonText = localizeMessage(userSession, keyboardButton.getText(), null);
      keyboardButton.setText(localizedButtonText);
    }
  }

  public boolean localizedButtonLabelEqualsGivenText(UserSession userSession, String text,
      String buttonLabel) {
    return text.equals(localizeMessage(userSession, buttonLabel, null));
  }

  private Locale getLocaleFromSession(UserSession userSession) {
    return Optional.ofNullable(userSession.getLanguage()).map(Enum::name)
        .map(Locale::forLanguageTag).orElse(Locale.ENGLISH);
  }
}
