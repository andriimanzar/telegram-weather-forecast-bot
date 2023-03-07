package com.manzar.telegramweatherbot.util;

import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Provides methods for formatting weather forecast data.
 */
@Component
public class ForecastFormatter {

  public static final String LINE_SEPARATOR = System.lineSeparator();

  /**
   * Formats the weather forecast for a given city and day.
   *
   * @param requestedDayForecasts the list of weather forecasts for the requested day.
   * @param cityName              the name of the city for which the weather forecast is being
   *                              formatted.
   * @return a formatted string containing the weather forecast for the specified city and day or
   * informs user that there is no forecast for requested day.
   */
  public String format(List<WeatherForecast> requestedDayForecasts, String cityName) {
    if (requestedDayForecasts.isEmpty()) {
      return "🙏 Thank you for using our service! "
          + "Our predictions for this period have ended or we were unable to request a forecast "
          + "because we can only parse weather for the next 5 days. Please try again later.";
    }

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("Weather in ").append(cityName).append(", ");
    stringBuilder.append(requestedDayForecasts.get(0).getForecastTime().toLocalDate());
    stringBuilder.append(LINE_SEPARATOR);

    for (WeatherForecast weatherForecast : requestedDayForecasts) {
      stringBuilder.append(weatherForecast.getForecastTime().toLocalTime()).append(" ");
      appendWeatherStateEmoji(weatherForecast, stringBuilder);
      appendTemperature(weatherForecast, stringBuilder);
      appendClouds(weatherForecast, stringBuilder);
      appendWind(weatherForecast, stringBuilder);
      stringBuilder.append(LINE_SEPARATOR);
    }
    return stringBuilder.toString();
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
    stringBuilder.append("clouds - ").append(weatherForecast.getClouds().getValue())
        .append(weatherForecast.getClouds().getUnit()).append(", ");
  }

  private void appendWind(WeatherForecast weatherForecast, StringBuilder stringBuilder) {
    stringBuilder.append("💨").append(weatherForecast.getWind().getSpeed()).append(" ")
        .append(weatherForecast.getWind().getUnit());
  }
}

