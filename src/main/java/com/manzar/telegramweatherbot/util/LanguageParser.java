package com.manzar.telegramweatherbot.util;

import com.manzar.telegramweatherbot.model.Language;
import java.util.Optional;

/**
 * Responsible for parsing language data and converting it into a Language object.
 */
public class LanguageParser {

  /**
   * Returns the Language object associated with the specified emojiValue, if it exists.
   *
   * @param emojiValue the emoji value string to be matched to a Language object
   * @return an Optional containing the matching Language object, or an empty Optional if no match
   *     is found
   */
  public static Optional<Language> fromEmojiValue(String emojiValue) {
    for (Language language : Language.values()) {
      if (language.getEmojiValue().equals(emojiValue)) {
        return Optional.of(language);
      }
    }
    return Optional.empty();
  }
}
