package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.LanguageKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UpdateParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handles user request to change language.
 */
@Component
public class LanguageHandler extends AbstractUserRequestHandler implements UserRequestHandler {

  private final LanguageKeyboardBuilder languageKeyboardBuilder;

  public LanguageHandler(
      MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      LanguageKeyboardBuilder languageKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.languageKeyboardBuilder = languageKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "edit.language");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    userSession.setConversationState(ConversationState.WAITING_FOR_LANGUAGE);
    getUserSessionService().editUserSession(userSession);

    getMessageSendingService().sendMessage(requestToDispatch.getUserSession(), "choose.language",
        languageKeyboardBuilder.build());
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
