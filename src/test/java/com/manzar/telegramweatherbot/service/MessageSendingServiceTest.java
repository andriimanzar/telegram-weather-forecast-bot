package com.manzar.telegramweatherbot.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.exception.MessageSendingException;
import com.manzar.telegramweatherbot.sender.WeatherBotSender;
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
  @InjectMocks
  private MessageSendingService messageSendingService;

  @Test
  void sendMessageCallsBotExecuteMethod() throws TelegramApiException {

    messageSendingService.sendMessage(1L, "test", new ReplyKeyboardMarkup());

    verify(weatherBotSender,
        times(1)).execute(any(SendMessage.class));
  }

  @Test
  void sendMessageThrowsAnExceptionIfMessageIsNotSent() throws TelegramApiException {

    doThrow(TelegramApiException.class).when(weatherBotSender).execute(any(SendMessage.class));

    assertThrows(MessageSendingException.class, () ->
        messageSendingService.sendMessage(1L, "test", new ReplyKeyboardMarkup()));
  }
}