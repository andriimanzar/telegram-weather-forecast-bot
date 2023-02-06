package com.manzar.telegramweatherbot.service;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service, that deals with weather forecast.
 */
@Service
@RequiredArgsConstructor
public class WeatherService {

  private final OpenWeatherMapClient openWeatherMapClient;

  /**
   * Finds weather forecast for 5 days for given city.
   *
   * @param cityName for which to find the forecast.
   * @return forecast for 5 days.
   */
  public List<WeatherForecast> getWeatherForecastByCityName(String cityName) {
    return openWeatherMapClient.forecast5Day3HourStep().byCityName(cityName).retrieve().asJava()
        .getWeatherForecasts();
  }

}
