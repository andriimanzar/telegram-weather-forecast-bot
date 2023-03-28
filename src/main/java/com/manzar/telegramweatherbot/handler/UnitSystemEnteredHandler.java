package com.manzar.telegramweatherbot.handler;

import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.manzar.telegramweatherbot.keyboard.SettingsKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UnitSystemParser;
import com.manzar.telegramweatherbot.util.UpdateParser;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Handles user request with chosen unit system.
 */
@Component
public class UnitSystemEnteredHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  private final SettingsKeyboardBuilder settingsKeyboardBuilder;

  public UnitSystemEnteredHandler(
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
        .equals(ConversationState.WAITING_FOR_UNIT_SYSTEM);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    String text = UpdateParser.getText(requestToDispatch.getUpdate());
    Optional<UnitSystem> unitSystem = UnitSystemParser.parse(getLocalizationService(), userSession,
        text);

    if (unitSystem.isEmpty()) {
      getMessageSendingService().sendMessage(userSession, "chosen.unit.system.not.supported");

    } else if (userSession.getUnitSystem().equals(unitSystem.get())) {
      getMessageSendingService().sendMessage(userSession,
          "chosen.unit.system.equals.current");

    } else {
      userSession.setUnitSystem(unitSystem.get());
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getUserSessionService().editUserSession(userSession);

      getMessageSendingService().sendMessage(userSession, "unit.system.updated",
          settingsKeyboardBuilder.build());

    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
