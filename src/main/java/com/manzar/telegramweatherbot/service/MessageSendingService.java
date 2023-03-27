package com.manzar.telegramweatherbot.service;

import com.manzar.telegramweatherbot.exception.MessageSendingException;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.sender.WeatherBotSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Service, that performs message sending from bot to user.
 */
@RequiredArgsConstructor
@Service
public class MessageSendingService {

  private final WeatherBotSender weatherBotSender;
  private final LocalizationService localizationService;

  /**
   * Sends text message to user, but without a keyboard.
   *
   * @param userSession current user session
   * @param text        message to be sent
   */
  public void sendMessage(UserSession userSession, String text) {
    sendMessage(userSession, text, null);
  }

  /**
   * Sends text message to user and shows a keyboard to interact.
   *
   * @param userSession   current user session
   * @param text          message to be sent
   * @param replyKeyboard keyboard to be shown to user
   */
  public void sendMessage(UserSession userSession, String text, ReplyKeyboardMarkup replyKeyboard) {
    sendMessage(userSession, text, null, replyKeyboard);
  }

  /**
   * Sends text message to user and shows a keyboard to interact.
   *
   * @param userSession   current user session
   * @param text          message to be sent
   * @param params        params optional parameters to replace the placeholders in the text
   * @param replyKeyboard keyboard to be shown to user
   */
  public void sendMessage(UserSession userSession, String text, String[] params,
      ReplyKeyboardMarkup replyKeyboard) {
    String localizedText = localizationService.localizeMessage(userSession, text, params);

    if (replyKeyboard != null) {
      replyKeyboard.getKeyboard()
          .forEach(
              buttonRow -> localizationService.localizeButtonRowLabels(userSession, buttonRow));
    }

    SendMessage sendMessage = SendMessage.builder().text(localizedText)
        .chatId(userSession.getTelegramId())
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
