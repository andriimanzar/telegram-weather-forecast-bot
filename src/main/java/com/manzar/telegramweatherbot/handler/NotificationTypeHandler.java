package com.manzar.telegramweatherbot.handler;

import static com.manzar.telegramweatherbot.constant.ButtonLabel.FOR_MORNING_AND_AFTERNOON;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.FOR_TOMORROW;
import static com.manzar.telegramweatherbot.constant.ButtonLabel.UNFOLLOW_NOTIFICATIONS;

import com.manzar.telegramweatherbot.keyboard.NotificationTimeKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.NotificationService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Handles notification type, chosen by user.
 */
@Component
public class NotificationTypeHandler extends AbstractUserRequestHandler implements
    UserRequestHandler {

  private final NotificationService notificationService;
  private final NotificationTimeKeyboardBuilder notificationTimeKeyboardBuilder;

  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  public NotificationTypeHandler(MessageSendingService messageSendingService,
      UserSessionService userSessionService, NotificationService notificationService,
      NotificationTimeKeyboardBuilder notificationTimeKeyboardBuilder) {
    super(messageSendingService, userSessionService);
    this.notificationService = notificationService;
    this.notificationTimeKeyboardBuilder = notificationTimeKeyboardBuilder;
  }

  /**
   * This constructor calls the constructor of the AbstractUserRequestHandler to initialize the
   * common fields inherited from the parent.
   */
  @Override
  public boolean isApplicable(UserRequest request) {
    return isText(request.getUpdate()) && request.getUserSession().getConversationState()
        .equals(ConversationState.WAITING_FOR_NOTIFICATION_TYPE);
  }

  @Override
  public void handle(UserRequest requestToDispatch) {
    String chosenOption = requestToDispatch.getUpdate().getMessage().getText();
    UserSession userSession = requestToDispatch.getUserSession();
    Long chatId = requestToDispatch.getChatId();

    if (chosenOption.equals(FOR_TOMORROW.getValue())) {
      notificationService.createTomorrowNotification(userSession, chatId, Optional.empty());
      userSession.setConversationState(ConversationState.WAITING_FOR_NOTIFICATION_TIME);
      getMessageSendingService().sendMessage(chatId,
          "🕒 Please choose a time when you'd like to receive notifications.",
          notificationTimeKeyboardBuilder.build());

    } else if (chosenOption.equals(FOR_MORNING_AND_AFTERNOON.getValue())) {
      notificationService.createMorningAndAfternoonNotification(userSession, chatId);
      userSession.setConversationState(ConversationState.CONVERSATION_STARTED);
      getMessageSendingService().sendMessage(chatId,
          "🌤️ You will receive weather forecast notifications for "
              + requestToDispatch.getUserSession().getCity()
              + " at 7:00 AM and 3:00 PM every day. Stay updated! 🌦️");

    } else if (chosenOption.equals(UNFOLLOW_NOTIFICATIONS.getValue())) {
      notificationService.deleteNotifications(userSession.getTelegramId());
      userSession.setConversationState(ConversationState.WAITING_FOR_NOTIFICATION_TYPE);
      getMessageSendingService().sendMessage(chatId,
          "🔕 You have unfollowed all weather forecast notifications. "
              + "You will no longer receive updates about the weather in your city. "
              + "If you change your mind, you can always follow the notifications again later. 🌤️");
    }

    getUserSessionService().editUserSession(userSession);
  }

  @Override
  public boolean isGlobal() {
    return false;
  }
}
