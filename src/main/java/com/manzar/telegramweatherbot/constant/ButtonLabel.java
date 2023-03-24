package com.manzar.telegramweatherbot.constant;

/**
 * Stores button labels.
 */
public enum ButtonLabel {

  CANCEL_BUTTON("❌Cancel"), SHOW_FORECAST("☀️Show forecast"), SETTINGS(
      "⚙️Settings"), SET_NOTIFICATIONS("🚨Set notifications"), CHANGE_CITY(
      "🏙️Change city"), FOR_TOMORROW("☑️Tomorrow"), FOR_MORNING_AND_AFTERNOON(
      "☑️Morning and afternoon"), UNFOLLOW_NOTIFICATIONS("🔕Unfollow Notifications"), EDIT_LANGUAGE(
      "🗺️Language"), EDIT_METRIC_SYSTEM("🌡️Metric System");

  private final String value;

  ButtonLabel(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
