package com.manzar.telegramweatherbot.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.manzar.telegramweatherbot.model.ConversationState;
import com.manzar.telegramweatherbot.model.UserRequest;
import com.manzar.telegramweatherbot.model.UserSession;
import com.manzar.telegramweatherbot.service.MessageSendingService;
import com.manzar.telegramweatherbot.service.UserSessionService;
import com.manzar.telegramweatherbot.service.factory.UserRequestFactory;
import com.manzar.telegramweatherbot.util.CityNameValidator;
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
class CityEnteredHandlerTest {

  @Mock
  private MessageSendingService messageSendingService;
  @Mock
  private UserSessionService userSessionService;
  @Mock
  private CityNameValidator cityNameValidator;
  @InjectMocks
  private CityEnteredHandler cityEnteredHandler;


  @ParameterizedTest
  @MethodSource("provideUserRequestsAndResults")
  void isApplicableWhenSessionStateEqualsWaitingForCity(UserRequest userRequest, boolean expected) {
    assertEquals(expected, cityEnteredHandler.isApplicable(userRequest));
  }

  @Test
  void handleSendsCityNotFoundMessageIfCityWasNotFound() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_CITY);
    when(cityNameValidator.enteredCityExists(anyString())).thenReturn(false);

    cityEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1)).sendMessage(userRequest.getUserSession(),
        "city.not.found");
  }

  @Test
  void handleSendsEnterDateMessageIfCityWasFound() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_CITY);
    when(cityNameValidator.enteredCityExists(anyString())).thenReturn(true);

    cityEnteredHandler.handle(userRequest);

    verify(messageSendingService, times(1)).sendMessage(userRequest.getUserSession(), "enter.date");
  }

  @Test
  void handleChangesSessionStateIfCityWasFound() {
    UserRequest userRequest = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED);
    UserSession userSession = userRequest.getUserSession();
    when(cityNameValidator.enteredCityExists(anyString())).thenReturn(true);

    cityEnteredHandler.handle(userRequest);

    verify(userSessionService, times(1)).editUserSession(userSession);
    assertEquals(ConversationState.WAITING_FOR_DATE, userSession.getConversationState());
  }


  private static Stream<Arguments> provideUserRequestsAndResults() {
    UserRequest requestWithoutText = UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.WAITING_FOR_CITY);
    requestWithoutText.getUpdate().getMessage().setText(null);
    return Stream.of(Arguments.of(
        UserRequestFactory.createRequestWithStateAndTestMessage(ConversationState.WAITING_FOR_CITY),
        true), Arguments.of(UserRequestFactory.createRequestWithStateAndTestMessage(
        ConversationState.CONVERSATION_STARTED), false), Arguments.of(requestWithoutText, false));
  }
}