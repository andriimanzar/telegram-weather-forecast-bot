package com.manzar.telegramweatherbot.service;

import com.manzar.telegramweatherbot.exception.MessageSendingException;
import com.manzar.telegramweatherbot.sender.WeatherBotSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Service, that performs message sending from bot to user.
 */
@AllArgsConstructor
@Service
public class MessageSendingService {

  private WeatherBotSender weatherBotSender;

  /**
   * Sends text message to user, but without a keyboard.
   *
   * @param chatId conversation identifier
   * @param text   message to be sent
   */
  public void sendMessage(Long chatId, String text) {
    sendMessage(chatId, text, null);
  }

  /**
   * Sends text message to user and shows a keyboard to interact.
   *
   * @param chatId        conversation identifier
   * @param text          message to be sent
   * @param replyKeyboard keyboard to be shown to user
   */
  public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
    SendMessage sendMessage = SendMessage.builder().text(text).chatId(chatId)
        .replyMarkup(replyKeyboard).build();

    execute(sendMessage);
  }

  private void execute(SendMessage sendMessage) {
    try {
      weatherBotSender.execute(sendMessage);
    } catch (TelegramApiException e) {
      throw new MessageSendingException("Cannot send message");
    }
  }
}
