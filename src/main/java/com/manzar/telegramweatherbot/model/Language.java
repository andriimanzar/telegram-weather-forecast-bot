package com.manzar.telegramweatherbot.model;

/**
 * An enumeration, that represents available language to choose.
 */
public enum Language {
  UA("🇺🇦"), EN("🇬🇧");

  private final String emojiValue;

  Language(String emojiValue) {
    this.emojiValue = emojiValue;
  }

  public String getEmojiValue() {
    return emojiValue;
  }
}
