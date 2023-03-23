package com.manzar.telegramweatherbot.handler;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.NotificationService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.util.TimeUtils;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.stereotype.Component;

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
      UserSessionService userSessionService, NotificationService notificationService,
      StartMenuKeyboardBuilder startMenuKeyboardBuilder) {
    super(messageSendingService, userSessionService);
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
    String notificationTimeToParse = requestToDispatch.getUpdate().getMessage().getText();
    Long chatId = requestToDispatch.getChatId();

    if (TimeUtils.isValid(notificationTimeToParse)) {
      UserSession userSession = requestToDispatch.getUserSession();
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      LocalTime notificationTime = LocalTime.parse(notificationTimeToParse);

      notificationService.createTomorrowNotification(requestToDispatch.getUserSession(), chatId,
          Optional.of(notificationTime));
      getUserSessionService().editUserSession(userSession);

      getMessageSendingService().sendMessage(chatId,
          "🌤️ You will receive weather forecast notifications for "
              + requestToDispatch.getUserSession().getCity() + " at " + notificationTime
              + " every day. Stay updated!🌦️", startMenuKeyboardBuilder.build());
    } else {
      getMessageSendingService().sendMessage(chatId,
          "⚠️ Please enter the notification time in `HH:mm` format with `00` minutes.");
    }
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
