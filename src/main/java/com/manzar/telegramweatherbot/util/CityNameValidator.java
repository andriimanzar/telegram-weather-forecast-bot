package com.manzar.telegramweatherbot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Validates that entered by user city exists.
 */
@RequiredArgsConstructor
@Component
public class CityNameValidator {

  private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";

  @Value("${api.openweathermap.key}")
  private String weatherMapApiKey;

  private final RestTemplate restTemplate;


  public boolean enteredCityExists(String enteredCity) {
    String response = restTemplate.getForObject(prepareUrlWithParams(enteredCity), String.class);
    return response != null && !response.contains("city not found");
  }

  private String prepareUrlWithParams(String enteredCity) {
    return WEATHER_URL + "q=" + enteredCity
        + "&appid=" + weatherMapApiKey;
  }
}