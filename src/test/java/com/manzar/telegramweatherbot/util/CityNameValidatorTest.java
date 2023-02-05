package com.manzar.telegramweatherbot.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CityNameValidatorTest {
  @Autowired
  private CityNameValidator cityNameValidator;

  @Test
  void enteredCityExistsReturnFalseIfCityNameIsIncorrect() {
    assertFalse(cityNameValidator.enteredCityExists("SOME_CITY"));
  }

  @Test
  void enteredCityExistsReturnTrueIfCityNameIsCorrect() {
    assertTrue(cityNameValidator.enteredCityExists("London"));
  }
}