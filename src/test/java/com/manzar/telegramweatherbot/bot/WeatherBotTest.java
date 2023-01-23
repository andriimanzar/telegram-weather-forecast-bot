package com.manzar.telegramweatherbot.bot;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manzar.telegramweatherbot.exception.BotRegistrationException;
import com.manzar.telegramweatherbot.handler.DispatcherHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherBotTest {

  @Mock
  private DispatcherHandler dispatcherHandler;
  @InjectMocks
  private static WeatherBot weatherBot;

  @Test
  void initThrowsAnExceptionIfBotRegistrationFailed() {
    assertThrows(BotRegistrationException.class, () -> weatherBot.init());
  }
}