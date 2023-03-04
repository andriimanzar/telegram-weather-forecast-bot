package com.manzar.telegramweatherbot.constant;

/**
 * Stores constants.
 */
public enum ButtonLabel {

  CANCEL_BUTTON("❌Cancel"), SHOW_FORECAST("☀️Show forecast"), SETTINGS(
      "⚙️Settings"), SET_NOTIFICATIONS("🚨Set notifications"), CHANGE_CITY("🏙️Change city");

  private final String value;

  ButtonLabel(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
