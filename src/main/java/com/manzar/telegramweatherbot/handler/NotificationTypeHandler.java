package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.NotificationTimeKeyboardBuilder;
import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.NotificationService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.UpdateParser;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handles notification type, chosen by user.
 */
@Component
public class NotificationTypeHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  private final NotificationService notificationService;
  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;
  private final NotificationTimeKeyboardBuilder notificationTimeKeyboardBuilder;

  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  public NotificationTypeHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService,
      LocalizationService localizationService,
      NotificationService notificationService, StartMenuKeyboardBuilder startMenuKeyboardBuilder,
      NotificationTimeKeyboardBuilder notificationTimeKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.notificationService = notificationService;
    this.startMenuKeyboardBuilder = startMenuKeyboardBuilder;
    this.notificationTimeKeyboardBuilder = notificationTimeKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_NOTIFICATION_TYPE);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    Update update = requestToDispatch.getUpdate();
    String chosenOption = UpdateParser.getText(update);
    UserSession userSession = requestToDispatch.getUserSession();

    if (getLocalizationService().localizedButtonLabelEqualsGivenText(userSession, chosenOption,
        "tomorrow.type")) {
      setTomorrowNotificationWithoutTime(userSession);
    } else if (getLocalizationService().localizedButtonLabelEqualsGivenText(userSession,
        chosenOption, "morning.and.afternoon.type")) {
      setMorningAndAfternoonNotifications(userSession);

    } else if (getLocalizationService().localizedButtonLabelEqualsGivenText(userSession,
        chosenOption, "unfollow.notifications")) {
      notificationService.deleteNotifications(userSession.getTelegramId());

      getMessageSendingService().sendMessage(userSession, "notifications.unfollowed");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

  private void setTomorrowNotificationWithoutTime(UserSession userSession) {
    notificationService.createTomorrowNotification(userSession, Optional.empty());

    userSession.setConversationState(ConversationState.WAITING_FOR_NOTIFICATION_TIME);
    getUserSessionService().editUserSession(userSession);

    getMessageSendingService().sendMessage(userSession, "notification.time",
        notificationTimeKeyboardBuilder.build());
  }

  private void setMorningAndAfternoonNotifications(UserSession userSession) {
    boolean notificationCreated = notificationService.createMorningAndAfternoonNotification(
        userSession);

    if (notificationCreated) {
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getUserSessionService().editUserSession(userSession);

      String city = userSession.getCity();
      getMessageSendingService().sendMessage(userSession, "morning.and.afternoon.notifications",
          new String[]{city}, startMenuKeyboardBuilder.build());
    } else {
      getMessageSendingService().sendMessage(userSession,
          "morning.and.afternoon.notifications.exists");
    }
  }
}
