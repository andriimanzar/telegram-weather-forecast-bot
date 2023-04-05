package com.manzar.telegramweatherbot.util;

import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast;
import com.github.prominence.openweathermap.api.model.forecast.Wind;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Provides methods for formatting weather forecast data.
 */
@Component
@RequiredArgsConstructor
public class ForecastFormatter {

  public static final String LINE_SEPARATOR = System.lineSeparator();
  private final LocalizationService localizationService;

  /**
   * Formats the weather forecast for a given city and day.
   *
   * @param userSession           current user session
   * @param requestedDayForecasts the list of weather forecasts for the requested day.
   * @return a formatted string containing the weather forecast for the specified city and day or
   *     informs user that there is no forecast for requested day.
   */
  public String format(UserSession userSession, List<WeatherForecast> requestedDayForecasts) {
    if (requestedDayForecasts.isEmpty()) {
      return "predictions.ended";
    }
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(LINE_SEPARATOR);

    for (WeatherForecast weatherForecast : requestedDayForecasts) {
      stringBuilder.append(weatherForecast.getForecastTime().toLocalTime()).append(" ");
      appendWeatherStateEmoji(weatherForecast, stringBuilder);
      appendTemperature(weatherForecast, stringBuilder);
      appendClouds(weatherForecast, stringBuilder);
      appendWind(userSession, weatherForecast, stringBuilder);
      stringBuilder.append(LINE_SEPARATOR);
    }
    LocalDate requestedDay = requestedDayForecasts.get(0).getForecastTime().toLocalDate();
    String[] params = new String[]{userSession.getCity(), requestedDay.toString(),
        stringBuilder.toString()};
    return localizationService.localizeMessage(userSession, "weather.forecast",
        params);
  }

  private void appendWeatherStateEmoji(WeatherForecast weatherForecast,
      StringBuilder stringBuilder) {

    String emoji = EmojiWeatherState.valueOf(
        weatherForecast.getWeatherState().getWeatherConditionEnum().name()).getEmoji();
    stringBuilder.append(emoji).append(" ");
  }

  private void appendTemperature(WeatherForecast weatherForecast, StringBuilder stringBuilder) {
    int temperature = (int) Math.round(weatherForecast.getTemperature().getValue());
    if (temperature > 0) {
      stringBuilder.append("+");
    }
    stringBuilder.append(temperature).append(weatherForecast.getTemperature().getUnit())
        .append(", ");
  }

  private void appendClouds(WeatherForecast weatherForecast, StringBuilder stringBuilder) {
    stringBuilder.append("☁️ - ").append(weatherForecast.getClouds().getValue())
        .append(weatherForecast.getClouds().getUnit()).append(", ");
  }

  private void appendWind(UserSession userSession, WeatherForecast weatherForecast,
      StringBuilder stringBuilder) {
    Wind wind = weatherForecast.getWind();
    stringBuilder.append("💨").append(String.format("%.1f", wind.getSpeed())).append(" ")
        .append(localizationService.localizeMessage(userSession, wind.getUnit(), null));
  }
}

