package com.manzar.telegramweatherbot.service;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.manzar.telegramweatherbot.util.ForecastFormatter;
import java.time.LocalDate;
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
    // TODO: This method must return already formatted weather forecast
    //  for given city and date as String object.
    return null;
  }

}
