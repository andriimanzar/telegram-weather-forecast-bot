package com.manzar.telegramweatherbot.bot;

import com.manzar.telegramweatherbot.exception.BotRegistrationException;
import com.manzar.telegramweatherbot.exception.UnexpectedUpdateException;
import com.manzar.telegramweatherbot.handler.DispatcherHandler;
import com.manzar.telegramweatherbot.model.UserRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Class, that represents bot. Contains methods to configure register and configure bot.
 */
@Component
@RequiredArgsConstructor
public class WeatherBot extends TelegramLongPollingBot {

  @Value("${telegram-bot.token}")
  private String botToken;
  @Value("${telegram-bot.username}")
  private String botUsername;
  private final DispatcherHandler dispatcherHandler;

  /**
   * Method, that will be called after creation bean for this class.
   *
   * @throws BotRegistrationException in case bot registration was failed.
   */
  @PostConstruct
  public void init() {
    try {
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
      telegramBotsApi.registerBot(this);
    } catch (TelegramApiException e) {
      throw new BotRegistrationException("Cannot register bot", e);
    }
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @SneakyThrows
  @Override
  public void onUpdateReceived(Update update) {

    UserRequest userRequest = new UserRequest(update, update.getMessage().getChatId());

    boolean dispatched = dispatcherHandler.dispatch(userRequest);

    if (!dispatched) {
      throw new UnexpectedUpdateException("Unexpected update!");
    }
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }
}
