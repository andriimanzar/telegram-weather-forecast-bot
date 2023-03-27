package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.NotificationTypeKeyboardBuilder;
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
 * Handles set notification user request.
 */
@Component
public class SetNotificationsHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  private final NotificationTypeKeyboardBuilder notificationTypeKeyboardBuilder;

  public SetNotificationsHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      NotificationTypeKeyboardBuilder notificationTypeKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.notificationTypeKeyboardBuilder = notificationTypeKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    Update update = request.getUpdate();
    String text = UpdateParser.getText(update);
    return isText(update) && getLocalizationService().localizedButtonLabelEqualsGivenText(
        request.getUserSession(), text, "set.notifications");
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    UserSession userSession = requestToDispatch.getUserSession();
    if (userSession.getCity() == null) {
      getMessageSendingService().sendMessage(userSession,
          "specify.city.first");
    } else {
      userSession.setConversationState(ConversationState.WAITING_FOR_NOTIFICATION_TYPE);
      getUserSessionService().editUserSession(userSession);
      getMessageSendingService().sendMessage(userSession,
          "notification.type",
          notificationTypeKeyboardBuilder.build());
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
