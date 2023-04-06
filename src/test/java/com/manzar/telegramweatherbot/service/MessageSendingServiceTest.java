package com.manzar.telegramweatherbot.service;

import static com.manzar.telegramweatherbot.service.factory.UserSessionFactory.createTestUserSession;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.sender.WeatherBotSender;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@ExtendWith(MockitoExtension.class)
class MessageSendingServiceTest {

  @Mock
  private WeatherBotSender weatherBotSender;
  @Mock
  private LocalizationService localizationService;
  @InjectMocks
  private MessageSendingService messageSendingService;

  @Test
  void sendMessageCallsBotExecuteMethod() throws TelegramApiException {
    UserSession userSession = createTestUserSession();

    when(localizationService.localizeMessage(userSession, "test",
        null)).thenReturn("test");

    messageSendingService.sendMessage(userSession, "test",
        new ReplyKeyboardMarkup(new ArrayList<>()));

    verify(weatherBotSender,
        times(1)).execute(any(SendMessage.class));
  }
}