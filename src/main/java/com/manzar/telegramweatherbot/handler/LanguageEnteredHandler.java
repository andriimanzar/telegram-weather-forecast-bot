package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.Language;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.LanguageParser;
import com.manzar.telegramweatherbot.util.UpdateParser;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Handles user request with chosen language.
 */
@Component
public class LanguageEnteredHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  private final SettingsKeyboardBuilder settingsKeyboardBuilder;

  public LanguageEnteredHandler(
      MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      SettingsKeyboardBuilder settingsKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.settingsKeyboardBuilder = settingsKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_LANGUAGE);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    String chosenLanguageEmoji = UpdateParser.getText(requestToDispatch.getUpdate());
    String currentUserLanguage = userSession.getLanguage().getEmojiValue();
    Optional<Language> languageFromEmoji = LanguageParser.fromEmojiValue(chosenLanguageEmoji);

    if (chosenLanguageEmoji.equals(currentUserLanguage)) {
      getMessageSendingService().sendMessage(userSession, "chosen.language.equals.current");

    } else if (languageFromEmoji.isEmpty()) {
      getMessageSendingService().sendMessage(userSession, "chosen.language.not.supported");

    } else {
      Language chosenLanguage = languageFromEmoji.get();
      userSession.setLanguage(chosenLanguage);
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getUserSessionService().editUserSession(userSession);

      getMessageSendingService().sendMessage(userSession, "language.updated",
          settingsKeyboardBuilder.build());
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
