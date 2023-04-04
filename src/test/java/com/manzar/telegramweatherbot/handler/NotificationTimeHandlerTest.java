package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.NotificationService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationTimeHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private NotificationService notificationService;
  @Mock
  private StartMenuKeyboardBuilder startMenuKeyboardBuilder;
  @InjectMocks
  private NotificationTimeHandler notificationTimeHandler;


  @ParameterizedTest
  @MethodSource("provideUserRequestsAndResults")
  void isApplicableWhenSessionStateEqualsWaitingForNotificationTime(UserRequest userRequest,
      boolean expected) {
    assertEquals(expected, notificationTimeHandler.isApplicable(userRequest));
  }

  @Test
  void handleSendsInvalidNotificationTimeMessageIfTimeIsInvalid() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, "invalid_time");

    notificationTimeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userRequest.getUserSession(), "invalid.notification.time");
  }

  @Test
  void handleSendsMessageAboutTomorrowNotificationsIfTimeIsValid() {
    String notificationTime = "07:00";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, notificationTime);
    UserSession userSession = userRequest.getUserSession();
    String city = userSession.getCity();
    String[] params = new String[]{city, notificationTime};
    LocalTime localTime = LocalTime.parse(notificationTime);

    when(notificationService.createTomorrowNotification(userSession,
        Optional.of(localTime))).thenReturn(true);

    notificationTimeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "tomorrow.notifications", params,
            startMenuKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToConversationStartedIfDateIsValid() {
    String notificationTime = "07:00";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, notificationTime);
    UserSession userSession = userRequest.getUserSession();
    LocalTime localTime = LocalTime.parse(notificationTime);

    when(notificationService.createTomorrowNotification(userSession,
        Optional.of(localTime))).thenReturn(true);

    notificationTimeHandler.handle(userRequest);

    assertEquals(ConversationState.CONVERSATION_STARTED, userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

  @Test
  void handleSendsMessageAboutExistingTomorrowNotificationIfNotificationForGivenTimeAlreadyExists() {
    String notificationTime = "07:00";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, notificationTime);
    UserSession userSession = userRequest.getUserSession();
    LocalTime localTime = LocalTime.parse(notificationTime);

    when(notificationService.createTomorrowNotification(userSession,
        Optional.of(localTime))).thenReturn(false);

    notificationTimeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "tomorrow.notification.with.same.time.exists");
  }


  @Test
  void handleCreatesTomorrowNotification() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, "07:00");
    LocalTime notificationTime = LocalTime.of(7, 0);

    notificationTimeHandler.handle(userRequest);

    verify(notificationService, times(1)).createTomorrowNotification(userRequest.getUserSession(),
        Optional.of(notificationTime));
  }

  private static Stream<Arguments> provideUserRequestsAndResults() {
    UserRequest requestWithoutText = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME);
    requestWithoutText.getUpdate().getMessage().setText(null);
    return Stream.of(
        Arguments.of(
            UserRequestFactory.createRequestWithStateAndTestMessage(
                ConversationState.WAITING_FOR_NOTIFICATION_TIME),
            true),
        Arguments.of(UserRequestFactory.createRequestWithStateAndTestMessage(
            ConversationState.CONVERSATION_STARTED), false),
        Arguments.of(requestWithoutText, false));
  }
}