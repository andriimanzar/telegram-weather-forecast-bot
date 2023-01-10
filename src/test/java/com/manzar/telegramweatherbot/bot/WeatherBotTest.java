package com.manzar.telegramweatherbot.bot;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manzar.telegramweatherbot.exception.BotRegistrationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WeatherBotTest {

  private static WeatherBot weatherBot;

  @BeforeAll
  static void beforeAll() {
    weatherBot = new WeatherBot();
  }

  @Test
  void initThrowsAnExceptionIfBotRegistrationFailed() {
    assertThrows(BotRegistrationException.class, () -> weatherBot.init());
  }
}