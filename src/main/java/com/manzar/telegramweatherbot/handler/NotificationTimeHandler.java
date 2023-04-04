package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.NotificationService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.TimeUtils;
import com.manzar.telegramweatherbot.util.UpdateParser;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handles notification time, chosen by user.
 */
@Component
public class NotificationTimeHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  private final NotificationService notificationService;
  private final StartMenuKeyboardBuilder startMenuKeyboardBuilder;


  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  public NotificationTimeHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, LocalizationService localizationService,
      NotificationService notificationService, StartMenuKeyboardBuilder startMenuKeyboardBuilder) {
    super(messageSendingService, userSessionService, localizationService);
    this.notificationService = notificationService;
    this.startMenuKeyboardBuilder = startMenuKeyboardBuilder;
  }

  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_NOTIFICATION_TIME);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    Update update = requestToDispatch.getUpdate();
    UserSession userSession = requestToDispatch.getUserSession();
    String notificationTimeToParse = UpdateParser.getText(update);

    if (TimeUtils.isValid(notificationTimeToParse)) {
      setTomorrowNotification(userSession, notificationTimeToParse);
    } else {
      getMessageSendingService().sendMessage(userSession, "invalid.notification.time");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }

  private void setTomorrowNotification(UserSession userSession,
      String notificationTimeToParse) {
    LocalTime notificationTime = LocalTime.parse(notificationTimeToParse);

    boolean notificationCreated = notificationService.createTomorrowNotification(userSession,
        Optional.of(notificationTime));

    if (notificationCreated) {
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getUserSessionService().editUserSession(userSession);

      String[] params = new String[]{userSession.getCity(), notificationTime.toString()};
      getMessageSendingService().sendMessage(userSession, "tomorrow.notifications",
          params, startMenuKeyboardBuilder.build());
    } else {
      getMessageSendingService().sendMessage(userSession,
          "tomorrow.notification.with.same.time.exists");
    }
  }
}
