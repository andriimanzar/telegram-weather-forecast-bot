package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UpdateParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handles user request to change settings.
 */
@Component
public class SettingsHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final SettingsKeyboardBuilder settingsKeyboardBuilder;

  public SettingsHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      SettingsKeyboardBuilder settingsKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.settingsKeyboardBuilder = settingsKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "settings.button");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    getUserSessionService().createUserSessionIfNotExists(
        UpdateParser.getTelegramId(requestToDispatch.getUpdate()));

    getMessageSendingService().sendMessage(requestToDispatch.getUserSession(),
        "settings", settingsKeyboardBuilder.build());
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
