package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.NotificationTypeKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SetNotificationsHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private NotificationTypeKeyboardBuilder notificationTypeKeyboardBuilder;

  @InjectMocks
  private SetNotificationsHandler setNotificationsHandler;


  @Test
  void handleSendsSpecifyCityFirstMessageIfUserSessionCityIsNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();
    userSession.setCity(null);

    setNotificationsHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "specify.city.first");
  }

  @Test
  void handleSendsChooseNotificationTypeAndBuildsNotificationTypeKeyboardIfUserSessionCityIsNotNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    setNotificationsHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "notification.type", notificationTypeKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToWaitingForNotificationTypeIfUserSessionCityIsNotNull() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();

    setNotificationsHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_NOTIFICATION_TYPE,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

}