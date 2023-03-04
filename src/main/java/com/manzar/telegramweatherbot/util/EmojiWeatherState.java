package com.manzar.telegramweatherbot.util;


/**
 * An enumeration representing weather states along with corresponding emojis.
 */
public enum EmojiWeatherState {
  THUNDERSTORM_LIGHT_RAIN("⛈️🌧️"),

  THUNDERSTORM_RAIN("⛈️🌧️"),

  THUNDERSTORM_HEAVY_RAIN("⛈️🌧️💧"),

  THUNDERSTORM_LIGHT("⛈️"),

  THUNDERSTORM("⛈️"),

  THUNDERSTORM_HEAVY("⛈️💧"),

  THUNDERSTORM_RAGGED("⛈️🌀"),

  THUNDERSTORM_LIGHT_DRIZZLE("⛈️🌧️"),

  THUNDERSTORM_DRIZZLE("⛈️🌧️"),

  THUNDERSTORM_HEAVY_DRIZZLE("⛈️🌧️💧"),

  DRIZZLE_LIGHT("🌧️"),

  DRIZZLE("🌧️"),

  DRIZZLE_HEAVY("🌧️💧"),

  DRIZZLE_LIGHT_RAIN("🌧️🌧️"),

  DRIZZLE_RAIN("🌧️🌧️"),

  DRIZZLE_HEAVY_RAIN("🌧️🌧️💧"),

  DRIZZLE_SHOWER_RAIN("🌧️🌦️"),

  DRIZZLE_HEAVY_SHOWER_RAIN("🌧️🌦️💧"),

  DRIZZLE_SHOWER("🌧️🌦️"),

  RAIN_LIGHT("🌧️"),

  RAIN_MODERATE("🌧️🌧️"),

  RAIN_HEAVY("🌧️🌧️💧"),

  RAIN_VERY_HEAVY("🌧️🌧️💧💧"),

  RAIN_EXTREME("🌧️🌧️💧💧"),

  RAIN_FREEZING("🌧️❄️"),

  RAIN_LIGHT_SHOWER("🌧️🌦️"),

  RAIN_SHOWER("🌧️🌦️"),

  RAIN_HEAVY_SHOWER("🌧️🌦️💧"),

  RAIN_RAGGED_SHOWER("🌧️🌦️🌀"),

  SNOW_LIGHT("❄️"),

  SNOW("❄️❄️"),

  SNOW_HEAVY("❄️❄️❄️"),

  SNOW_SLEET("❄️🌨️"),

  SNOW_LIGHT_SHOWER_SLEET("❄️🌨️🌦️"),

  SNOW_SHOWER_SLEET("❄️🌨️🌦️"),

  SNOW_LIGHT_RAIN_AND_SNOW("❄️🌧️"),

  SNOW_RAIN_AND_SNOW("❄️🌧️"),

  SNOW_LIGHT_SHOWER_SNOW("❄️🌨️🌦️"),

  SNOW_SHOWER_SNOW("❄️🌨️🌦️"),

  SNOW_HEAVY_SHOWER_SNOW("❄️🌨️🌦️💧"),

  MIST("🌫️"),

  SMOKE("🌫️🔥"),

  HAZE("🌫️"),

  DUST_WHIRLS("💨🌪️"),

  FOG("🌫️"),

  SAND("🏜️"),

  DUST("🌪️💨"),

  ASH("🌋🌫️"),

  SQUALL("💨🌬️"),

  TORNADO("🌪️"),

  CLEAR("☀️"),

  CLOUDS_FEW("🌤️"),

  CLOUDS_SCATTERED("⛅"),

  CLOUDS_BROKEN("🌥️"),

  CLOUDS_OVERCAST("☁️");

  private final String emoji;

  EmojiWeatherState(String emoji) {
    this.emoji = emoji;
  }

  public String getEmoji() {
    return emoji;
  }
}
