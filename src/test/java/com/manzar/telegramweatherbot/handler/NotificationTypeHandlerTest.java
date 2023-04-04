package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.manzar.telegramweatherbot.keyboard.NotificationTimeKeyboardBuilder;
import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.LocalizationService;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.NotificationService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
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
class NotificationTypeHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private LocalizationService localizationService;
  @Mock
  private StartMenuKeyboardBuilder startMenuKeyboardBuilder;
  @Mock
  private NotificationTimeKeyboardBuilder notificationTimeKeyboardBuilder;
  @Mock
  private NotificationService notificationService;
  @InjectMocks
  private NotificationTypeHandler notificationTypeHandler;

  @ParameterizedTest
  @MethodSource("provideUserRequestsAndResults")
  void isApplicableWhenSessionStateEqualsWaitingForNotificationTime(UserRequest userRequest,
      boolean expected) {
    assertEquals(expected, notificationTypeHandler.isApplicable(userRequest));
  }

  @Test
  void handleSendsMessageToChooseNotificationTimeIfTomorrowNotificationHasBeenChosen() {
    String tomorrowButton = "☑️ Tomorrow";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, tomorrowButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession, tomorrowButton,
        "tomorrow.type")).thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "notification.time", notificationTimeKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToWaitingForNotificationTimeIfTomorrowNotificationHasBeenChosen() {
    String tomorrowButton = "☑️ Tomorrow";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, tomorrowButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession, tomorrowButton,
        "tomorrow.type")).thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    assertEquals(ConversationState.WAITING_FOR_NOTIFICATION_TIME,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

  @Test
  void handleCreatesTomorrowNotificationIfTomorrowNotificationHasBeenChosen() {
    String tomorrowButton = "☑️ Tomorrow";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, tomorrowButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession, tomorrowButton,
        "tomorrow.type")).thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    verify(notificationService, times(1)).createTomorrowNotification(userSession,
        Optional.empty());
  }

  @Test
  void handleSendsMessageAboutMorningAndAfternoonNotificationAndBuildsStartKeyboardIfMorningAndAfternoonNotificationNotificationHasBeenChosen() {
    String morningAndAfternoonButton = "️☑️ Morning and afternoon";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, morningAndAfternoonButton);
    UserSession userSession = userRequest.getUserSession();
    String[] params = new String[]{userSession.getCity()};

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "tomorrow.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "morning.and.afternoon.type"))
        .thenReturn(true);
    when(notificationService.createMorningAndAfternoonNotification(userSession
    )).thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "morning.and.afternoon.notifications", params,
            startMenuKeyboardBuilder.build());
  }

  @Test
  void handleChangesSessionStateToConversationStartedIfMorningAndAfternoonNotificationHasBeenChosen() {
    String morningAndAfternoonButton = "️☑️ Morning and afternoon";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, morningAndAfternoonButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "tomorrow.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "morning.and.afternoon.type"))
        .thenReturn(true);
    when(notificationService.createMorningAndAfternoonNotification(userSession)).thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    assertEquals(ConversationState.CONVERSATION_STARTED,
        userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }

  @Test
  void handleCreatesMorningAndAfternoonNotificationIfMorningAndAfternoonNotificationHasBeenChosen() {
    String morningAndAfternoonButton = "️☑️ Morning and afternoon";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, morningAndAfternoonButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "tomorrow.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "morning.and.afternoon.type"))
        .thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    verify(notificationService, times(1)).createMorningAndAfternoonNotification(userSession);
  }

  @Test
  void handleSendsMessageAboutMorningAndAfternoonNotificationsExistsIfNotificationsAlreadyExists() {
    String morningAndAfternoonButton = "️☑️ Morning and afternoon";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, morningAndAfternoonButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "tomorrow.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        morningAndAfternoonButton,
        "morning.and.afternoon.type"))
        .thenReturn(true);
    when(notificationService.createMorningAndAfternoonNotification(userSession
    )).thenReturn(false);

    notificationTypeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "morning.and.afternoon.notifications.exists");
  }

  @Test
  void handleSendsMessageAboutNotificationsUnfollowedIfUnfollowNotificationsHasBeenChosen() {
    String unfollowNotificationsButton = "🔕 Unfollow Notifications";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, unfollowNotificationsButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        unfollowNotificationsButton,
        "tomorrow.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        unfollowNotificationsButton,
        "morning.and.afternoon.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        unfollowNotificationsButton,
        "unfollow.notifications"))
        .thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userSession, "notifications.unfollowed");
  }

  @Test
  void handleDeletesNotificationsIfUnfollowNotificationsHasBeenChosen() {
    String unfollowNotificationsButton = "🔕 Unfollow Notifications";
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_NOTIFICATION_TIME, unfollowNotificationsButton);
    UserSession userSession = userRequest.getUserSession();

    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        unfollowNotificationsButton,
        "tomorrow.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        unfollowNotificationsButton,
        "morning.and.afternoon.type"))
        .thenReturn(false);
    when(localizationService.localizedButtonLabelEqualsGivenText(userSession,
        unfollowNotificationsButton,
        "unfollow.notifications"))
        .thenReturn(true);

    notificationTypeHandler.handle(userRequest);

    verify(notificationService, times(1)).deleteNotifications(userSession.getTelegramId());
  }


  private static Stream<Arguments> provideUserRequestsAndResults() {
    UserRequest requestWithoutText = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_NOTIFICATION_TYPE);
    requestWithoutText.getUpdate().getMessage().setText(null);
    return Stream.of(
        Arguments.of(
            UserRequestFactory.createRequestWithStateAndTestMessage(
                ConversationState.WAITING_FOR_NOTIFICATION_TYPE),
            true),
        Arguments.of(UserRequestFactory.createRequestWithStateAndTestMessage(
            ConversationState.CONVERSATION_STARTED), false),
        Arguments.of(requestWithoutText, false));
  }
}