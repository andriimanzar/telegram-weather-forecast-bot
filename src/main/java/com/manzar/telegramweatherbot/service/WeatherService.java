package com.manzar.telegramweatherbot.service;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.model.forecast.Forecast;
import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast;
import com.manzar.telegramweatherbot.util.ForecastFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service, that deals with weather forecast.
 */
@Service
@RequiredArgsConstructor
public class WeatherService {

  private final OpenWeatherMapClient openWeatherMapClient;
  private final ForecastFormatter forecastFormatter;

  /**
   * Retrieves the weather forecast for a given city name and date.
   *
   * @param cityName     the name of the city to retrieve the forecast for
   * @param requestedDay the date to retrieve the forecast for
   * @return a String formatted forecast
   */
  public String getWeatherForecastByCityNameAndDate(String cityName, LocalDate requestedDay) {
    Forecast forecastForFiveDays = openWeatherMapClient.forecast5Day3HourStep().byCityName(cityName)
        .retrieve().asJava();
    List<WeatherForecast> requestedDayForecasts = forecastForFiveDays.getWeatherForecasts()
        .stream()
        .filter(threeHourPeriodForecast -> threeHourPeriodForecast.getForecastTime().toLocalDate()
            .equals(requestedDay)).collect(Collectors.toList());

    return forecastFormatter.format(requestedDayForecasts);
  }

}
