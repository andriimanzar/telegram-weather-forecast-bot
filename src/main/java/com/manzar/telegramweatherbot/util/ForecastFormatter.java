package com.manzar.telegramweatherbot.util;

import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Provides methods for formatting weather forecast data.
 */
@Component
public class ForecastFormatter {

  public String format(List<WeatherForecast> requestedDayForecasts) {
    // TODO: must take weather forecast to format and return formatted String.
    return null;
  }

}
