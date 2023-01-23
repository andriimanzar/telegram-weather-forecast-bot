package com.manzar.telegramweatherbot.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

/**
 * Sends message to user.
 */
@Component
public class WeatherBotSender extends DefaultAbsSender {

  @Value("${telegram-bot.token}")
  private String botToken;

  public WeatherBotSender() {
    super(new DefaultBotOptions());
  }

  @Override
  public String getBotToken() {
    return botToken;
  }
}

