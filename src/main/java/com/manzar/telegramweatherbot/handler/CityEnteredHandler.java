package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.util.CityNameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handles user message, that contains city name.
 */
@Component
@RequiredArgsConstructor
public class CityEnteredHandler extends AbstractUserRequestHandler {

  private final CityNameValidator cityNameValidator;
  private final MessageSendingService messageSendingService;

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate());
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String cityName = requestToDispatch.getUpdate().getMessage().getText();
    if (!cityNameValidator.enteredCityExists(cityName)) {
      messageSendingService.sendMessage(requestToDispatch.getChatId(),
          "Entered city not found. Please, try again");
    } else {
      messageSendingService.sendMessage(requestToDispatch.getChatId(),
          "Now, choose how many days you want to see the weather forecast");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}