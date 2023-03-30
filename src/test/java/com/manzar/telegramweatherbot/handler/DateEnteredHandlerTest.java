package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.manzar.telegramweatherbot.keyboard.StartMenuKeyboardBuilder;
import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.WeatherService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
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
class DateEnteredHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private WeatherService weatherService;
  @Mock
  private StartMenuKeyboardBuilder startMenuKeyboardBuilder;
  @InjectMocks
  private DateEnteredHandler dateEnteredHandler;

  @ParameterizedTest
  @MethodSource("provideUserRequestsAndResults")
  void isApplicableWhenSessionStateEqualsWaitingForDate(UserRequest userRequest,
      boolean expected) {
    assertEquals(expected, dateEnteredHandler.isApplicable(userRequest));
  }

  @Test
  void handleSendsInvalidDateMessageIfDateIsInvalid() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_DATE, "invalid_date");

    dateEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1))
        .sendMessage(userRequest.getUserSession(), "invalid.date");
  }

  @Test
  void handleChangesSessionStateToConversationStartedIfDateIsValid() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndText(
        ConversationState.WAITING_FOR_DATE, "27/03");
    UserSession userSession = userRequest.getUserSession();

    dateEnteredHandler.handle(userRequest);

    assertEquals(ConversationState.CONVERSATION_STARTED, userSession.getConversationState());
    verify(userSessionService, times(1)).editUserSession(userSession);
  }


  private static Stream<Arguments> provideUserRequestsAndResults() {
    UserRequest requestWithoutText = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_DATE);
    requestWithoutText.getUpdate().getMessage().setText(null);
    return Stream.of(
        Arguments.of(
            UserRequestFactory.createRequestWithStateAndTestMessage(
                ConversationState.WAITING_FOR_DATE),
            true),
        Arguments.of(UserRequestFactory.createRequestWithStateAndTestMessage(
            ConversationState.CONVERSATION_STARTED), false),
        Arguments.of(requestWithoutText, false));
  }

}