package com.manzar.telegramweatherbot.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class CityNameValidatorTest {

  private static CityNameValidator cityNameValidator;

  @BeforeAll
  static void beforeAll() {
    RestTemplate restTemplate = buildRestTemplateWithoutErrorHandling();
    cityNameValidator = new CityNameValidator(restTemplate);
    ReflectionTestUtils.setField(cityNameValidator, "weatherMapApiKey",
        System.getenv().get("API_OPEN_WEATHER_MAP_KEY"));
  }

  @Test
  void enteredCityExistsReturnFalseIfCityNameIsIncorrect() {
    assertFalse(cityNameValidator.enteredCityExists("SOME_CITY"));
  }

  @Test
  void enteredCityExistsReturnTrueIfCityNameIsCorrect() {
    assertTrue(cityNameValidator.enteredCityExists("London"));
  }

  private static RestTemplate buildRestTemplateWithoutErrorHandling() {
    return new RestTemplateBuilder()
        .errorHandler(new DefaultResponseErrorHandler() {
          @Override
          public void handleError(@NonNull ClientHttpResponse response) {
          }
        })
        .build();
  }
}